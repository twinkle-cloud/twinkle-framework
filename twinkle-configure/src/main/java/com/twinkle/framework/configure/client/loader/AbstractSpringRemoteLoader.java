package com.twinkle.framework.configure.client.loader;

import com.twinkle.framework.configure.client.data.ConfigClientProperties;
import com.twinkle.framework.configure.client.data.IRemotePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-17 10:42<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractSpringRemoteLoader extends AbstractRemoteLoader{
    public AbstractSpringRemoteLoader(ConfigClientProperties _properties){
        super(_properties);
    }
    @Override
    String getRemoteEnv(ConfigClientProperties properties, String label) {
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
