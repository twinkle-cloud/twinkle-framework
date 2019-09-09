package com.twinkle.framework.bootstarter.controller;

import com.twinkle.framework.api.constant.ResultCode;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.bootstarter.data.HelloDemo;
import com.twinkle.framework.bootstarter.data.HelloRequest;
import com.twinkle.framework.bootstarter.service.HelloWorld2Service;
import com.twinkle.framework.api.data.GeneralResult;
import com.twinkle.framework.connector.server.AbstractServer;
import com.twinkle.framework.core.context.model.NormalizedContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-17 17:46<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@RestController
@Slf4j
@Api
public class HelloController extends AbstractServer {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HelloWorld2Service helloWorldService;

    @ApiOperation(value = "获取用户Token")
    @RequestMapping(value = "authsec/token/{_userName}", method = RequestMethod.POST)
    public GeneralResult<String> createAuthenticationToken(
            @ApiParam(value = "请求体") @RequestBody HelloDemo _request,
            @ApiParam(value = "UserName") @PathVariable(value = "_userName") String _userName,
            @RequestParam(value = "_testParam", defaultValue = "DDDFF") String _testParam) throws Exception {
        log.info("The request body is AA");
        log.info("The request body is: {}", _request);
        log.info("The request body is: {} -> {}", _request, _userName);

        log.info("The _testParam = [{}].", _testParam);
        log.info("The request _testParam = [{}].", request.getParameter("_testParam"));
        String tempContent = this.helloWorldService.sayHello(_request.getName());

        GeneralResult<String> tempResult = new GeneralResult<>();
        tempResult.setCode(ResultCode.OPERATION_SUCCESS);
        tempResult.setData(tempContent);
        return tempResult;
    }


    @ApiOperation(value = "Test Get")
    @RequestMapping(value = "authsec/get/{_addressId}", method = RequestMethod.POST)
    public GeneralResult<String> getRequestString(
            //@ApiParam(value = "请求体") @RequestBody HelloRequest _request,
            @RequestParam(value = "_param1", defaultValue = "cxj110") String _testParam1,
            @RequestParam(value = "_param2") String _testParam2,
            @ApiParam(value = "_addressId") @PathVariable(value = "_addressId") String _addressId,
            @ApiParam(value = "requestBody") @RequestBody String _requestBody) {
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
