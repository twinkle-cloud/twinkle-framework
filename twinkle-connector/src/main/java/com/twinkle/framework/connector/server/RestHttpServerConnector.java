package com.twinkle.framework.connector.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.connector.data.HttpAttrNEAttrMapItem;
import com.twinkle.framework.core.context.ContextSchema;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-14 17:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RestHttpServerConnector implements ServerConnector {

    public RestHttpServerConnector() {
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public void registerAsService() {

    }
}
