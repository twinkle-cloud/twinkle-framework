package com.twinkle.framework.bootstarter.service.impl;

import com.twinkle.framework.bootstarter.service.HelloWorld2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:05<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Service
public class HelloWorld2ServiceImpl implements HelloWorld2Service {
    @Override
    public String sayHello(String _name) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("Hello ");
        tempBuilder.append(_name);
        tempBuilder.append("!");
        return tempBuilder.toString();
    }
}
