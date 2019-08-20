package com.twinkle.framework.connector.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;

import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-20 15:10<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RestHttpResponse extends AbstractHttpHandler implements HttpResponse {
    /**
     * Http Header Name -> HeaderNE Map
     * Key: Http Header Name
     * Value: HttpHeaderNE
     */
    private Map<String, AttributeNode> responseHeaderMap;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.responseHeaderMap = this.packAttrMap(_conf.getJSONArray("ResponseHeader"), AttributeCategory.RESPONSE_HEADER);
    }

    @Override
    public TypeDescriptor getMethodReturnType() {
        return null;
    }

}
