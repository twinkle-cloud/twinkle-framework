package com.twinkle.framework.bootstarter.service.impl;

import com.twinkle.framework.bootstarter.service.HelloWorld2Service;
import com.twinkle.framework.datasource.annotation.TwinkleDataSource;
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
//    @Autowired
//    private JdbcTemplate jdbcTemplate;

    private String dataSource;

    public HelloWorld2ServiceImpl(){
        this.dataSource = "usermgmt";
    }

    @TwinkleDataSource(value="#_name")
    @Override
    public String sayHello(String _name) {
//        jdbcTemplate.update("INSERT INTO U_USER (USER_NAME,PASSWORD) VALUES(?, ?)",
//                new Object[]{"cxj110", "abcd1234"});

        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("Hello ");
        tempBuilder.append(_name);
        tempBuilder.append("!");
        return tempBuilder.toString();
    }
}
