package com.twinkle.framework.connector.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.datastruct.descriptor.AttributeDescriptor;

import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-20 14:28<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class GeneralHttpRequest extends AbstractHttpHandler implements HttpRequest {
    /**
     * Request Headers.
     */
    private Map<String, AttributeNode> requestHeaderMap;
    /**
     * Request parameters.
     */
    private Map<String, AttributeNode> requestParameterMap;
    /**
     * Path variables in endpoint's URL.
     */
    private Map<String, AttributeNode> pathVariableMap;
    /**
     * Http Request Body.
     */
    private AttributeNode requestBodyNode;
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.requestHeaderMap = this.packAttrMap(_conf.getJSONArray("RequestHeader"), AttributeCategory.REQUEST_HEADER);
        this.requestParameterMap = this.packAttrMap(_conf.getJSONArray("RequestParameter"), AttributeCategory.REQUEST_PARAMETER);
        this.pathVariableMap = this.packAttrMap(_conf.getJSONArray("PathVariable"), AttributeCategory.PATH_VARIABLE);
        this.requestBodyNode = this.packAttributeNode(_conf.getJSONObject("RequestBody"), AttributeCategory.REQUEST_BODY);

    }

    @Override
    public List<AttributeDescriptor> getMethodParameters() {
        return null;
    }
}
