package com.twinkle.framework.connector.http.endpoint;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.connector.data.HttpAttrNEAttrMapItem;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-20 15:04<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractHttpHandler implements HttpHandler {

    /**
     * Pack Http attr-- Ne Attribute Map.
     *
     * @param _jsonArray
     */
    protected Map<String, AttributeNode> packAttrMap(JSONArray _jsonArray, AttributeCategory _category) {
        if (CollectionUtils.isEmpty(_jsonArray)) {
            return Collections.emptyMap();
        }
        Map<String, AttributeNode> tempMap = new HashMap<>(_jsonArray.size());
        for (int i = 0; i < _jsonArray.size(); i++) {
            JSONObject tempObj = _jsonArray.getJSONObject(i);
            AttributeNode tempNode = this.packAttributeNode(tempObj, _category);
            if(tempNode == null) {
                continue;
            }
            tempMap.put(tempObj.getString("HttpAttr"), tempNode);
        }
        return tempMap;
    }

    /**
     * Pack the request Node.
     *
     * @param _obj
     * @param _category
     * @return
     */
    protected AttributeNode packAttributeNode(JSONObject _obj, AttributeCategory _category) {
        if(_obj == null || _obj.isEmpty()) {
            return null;
        }
//        String tempNeAttrName = _obj.getString("NeAttr");
//        if(tempNeAttrName.indexOf(":") > 0) {
//            tempNeAttrName.indexOf(":")
//        }
        AttributeInfo tempAttrInfo = PrimitiveAttributeSchema.getInstance().getAttribute(_obj.getString("NeAttr"));
        if (tempAttrInfo == null) {
            log.error("The attribute[{}] does not exist in PrimitiveAttributeSchema.", _obj.toString());
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_MISSED_IN_SCHEMA, "The attribute[" + _obj.getString("NeAttr") + "] does not exist in PrimitiveAttributeSchema.");
        }

        String tempValueStr = _obj.getString("DefaultValue");
        if (StringUtils.isBlank(tempValueStr)) {
            tempValueStr = null;
        } else {
            tempValueStr = tempValueStr.trim();
        }
        String tempHttpAttr = _obj.getString("HttpAttr");
        if (StringUtils.isBlank(tempHttpAttr)) {
            log.error("The http attribute[{}] is empty here.", _obj.toString());
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_MISSED_IN_SCHEMA, "The http attribute[" + _obj.toString() + "] is not set.");
        }
        HttpAttrNEAttrMapItem tempItem = HttpAttrNEAttrMapItem.builder()
                .httpAttr(tempHttpAttr)
                .neAttr(_obj.getString("NeAttr"))
                .neAttrIndex(tempAttrInfo.getIndex())
                .defaultValue(tempValueStr)
                .build();

        AttributeNode tempNode = new AttributeNode();
        tempNode.setAttrName(_obj.getString("HttpAttr"));
        tempNode.setAttrNEAttrMapItem(tempItem);
        tempNode.addAnnotation("@io.swagger.v3.oas.annotations.Parameter(name = \"" + tempAttrInfo.getName() + "\")");
        tempNode.setAttributeInfo(tempAttrInfo);

        StringBuilder tempBuilder;
        switch (_category) {
            case REQUEST_BODY:
                tempNode.addAnnotation("@org.springframework.web.bind.annotation.RequestBody");
                break;
            case REQUEST_HEADER:
                tempBuilder = new StringBuilder("@org.springframework.web.bind.annotation.RequestHeader(value = \"" + tempHttpAttr + "\"");
                String tempStr = tempValueStr == null ? tempBuilder.append(")").toString() : tempBuilder.append(", defaultValue = \"" + tempValueStr + "\")").toString();
                tempNode.addAnnotation(tempStr);
                break;
            case REQUEST_PARAMETER:
                tempBuilder = new StringBuilder("@org.springframework.web.bind.annotation.RequestParam(value = \"" + tempHttpAttr + "\"");
                tempStr = tempValueStr == null ? tempBuilder.append(")").toString() : tempBuilder.append(", defaultValue = \"" + tempValueStr + "\")").toString();
                tempNode.addAnnotation(tempStr);
                break;
            case PATH_VARIABLE:
                tempNode.addAnnotation("@org.springframework.web.bind.annotation.PathVariable(value = \"" + tempHttpAttr + "\")");
                break;
            case RESPONSE_HEADER:
                // Response Header does not need annotation support.
            case RESPONSE_VARIABLE:
                // Response Header does not need annotation support.
                break;
            default:
                throw new RuntimeException("Does not support the attribute category[{" + _category + "}] currently.");
        }
        return tempNode;
    }


    @Data
    protected static class AttributeNode {
        private String attrName;
        private HttpAttrNEAttrMapItem attrNEAttrMapItem;
        private Set<String> annotations;
        private AttributeInfo attributeInfo;

        /**
         * Add the annotation expression for this attribute.
         *
         * @param _str
         */
        public void addAnnotation(String _str) {
            if (this.annotations == null) {
                this.annotations = new HashSet<>(4);
            }
            this.annotations.add(_str);
        }
    }

    /**
     * Attribute's category.
     */
    protected enum AttributeCategory {
        REQUEST_HEADER,
        REQUEST_PARAMETER,
        PATH_VARIABLE,
        RESPONSE_HEADER,
        REQUEST_BODY,
        RESPONSE_VARIABLE
    }
}
