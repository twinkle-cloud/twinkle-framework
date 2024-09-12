package com.twinkle.framework.ruleengine.pool;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Pool settings
 *
 * @author chenxj
 * @date 2024/09/01
 */
@Slf4j
@Getter
public class PoolConfig {
    int initialPoolSize;
    int maxPoolSize;
    boolean strictPoolSize;
    boolean managed;
    Level contentionLogLevel;

    PoolConfig() {
    }

    public static PoolConfig fromConfig(JSONObject _conf) throws ConfigurationException {
        PoolConfig tempConfig = new PoolConfig();
        tempConfig.maxPoolSize = _conf.getIntValue("MaxPoolSize", 5);
        tempConfig.initialPoolSize = _conf.getIntValue("MinPoolSize", 1);
        tempConfig.strictPoolSize = _conf.getBooleanValue("StrictPoolSize", false);
        tempConfig.managed = _conf.getBooleanValue("Managed", true);
        tempConfig.contentionLogLevel = getContentionLogLevel(_conf.getString("PoolContentionLogLevel"));
        verify(tempConfig);
        return tempConfig;
    }

    protected static Level getContentionLogLevel(String _level) {
        String tempLevel = StringUtils.isEmpty(_level) ? Level.WARN.name() : _level;
        return !tempLevel.equalsIgnoreCase("NONE") ? Level.valueOf(tempLevel) : null;
    }

    public static PoolConfig fromString(String _ruleChainName, String[] _poolSize, PoolConfig _sourceConfig) throws ConfigurationException {
        PoolConfig tempConfig = new PoolConfig();
        tempConfig.managed = _sourceConfig.managed;
        tempConfig.maxPoolSize = _sourceConfig.maxPoolSize;
        tempConfig.strictPoolSize = _sourceConfig.strictPoolSize;
        tempConfig.initialPoolSize = _sourceConfig.initialPoolSize;
        tempConfig.contentionLogLevel = _sourceConfig.contentionLogLevel;
        if (_poolSize.length == 1) {
            tempConfig.initialPoolSize = Integer.parseInt(_poolSize[0]);
            tempConfig.maxPoolSize = Integer.parseInt(_poolSize[0]);
        } else {
            if (_poolSize.length <= 2) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Not valid pool parameters configuration for rulechain " + _ruleChainName + " " + Arrays.toString(_poolSize));
            }

            if (StringUtils.isNoneBlank(_poolSize[0])) {
                tempConfig.initialPoolSize = Integer.parseInt(_poolSize[0]);
            }

            if (StringUtils.isNoneBlank(_poolSize[1])) {
                tempConfig.maxPoolSize = Integer.parseInt(_poolSize[1]);
            }

            if (StringUtils.isNoneBlank(_poolSize[2])) {
                tempConfig.strictPoolSize = Boolean.parseBoolean(_poolSize[2]);
            }

            if (_poolSize.length > 3 && StringUtils.isNoneBlank(_poolSize[3])) {
                tempConfig.contentionLogLevel = getContentionLogLevel(_poolSize[3]);
            }
        }

        verify(tempConfig, _ruleChainName);
        return tempConfig;
    }

    private static void verify(PoolConfig _config) throws ConfigurationException {
        verify(_config, "");
    }

    private static void verify(PoolConfig _config, String _ruleChainName) throws ConfigurationException {
        if (_config.initialPoolSize <= 0) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "PoolSize " + _config.initialPoolSize + " configured for rulechain " + _ruleChainName + " is invalid. Please enter a positive, non zero integer as value of poolsize ");
        } else if (_config.maxPoolSize <= 0) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "PoolSize " + _config.maxPoolSize + " configured for rulechain " + _ruleChainName + " is invalid. Please enter a positive, non zero integer as value of poolsize ");
        }
    }

    public String toString() {
        return "PoolConfig [initialPoolSize=" + this.initialPoolSize + ", maxPoolSize=" + this.maxPoolSize + ", strictPoolSize=" + this.strictPoolSize + ", managed=" + this.managed + "]";
    }
}
