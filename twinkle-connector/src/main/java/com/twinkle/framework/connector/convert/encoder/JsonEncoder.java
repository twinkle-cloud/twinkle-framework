package com.twinkle.framework.connector.convert.encoder;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.context.model.DefaultNormalizedContext;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-14 18:10<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class JsonEncoder implements Encoder {
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public Object handleEncode(DefaultNormalizedContext _nc) {
        return null;
    }
}
