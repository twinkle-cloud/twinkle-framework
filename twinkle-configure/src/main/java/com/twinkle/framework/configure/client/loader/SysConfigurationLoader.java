package com.twinkle.framework.configure.client.loader;

import com.twinkle.framework.configure.client.data.ConfigClientProperties;
import com.twinkle.framework.configure.client.data.ConfigurationEnvironment;
import com.twinkle.framework.configure.client.data.ConfigurationPath;
import com.twinkle.framework.configure.client.data.IRemotePath;
import com.twinkle.framework.configure.client.parser.IConfigurationParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static com.twinkle.framework.configure.client.data.ConfigClientProperties.STATE_HEADER;
import static com.twinkle.framework.configure.client.data.ConfigClientProperties.TOKEN_HEADER;
import static org.springframework.cloud.config.client.ConfigClientProperties.AUTHORIZATION;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-15 22:35<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class SysConfigurationLoader extends AbstractRemoteLoader {

    public SysConfigurationLoader(ConfigClientProperties defaultProperties){
        super(defaultProperties);
    }
    @Override
    IConfigurationParser getConfigurationParser() {
        return null;
    }

    @Override
    IRemotePath getRemotePath(ConfigClientProperties properties, String label) {
        String path = "/{name}/{profile}";
        String name = properties.getName();
        String profile = properties.getProfile();

        Object[] args = new String[]{name, profile};
        if (StringUtils.hasText(label)) {
            if (label.contains("/")) {
                label = label.replace("/", "(_)");
            }
            args = new String[]{name, profile, label};
            path = path + "/{label}";
        }
        return null;
    }

    @Override
    String getRemoteEnv(ConfigClientProperties properties, String label, String state) {
        ResponseEntity<String> response = null;

        int noOfUrls = properties.getUri().length;
        if (noOfUrls > 1) {
            log.info("Multiple Config Server Urls found listed.");
        }
        IRemotePath tempPath = this.getRemotePath(properties, label);
        for (int i = 0; i < noOfUrls; i++) {
            try {
                HttpHeaders tempHeaders = tempPath.getHttpHeaders(properties, i);
                log.info("Fetching config from server at : " + tempPath.getFullPath());
                final HttpEntity<Void> entity = new HttpEntity<>((Void) null, tempHeaders);
                response = this.getRestTemplate(properties).exchange(tempPath.getFullPath(), HttpMethod.GET, entity,
                        String.class, tempPath.getArgs());
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                    throw e;
                }
            } catch (ResourceAccessException e) {
                log.info("Connect Timeout Exception on Url - " + properties.getUri()[i]
                        + ". Will be trying the next url if available");
                if (i == noOfUrls - 1) {
                    throw e;
                } else {
                    continue;
                }
            }
            if (response == null || response.getStatusCode() != HttpStatus.OK) {
                return null;
            }
            return response.getBody();
        }
        return null;
    }
}
