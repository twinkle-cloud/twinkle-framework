package com.twinkle.framework.configure.client.loader;

import com.alibaba.fastjson2.JSON;
import com.twinkle.framework.configure.client.data.*;
import com.twinkle.framework.configure.client.parser.IConfigurationParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static com.twinkle.framework.configure.client.data.ConfigClientProperties.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-16 11:32<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractRemoteLoader implements PropertySourceLocator {
    private RestTemplate restTemplate;

    private ConfigClientProperties defaultProperties;

    public AbstractRemoteLoader(ConfigClientProperties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    abstract IConfigurationParser getConfigurationParser();
    abstract IRemotePath getRemotePath(ConfigClientProperties properties, String label);
    abstract String getRemoteEnv(ConfigClientProperties properties, String label);
    @Override
    @Retryable(interceptor = "configServerRetryInterceptor")
    public PropertySource<?> locate(Environment environment) {
        ConfigClientProperties properties = this.defaultProperties.override(environment);
        CompositePropertySource composite = new CompositePropertySource("configService");
        Exception error = null;
        String errorBody = null;
        try {
            String[] labels = new String[]{""};
            if (StringUtils.hasText(properties.getLabel())) {
                labels = StringUtils
                        .commaDelimitedListToStringArray(properties.getLabel());
            }
            // Try all the labels until one works
            for (String label : labels) {
                String tempResult = this.getRemoteEnv(properties, label.trim());
                if (!StringUtils.isEmpty(tempResult)) {
                    log(tempResult);
                    Object tempObj = this.getConfigurationParser().doParser(tempResult);
                    if(tempObj instanceof ConfigurationEnvironment){
                        ConfigurationEnvironment tempEnv = (ConfigurationEnvironment)tempObj;
                        if (tempEnv.getConfigurationPropertySources() != null) { // result.getConfigurationPropertySources()
                            // can be null if using xml
                            for (ConfigurationPropertySource source : tempEnv.getConfigurationPropertySources()) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> map = (Map<String, Object>) source
                                        .getSource();
                                composite.addPropertySource(
                                        new MapPropertySource(source.getName(), map));
                            }
                        }

                        if (StringUtils.hasText(tempEnv.getState())
                                || StringUtils.hasText(tempEnv.getVersion())) {
                            HashMap<String, Object> map = new HashMap<>();
                            putValue(map, "config.client.state", tempEnv.getState());
                            putValue(map, "config.client.version", tempEnv.getVersion());
                            composite.addFirstPropertySource(
                                    new MapPropertySource("configClient", map));
                        }
                    } else {
                        composite.addPropertySource(
                                new MapPropertySource(IConfigurationLoader.LOGIC_CONFIGURATION_KEY, JSON.parseObject((String)tempObj)));
                    }
                    return composite;
                }
            }
        } catch (HttpServerErrorException e) {
            error = e;
            if (MediaType.APPLICATION_JSON
                    .includes(e.getResponseHeaders().getContentType())) {
                errorBody = e.getResponseBodyAsString();
            }
        } catch (Exception e) {
            error = e;
        }
        if (properties.isFailFast()) {
            throw new IllegalStateException(
                    "Could not locate ConfigurationPropertySource and the fail fast property is set, failing"
                            + (errorBody == null ? "" : ": " + errorBody),
                    error);
        }
        log.warn("Could not locate ConfigurationPropertySource: " + (errorBody == null
                ? error == null ? "label not found" : error.getMessage() : errorBody));
        return null;

    }

    private void putValue(HashMap<String, Object> map, String key, String value) {
        if (StringUtils.hasText(value)) {
            map.put(key, value);
        }
    }

    protected RestTemplate getRestTemplate(ConfigClientProperties properties){
        RestTemplate restTemplate = this.restTemplate == null
                ? getSecureRestTemplate(properties) : this.restTemplate;
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private RestTemplate getSecureRestTemplate(ConfigClientProperties client) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        if (client.getRequestReadTimeout() < 0) {
            throw new IllegalStateException("Invalid Value for Read Timeout set.");
        }
        if (client.getRequestConnectTimeout() < 0) {
            throw new IllegalStateException("Invalid Value for Connect Timeout set.");
        }
        requestFactory.setReadTimeout(client.getRequestReadTimeout());
        requestFactory.setConnectTimeout(client.getRequestConnectTimeout());
        RestTemplate template = new RestTemplate(requestFactory);
        Map<String, String> headers = new HashMap<>(client.getHeaders());
        if (headers.containsKey(AUTHORIZATION)) {
            headers.remove(AUTHORIZATION); // To avoid redundant addition of header
        }
        if (!headers.isEmpty()) {
            template.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(
                    new AbstractRemoteLoader.GenericRequestHeaderInterceptor(headers)));
        }

        return template;
    }

    private void addAuthorizationToken(ConfigClientProperties configClientProperties,
                                       HttpHeaders httpHeaders, String username, String password) {
        String authorization = configClientProperties.getHeaders().get(AUTHORIZATION);

    }

    /**
     * Adds the provided headers to the request.
     */
    public static class GenericRequestHeaderInterceptor
            implements ClientHttpRequestInterceptor {

        private final Map<String, String> headers;

        public GenericRequestHeaderInterceptor(Map<String, String> headers) {
            this.headers = headers;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            for (Map.Entry<String, String> header : this.headers.entrySet()) {
                request.getHeaders().add(header.getKey(), header.getValue());
            }
            return execution.execute(request, body);
        }

        protected Map<String, String> getHeaders() {
            return this.headers;
        }
    }


    private void log(Object result) {
        if(result instanceof ConfigurationEnvironment) {
            ConfigurationEnvironment tempEnv = (ConfigurationEnvironment) result;
            if (log.isInfoEnabled()) {
                log.info(String.format(
                        "Located environment: name=%s, profiles=%s, label=%s, version=%s, state=%s",
                        tempEnv.getName(),
                        tempEnv.getProfiles() == null ? ""
                                : Arrays.asList(tempEnv.getProfiles()),
                        tempEnv.getLabel(), tempEnv.getVersion(), tempEnv.getState()));
            }
            if (log.isDebugEnabled()) {
                List<ConfigurationPropertySource> propertySourceList = tempEnv.getConfigurationPropertySources();
                if (propertySourceList != null) {
                    int propertyCount = 0;
                    for (ConfigurationPropertySource propertySource : propertySourceList) {
                        propertyCount += propertySource.getSource().size();
                    }
                    log.debug(String.format(
                            "ConfigurationEnvironment %s has %d property sources with %d properties.",
                            tempEnv.getName(), tempEnv.getConfigurationPropertySources().size(),
                            propertyCount));
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("The Twinkle Logic Configuration is: {}", result);
            }
        }
    }
}
