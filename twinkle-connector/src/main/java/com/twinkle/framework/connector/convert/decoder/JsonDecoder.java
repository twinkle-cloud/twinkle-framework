package com.twinkle.framework.connector.convert.decoder;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.context.model.NormalizedContext;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-14 17:53<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class JsonDecoder implements Decoder {
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        // Get the Decoder Configuration.
    }

    @Override
    public void doDecode(NormalizedContext _nc) {
        // do the real decode.
    }
}
