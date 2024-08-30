package com.twinkle.framework.bootstarter.controller;

import com.twinkle.framework.api.constant.ResultCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.data.GeneralResult;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.bootstarter.data.HelloRequest;
import com.twinkle.framework.bootstarter.data.HelloResponse;
import com.twinkle.framework.bootstarter.data.Title;
import com.twinkle.framework.bootstarter.service.HelloWorld2Service;
import com.twinkle.framework.connector.server.AbstractServer;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.serialize.JsonIntrospectionSerializerFactory;
import com.twinkle.framework.struct.serialize.JsonSerializer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-17 17:46<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Tag(name = "HelloWorld", description = "Test Hello World")
@RestController
@Slf4j
public class HelloController extends AbstractServer {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HelloWorld2Service helloWorldService;

    @Operation(summary = "获取用户Token")
    @RequestMapping(value = "authsec/token/{_userName}", method = RequestMethod.POST)
    public GeneralResult<Object> createAuthenticationToken(
            @Parameter(name = "请求体") @RequestBody HelloRequest[] _request,
            @Parameter(name = "UserName") @PathVariable(value = "_userName") String _userName,
            @RequestParam(value = "_testParam", defaultValue = "DDDFF") String _testParam) throws Exception {
        log.info("The request body is AA");
        log.info("The request body is: {}", _request);
        log.info("The request body is: {} -> {}", _request, _userName);

        log.info("The _testParam = [{}].", _testParam);
//        log.info("The request _testParam = [{}].", request.getParameter("_testParam"));
        String tempContent = this.helloWorldService.sayHello("assessmgmt");

        GeneralResult<Object> tempResult = new GeneralResult<>();
        HelloResponse tempResponse = new HelloResponse();
        tempResponse.setName("CXJ110");
        Title tempTitle = new Title();
        tempTitle.setDept("GG");
        tempTitle.setPosition("HH");
        List<Title> tempTitles = new ArrayList<>();
        tempTitles.add(tempTitle);
        tempResponse.setTitles(tempTitles);
        tempResult.setCode(ResultCode.OPERATION_SUCCESS);
        tempResult.setData(testAttribute());
        return tempResult;
    }

    private StructAttribute testAttribute(){
        ClassPathResource classPathResource = new ClassPathResource("TestAttr.json");

        String tempConfiguration = null;
        try {
            tempConfiguration = IOUtils.toString(classPathResource.getInputStream(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SerializerFactory tempFactory = new JsonIntrospectionSerializerFactory();
        Serializer tempSerializer = tempFactory.getSerializer("TestDemo:TestRequest");
        JsonSerializer tempJsonSerializer = (JsonSerializer)tempSerializer;
        StructAttribute tempAttribute  = tempJsonSerializer.read(tempConfiguration);
        log.info("The attribute is: {}", tempAttribute);
        return tempAttribute;
    }

    @Operation(summary = "Test Get")
    @RequestMapping(value = "authsec/get/{_addressId}", method = RequestMethod.POST)
    public GeneralResult<String> getRequestString(
            //@ApiParam(value = "请求体") @RequestBody HelloRequest _request,
            @RequestParam(value = "_param1", defaultValue = "cxj110") String _testParam1,
            @RequestParam(value = "_param2") String _testParam2,
            @Parameter(name = "_addressId") @PathVariable(value = "_addressId") String _addressId,
            @Parameter(name = "requestBody") @RequestBody String _requestBody) {
        log.info("The request is: {} -> {}, {}", _testParam1, _testParam2, _addressId);
        NormalizedContext tempNc = this.getNormalizedContext();
        this.decodeParameter(tempNc, 0, _testParam1);
        this.decodeParameter(tempNc, 1, _testParam2);
        this.decodeParameter(tempNc, 3, _addressId);

        GeneralResult<String> tempResult = new GeneralResult<>();
        try {
            this.invokeRuleChain(tempNc, "TestRuleChain");
            tempResult.setCode(ResultCode.OPERATION_SUCCESS);
            tempResult.setData(this.encodeReturnData(tempNc, 4));
        } catch (RuleException e) {
            log.error("Encountered error while applying the rule chain[{}]. Exception: {}", "TestRuleChain", e);
            tempResult.setCode(e.getCode());
            tempResult.setData(e.getMessage());
        }
        return tempResult;
    }
}
