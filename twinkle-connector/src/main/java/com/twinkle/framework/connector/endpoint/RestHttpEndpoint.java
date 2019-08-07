package com.twinkle.framework.connector.endpoint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.connector.data.HttpAttrNEAttrMapItem;
import com.twinkle.framework.core.context.ContextSchema;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 11:19<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RestHttpEndpoint implements HttpEndpoint {
    /**
     * Http Header Name -> HeaderNE Map
     * Key: Http Header Name
     * Value: HttpHeaderNE
     */
    private Map<String, HttpAttrNEAttrMapItem> requestHeaderMap;
    private Map<String, HttpAttrNEAttrMapItem> responseHeaderMap;
    private Map<String, HttpAttrNEAttrMapItem> requestParameterMap;

    public RestHttpEndpoint() {
        this.requestHeaderMap = new HashMap<>(8);
        this.responseHeaderMap = new HashMap<>(8);
        this.requestParameterMap = new HashMap<>(8);
    }
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.packAttrMap(_conf.getJSONArray("RequestHeader"), this.requestHeaderMap);
        this.packAttrMap(_conf.getJSONArray("RequestParameter"), this.requestParameterMap);
        this.packAttrMap(_conf.getJSONArray("ResponseHeader"), this.responseHeaderMap);

    }

    /**
     * Pack Http attr-- Ne Attribute Map.
     *
     * @param _jsonArray
     * @param _attrMap
     */
    private void packAttrMap(JSONArray _jsonArray, Map<String, HttpAttrNEAttrMapItem> _attrMap){
        if(CollectionUtils.isNotEmpty(_jsonArray)) {
            for(int i = 0; i< _jsonArray.size(); i++) {
                JSONObject tempObj = _jsonArray.getJSONObject(i);
                HttpAttrNEAttrMapItem tempItem = HttpAttrNEAttrMapItem.builder()
                        .httpAttr(tempObj.getString("HttpAttr"))
                        .neAttr(tempObj.getString("NeAttr"))
                        .neAttrIndex(ContextSchema.getInstance().getAttributeIndex(tempObj.getString("NeAttr"), tempObj.toJSONString()))
                        .defaultValue(tempObj.getString("DefaultValue"))
                        .build();
                _attrMap.put(tempObj.getString("HttpAttr"), tempItem);
            }
        }
    }

}
