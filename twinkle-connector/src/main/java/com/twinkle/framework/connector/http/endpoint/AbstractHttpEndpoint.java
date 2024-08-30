package com.twinkle.framework.connector.http.endpoint;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.asm.descriptor.*;
import com.twinkle.framework.asm.handler.MethodInstructionHandler;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.core.lang.MethodAttribute;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 11:19<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Getter
public abstract class AbstractHttpEndpoint extends AbstractComponent implements HttpEndpoint {
    /**
     * The request type for this method.
     */
    private HttpEndPointMethod requestType;
    /**
     * The method name for this end point.
     */
    private String methodName;
    /**
     * The description for this end point.
     */
    private String description;
    /**
     * The URL for this end point.
     */
    private String url;
    /**
     * The request info for this end point.
     */
    private HttpRequest httpRequest;
    /**
     * The http response for this end point.
     */
    private HttpResponse httpResponse;
    /**
     * The Rule Chain for this end point.
     */
    private String ruleChainName;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.methodName = _conf.getString("Name");
        this.requestType = HttpEndPointMethod.valueOf(_conf.getIntValue("RequestType"));
        this.description = _conf.getString("Description");
        if (StringUtils.isBlank(this.description)) {
            this.description = this.methodName;
        }
        this.url = _conf.getString("URL");
        this.ruleChainName = _conf.getString("RuleChain");
        this.httpRequest = ComponentFactory.getInstance().loadGeneralComponent(_conf.getJSONObject("Request"));
        this.httpResponse = ComponentFactory.getInstance().loadGeneralComponent(_conf.getJSONObject("Response"));
    }

    @Override
    public MethodTypeDescriptor getMethodDescriptor() {
        JSONObject tempObj = new JSONObject();
        tempObj.put(MethodAttribute.EXT_INFO_RULE_CHAIN, this.ruleChainName);
        tempObj.put(MethodAttribute.EXT_INFO_RETURN_NC_INDEX, this.httpResponse.getResponseAttributeNCIndex());
        MethodTypeDescriptor tempMethod = MethodTypeDescriptorImpl.builder()
                .access(Opcodes.ACC_PUBLIC)
                .annotations(getAnnotaions())
                .name(this.getMethodName())
                .instructionHandler(this.getInstructionHandler())
                .parameterAttrs(this.httpRequest.getMethodParameters(this.requestType))
                .localParameterAttrs(this.getMethodLocalParameters())
                .returnType(this.getReturnTypeDescriptor())
                .extraInfo(tempObj)
                .build();
        return tempMethod;
    }

    /**
     * Get method local parameters.
     * Method Instruction handler will add the local parameters.
     * So, no need set here anymore.
     *
     * @return
     */
    @Deprecated
    private List<AttributeDescriptor> getMethodLocalParameters(){
        List<AttributeDescriptor> tempAttrDescriptorList = new ArrayList<>();
        return tempAttrDescriptorList;
    }

    /**
     * Get the instruction method name.
     *
     * @return
     */
    protected abstract MethodInstructionHandler getInstructionHandler();

    /**
     * Get Method
     *
     * @return
     */
    private String getMethodName() {
        String tempName = StringUtils.capitalize(this.methodName);
        switch (this.requestType) {
            case GET:
                return "get" + tempName;
            case PUT:
                return "update" + tempName;
            case POST:
                return "post" + tempName;
            case DELETE:
                return "delete" + tempName;
            default:
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Http Method[" + this.requestType + "] is not supported currently.");
        }
    }

    /**
     * Pack the method's annotations.
     *
     * @return
     */
    private Set<String> getAnnotaions() {
        Set<String> tempAnnotations = new HashSet<>();
        tempAnnotations.add("@io.swagger.v3.oas.annotations.Operation(summary = \"" + this.description + "\")");
        switch (this.requestType) {
            case GET:
                tempAnnotations.add("@org.springframework.web.bind.annotation.RequestMapping(value = \"" + this.url + "\", method = org.springframework.web.bind.annotation.RequestMethod.GET)");
                return tempAnnotations;
            case PUT:
                tempAnnotations.add("@org.springframework.web.bind.annotation.RequestMapping(value = \"" + this.url + "\", method = org.springframework.web.bind.annotation.RequestMethod.PUT)");
                return tempAnnotations;
            case POST:
                tempAnnotations.add("@org.springframework.web.bind.annotation.RequestMapping(value = \"" + this.url + "\", method = org.springframework.web.bind.annotation.RequestMethod.POST)");
                return tempAnnotations;
            case DELETE:
                tempAnnotations.add("@org.springframework.web.bind.annotation.RequestMapping(value = \"" + this.url + "\", method = org.springframework.web.bind.annotation.RequestMethod.DELETE)");
                return tempAnnotations;
            default:
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Http Method[" + this.requestType + "] is not supported currently.");
        }
    }

    private TypeDescriptor getReturnTypeDescriptor() {
        if (this.httpResponse != null) {
            return this.httpResponse.getMethodReturnType();
        }
        TypeDescriptor tempReturn = TypeDescriptorImpl.builder()
                .className("com.twinkle.framework.api.data.GeneralResult<java.lang.String>")
                .name("GeneralResult")
                .description("Lcom/twinkle/framework/api/data.GeneralResult<Ljava/lang/String;>;")
                .build();
        return tempReturn;
    }
}
