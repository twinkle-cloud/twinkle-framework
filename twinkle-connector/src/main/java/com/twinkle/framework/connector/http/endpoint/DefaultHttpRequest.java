package com.twinkle.framework.connector.http.endpoint;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.asm.descriptor.AttributeDescriptor;
import com.twinkle.framework.asm.descriptor.AttributeDescriptorImpl;
import com.twinkle.framework.asm.descriptor.TypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptorImpl;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.JavaAttributeInfo;
import com.twinkle.framework.context.converter.JavaAttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Collections;
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
@Slf4j
public class DefaultHttpRequest extends AbstractHttpHandler implements HttpRequest {
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
        this.requestHeaderMap = this.packAttrMap(_conf.getJSONArray("RequestHeaders"), AttributeCategory.REQUEST_HEADER);
        this.requestParameterMap = this.packAttrMap(_conf.getJSONArray("RequestParameters"), AttributeCategory.REQUEST_PARAMETER);
        this.pathVariableMap = this.packAttrMap(_conf.getJSONArray("PathVariables"), AttributeCategory.PATH_VARIABLE);
        this.requestBodyNode = this.packAttributeNode(_conf.getJSONObject("RequestBody"), AttributeCategory.REQUEST_BODY);
    }

    @Override
    public List<AttributeDescriptor> getMethodParameters(HttpEndPointMethod _requestType) {
        List<AttributeDescriptor> tempResultList;
        switch (_requestType) {
            case GET:
                tempResultList = new ArrayList<>(this.requestHeaderMap.size() + this.requestParameterMap.size() + this.pathVariableMap.size());
                tempResultList.addAll(this.retrieveMethodAttribute(this.requestHeaderMap));
                tempResultList.addAll(this.retrieveMethodAttribute(this.requestParameterMap));
                tempResultList.addAll(this.retrieveMethodAttribute(this.pathVariableMap));
                return tempResultList;
            case PUT:
            case POST:
            case DELETE:
                return getMethodParameters();
            default:
                throw new RuntimeException("Does not support the attribute category[{" + _requestType + "}] currently.");
        }
    }

    @Override
    public List<AttributeDescriptor> getMethodParameters() {
        List<AttributeDescriptor> tempResultList;
        tempResultList = new ArrayList<>(this.requestHeaderMap.size() + this.requestParameterMap.size() + this.pathVariableMap.size() + 1);
        tempResultList.addAll(this.retrieveMethodAttribute(this.requestHeaderMap));
        tempResultList.addAll(this.retrieveMethodAttribute(this.requestParameterMap));
        tempResultList.addAll(this.retrieveMethodAttribute(this.pathVariableMap));
        if (this.requestBodyNode != null)
            tempResultList.add(this.packAttributeDescriptor(this.requestBodyNode));
        return tempResultList;
    }

    /**
     * Retrieve method attribute.
     *
     * @param _attrMap
     * @return
     */
    private List<AttributeDescriptor> retrieveMethodAttribute(Map<String, AttributeNode> _attrMap) {
        if (MapUtils.isEmpty(_attrMap)) {
            return Collections.EMPTY_LIST;
        }
        List<AttributeDescriptor> tempResultList = new ArrayList<>(_attrMap.size());
        for (Map.Entry<String, AttributeNode> tempEntry : _attrMap.entrySet()) {
            tempResultList.add(this.packAttributeDescriptor(tempEntry.getValue()));
        }
        return tempResultList;
    }

    /**
     * Pack Attribute descriptor.
     *
     * @param _node
     * @return
     */
    private AttributeDescriptor packAttributeDescriptor(AttributeNode _node) {
        JSONObject tempObj = new JSONObject();
        tempObj.put(Attribute.EXT_INFO_NC_INDEX, _node.getAttrNEAttrMapItem().getNeAttrIndex());
        AttributeDescriptor tempDescriptor = null;
        try {
            tempDescriptor = AttributeDescriptorImpl.builder()
                    .access(Opcodes.ACC_FINAL)
                    .name(_node.getAttrName())
                    .annotations(_node.getAnnotations())
                    .type(this.getAttrTypeDescriptor(_node))
                    .extraInfo(tempObj)
                    .build();
        } catch (ClassNotFoundException e) {
            log.warn("Struct Attribute is defined incorrectly.", e);
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Struct Attribute is defined incorrectly.");
        }
        return tempDescriptor;
    }

    /**
     * Get attribute Type descriptor.
     *
     * @param _node
     * @return
     */
    private TypeDescriptor getAttrTypeDescriptor(AttributeNode _node) throws ClassNotFoundException {
        JavaAttributeInfo tempAttrInfo = JavaAttributeConverter.convertToJavaAttribute(_node.getAttributeInfo());

        TypeDescriptor tempTypeDescriptor = TypeDescriptorImpl.builder().name(tempAttrInfo.getName())
                .className(tempAttrInfo.getClassName())
                .description(Type.getDescriptor(tempAttrInfo.getClass()))
                .build();
        return tempTypeDescriptor;
    }

}
