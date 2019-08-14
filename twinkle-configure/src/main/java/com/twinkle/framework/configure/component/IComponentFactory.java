package com.twinkle.framework.configure.component;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.core.datastruct.Bean;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-17 16:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IComponentFactory {
    String KEY_CLASS_NAME = "ClassName";
    /**
     * Load the configurable component.
     *
     * @param _obj
     * @return
     */
    <T extends Configurable> T loadComponent(JSONObject _obj);

    void registerCustomizeBean(Bean _bean);
}
