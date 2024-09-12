package com.twinkle.framework.ruleengine;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.component.rule.IRuleChain;
import com.twinkle.framework.api.component.rule.IRuleChainManager;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.ruleengine.pool.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author chenxj
 */
@Slf4j
public class RuleChainManager extends AbstractConfigurableComponent implements IRuleChainManager {
    private List<String> ruleChainNameList;

    private Map<String, PoolConfig> poolConfigMap;
    private Map<String, IObjectPool<IRuleChain>> poolsMap;
    private PoolConfig commonPoolConfig;
    private ExecutorService executorService;
    private int threadPoolSize;
    boolean prototypeFlag = true;

    public RuleChainManager() {
        this.poolConfigMap = new ConcurrentHashMap<>(8);
        this.poolsMap = new ConcurrentHashMap<>(8);
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        this.commonPoolConfig = PoolConfig.fromConfig(_conf);
        this.threadPoolSize = _conf.getIntValue("ThreadPoolSize", 4);
        this.executorService = new ThreadPoolExecutor(this.threadPoolSize, this.threadPoolSize, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Thread.ofVirtual().factory());
        prototypeFlag = _conf.getBooleanValue(IComponentFactory.BEAN_SCOPE_TYPE, true);

        JSONArray tempArray = _conf.getJSONArray("RuleChainNames");
        JSONArray tempRuleChainArray = _conf.getJSONArray("RuleChains");
        if (tempArray.isEmpty() || tempRuleChainArray.isEmpty()) {
            return;
        }
        this.ruleChainNameList = new ArrayList<>(tempArray.size());
        for (int i = 0; i < tempArray.size(); i++) {
            String tempItem = tempArray.getString(i);
            for (int j = 0; j < tempRuleChainArray.size(); j++) {
                JSONObject tempObj = tempRuleChainArray.getJSONObject(j);
                if (tempObj.getString("Name").equals(tempItem)) {
                    try {
                        RuleChainObjectFactory tempObjFactory = new RuleChainObjectFactory(this.getComponentName("RuleChains"), tempObj, prototypeFlag);
                        IObjectPool<IRuleChain> tempPoolObj = this.createPool(tempItem, this.commonPoolConfig, tempObjFactory);
                        log.debug("Created pool for rulechain: {}", tempItem);

                        this.poolsMap.put(tempItem, tempPoolObj);
                        this.ruleChainNameList.add(tempItem);
                    } catch (Exception e) {
                        throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_RULECHAIN, "Failed to create a pool for RuleChain: " + tempItem, e);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public IRuleChain getRuleChain(String _ruleChainName) {
        if (MapUtils.isEmpty(this.poolsMap)) {
            return null;
        }
        this.nameCheck(_ruleChainName);
        try {
            return (this.poolsMap.get(_ruleChainName)).getObject();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to get rulechain: " + _ruleChainName, ex);
        }
    }

    @Override
    public void addRuleChain(String _ruleChainName, IRuleChain _ruleChain) {
        if (this.executorService == null) {
            this.executorService = Executors.newCachedThreadPool();
        }

        try {
            PoolConfig tempPoolConfig = this.poolConfigMap.get(_ruleChainName);
            this.poolsMap.put(_ruleChainName, this.createPool(_ruleChainName, tempPoolConfig, this.wrap(_ruleChain)));
        } catch (Exception ex) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_RULECHAIN, ex.getMessage(), ex);
        }
    }

    private ObjectFactory<IRuleChain> wrap(final IRuleChain _ruleChain) {
        return new ObjectFactory<IRuleChain>() {
            public IRuleChain create() throws Exception {
                return _ruleChain;
            }
        };
    }

    @Override
    public Collection<String> getRuleChainNames() {
        return this.poolsMap.keySet();
    }

    private void nameCheck(String _ruleChainName) {
        if (!this.getRuleChainNames().contains(_ruleChainName)) {
            throw new IllegalArgumentException("RuleChain " + _ruleChainName + " is not configured.");
        }
    }

    @Override
    public void releaseRuleChain(String _name, IRuleChain _ruleChain) {
        this.nameCheck(_name);
        IObjectPool<IRuleChain> tempObjPool = this.poolsMap.get(_name);
        tempObjPool.releaseObject(_ruleChain);
    }

    private IObjectPool<IRuleChain> createPool(String _ruleChainName, PoolConfig _config, ObjectFactory<IRuleChain> _objFactory) throws Exception {
        if (_config == null) {
            return ObjectPoolFactory.newObjectPool(_objFactory, 1, 1, true, null, null, this.prototypeFlag);
        } else {
            RuleChainPoolContentionListener tempListener = new RuleChainPoolContentionListener(_ruleChainName, _config.getMaxPoolSize(), _config.getContentionLogLevel());
            return ObjectPoolFactory.newObjectPool(_objFactory, _config.getInitialPoolSize(), _config.getMaxPoolSize(), _config.isStrictPoolSize(), tempListener, this.executorService, this.prototypeFlag);
        }
    }
}
