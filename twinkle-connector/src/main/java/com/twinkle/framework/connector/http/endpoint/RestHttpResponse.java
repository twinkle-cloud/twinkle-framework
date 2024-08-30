package com.twinkle.framework.connector.http.endpoint;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.asm.descriptor.TypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptorImpl;

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
    /**
     * Response Attr Node.
     */
    private AttributeNode responseAttrNode;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.responseHeaderMap = this.packAttrMap(_conf.getJSONArray("ResponseHeader"), AttributeCategory.RESPONSE_HEADER);
        this.responseAttrNode = this.packAttributeNode(_conf.getJSONObject("ResultData"), AttributeCategory.RESPONSE_VARIABLE);
    }

    @Override
    public TypeDescriptor getMethodReturnType() {
        TypeDescriptor tempDescriptor = TypeDescriptorImpl.builder()
                .className("com.twinkle.framework.api.data.GeneralResult<java.lang.Object>")
                .name("GeneralResult")
                .description("Lcom/twinkle/framework/api/data.GeneralResult<Ljava/lang/Object;>;")
                .build();
        return tempDescriptor;
    }

    @Override
    public int getResponseAttributeNCIndex() {
        if(this.responseAttrNode == null) {
            return -1;
        }
        return this.responseAttrNode.getAttrNEAttrMapItem().getNeAttrIndex();
    }
}
