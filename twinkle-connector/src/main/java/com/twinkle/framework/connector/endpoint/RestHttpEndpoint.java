package com.twinkle.framework.connector.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.configure.component.IComponentFactory;
import com.twinkle.framework.core.datastruct.descriptor.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Opcodes;
import org.springframework.beans.factory.annotation.Autowired;

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
public class RestHttpEndpoint implements HttpEndpoint {
    @Autowired
    private IComponentFactory componentFactory;
    private EndPointMethod requestType;
    private String methodName;
    private String description;
    private String url;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.methodName = _conf.getString("Name");
        this.requestType = EndPointMethod.valueOf(_conf.getIntValue("RequestType"));
        this.description = _conf.getString("Description");
        if (StringUtils.isBlank(this.description)) {
            this.description = this.methodName;
        }
        this.url = _conf.getString("URL");
        this.httpRequest = this.componentFactory.loadComponent(_conf.getJSONObject("Request"));
        this.httpResponse = this.componentFactory.loadComponent(_conf.getJSONObject("Response"));
    }

    @Override
    public MethodTypeDescriptor getMethodDescriptor() {
        MethodTypeDescriptor tempMethod = MethodTypeDescriptorImpl.builder()
                .access(Opcodes.ACC_PUBLIC)
                .annotations(getAnnotaions())
                .name(this.getMethodName())
                .instructionMethodName(this.getInstructionMethodName())
                .parameterAttrs(this.httpRequest.getMethodParameters(this.requestType))
                .localParameterAttrs(this.getMethodLocalParameters())
                .returnType(this.getReturnTypeDescriptor())
                .build();
        return null;
    }

    /**
     * Get method local parameters.
     *
     * @return
     */
    private List<AttributeDescriptor> getMethodLocalParameters(){
        List<AttributeDescriptor> tempAttrDescriptorList = new ArrayList<>();
        switch (this.requestType) {
            case GET:
                return tempAttrDescriptorList;
            case PUT:
                return tempAttrDescriptorList;
            case POST:
                return tempAttrDescriptorList;
            case DELETE:
                return tempAttrDescriptorList;
            default:
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Http Method[" + this.requestType + "] is not supported currently.");
        }
    }
    /**
     * Get the instruction method name.
     *
     * @return
     */
    private String getInstructionMethodName(){
        switch (this.requestType) {
            case GET:
                return "packGetMethodInstruction";
            case PUT:
                return "packPutMethodInstruction";
            case POST:
                return "packPostMethodInstruction";
            case DELETE:
                return "packDeleteMethodInstruction";
            default:
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Http Method[" + this.requestType + "] is not supported currently.");
        }
    }

    /**
     * Get Method
     *
     * @return
     */
    private String getMethodName() {
        String tempName = this.upperFirstChar(this.methodName);
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
     * Convert the first char to Upper case.
     *
     * @param _str
     * @return
     */
    private static String upperFirstChar(String _str) {
        char[] ch = _str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return String.valueOf(ch);
    }

    /**
     * Pack the method's annotations.
     *
     * @return
     */
    private Set<String> getAnnotaions() {
        Set<String> tempAnnotations = new HashSet<>();
        tempAnnotations.add("@io.swagger.annotations.ApiOperation(value = \"" + this.description + "\")");
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
