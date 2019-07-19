package com.twinkle.framework.configure.client.data;

import com.twinkle.framework.configure.client.bootstrap.ConfigClientStateHolder;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-16 19:46<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class ConfigurationPath implements IRemotePath {
    private String path;
    private String uri;
    private List<String> argList;
    private HttpHeaders httpHeaders;
    /**
     * Add arg into the list.
     *
     * @param _arg
     */
    public void addArg(String _arg) {
        if(this.argList == null) {
            this.argList = new ArrayList<>(4);
        }
        this.argList.add(_arg);
    }


    @Override
    public String getFullPath() {
        return this.uri + this.path;
    }

    @Override
    public String[] getArgs(){
        return this.argList.toArray(new String[]{});
    }

    public void buildHttpHeader(ConfigClientProperties _properties){
        if(this.httpHeaders == null) {
            this.httpHeaders = new HttpHeaders();
        } else {
            if (StringUtils.hasText(_properties.getToken())) {
                httpHeaders.add(TOKEN_HEADER, _properties.getToken());
            }
            String state = ConfigClientStateHolder.getState();
            if (StringUtils.hasText(state) && _properties.isSendState()) {
                httpHeaders.add(STATE_HEADER, state);
            }
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        }
    }

    private void setCredentialInfo(ConfigClientProperties _properties, int _uriIndex) {
        String authorization = _properties.getHeaders().get(AUTHORIZATION);
        ConfigClientProperties.Credentials tempCredential = _properties.getCredentials(0);

        this.uri = tempCredential.getUri();
        if (tempCredential.getPassword() != null && authorization != null) {
            throw new IllegalStateException(
                    "You must set either 'password' or 'authorization'");
        }
        if (tempCredential.getPassword() != null) {
            byte[] token = Base64Utils.encode((tempCredential.getUsername() + ":" + tempCredential.getPassword()).getBytes());
            httpHeaders.remove("Authorization");
            httpHeaders.add("Authorization", "Basic " + new String(token));
        } else if (authorization != null) {
            httpHeaders.remove("Authorization");
            httpHeaders.add("Authorization", authorization);
        }
    }

    @Override
    public HttpHeaders getHttpHeaders(ConfigClientProperties _properties, int _uriIndex) {
        this.buildHttpHeader(_properties);
        this.setCredentialInfo(_properties, _uriIndex);
        return this.httpHeaders;
    }
}
