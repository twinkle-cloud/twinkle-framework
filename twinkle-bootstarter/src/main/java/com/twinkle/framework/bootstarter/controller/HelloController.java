package com.twinkle.framework.bootstarter.controller;

import com.twinkle.framework.api.constant.ResultCode;
import com.twinkle.framework.bootstarter.data.HelloRequest;
import com.twinkle.framework.bootstarter.service.HelloWorld2Service;
import com.twinkle.framework.api.data.GeneralResult;
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
public class HelloController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HelloWorld2Service helloWorldService;

    @ApiOperation(value = "获取用户Token")
    @RequestMapping(value = "authsec/token/{_userName}", method = RequestMethod.POST)
    public GeneralResult<String> createAuthenticationToken(
            @ApiParam(value = "请求体") @RequestBody HelloRequest _request,
            @ApiParam(value = "UserName") @PathVariable(value = "_userName") String _userName,
            @RequestParam(value = "_testParam", defaultValue = "DDDFF") String _testParam) throws Exception {
        log.info("The request body is AA");
        log.info("The request body is: {}", _request);
        log.info("The request body is: {} -> {}", _request, _userName);

        log.info("The _testParam = [{}].", _testParam);
        log.info("The request _testParam = [{}].", request.getParameter("_testParam"));
        String tempContent = this.helloWorldService.sayHello(_request.getUserName());

        GeneralResult<String> tempResult = new GeneralResult<>();
        tempResult.setCode(ResultCode.OPERATION_SUCCESS);
        tempResult.setData(tempContent);
        return tempResult;
    }

    public GeneralResult getRequestString(@PathVariable int _a, @PathVariable float _b) {

        GeneralResult<String> tempResult = new GeneralResult<>();
        tempResult.setCode(ResultCode.OPERATION_SUCCESS);
        tempResult.setData("DDDD");

        return null;
    }
}
