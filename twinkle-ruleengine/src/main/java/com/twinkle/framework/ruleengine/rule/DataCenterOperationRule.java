package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.datacenter.IDataCenterManager;
import com.twinkle.framework.api.component.datacenter.IStatementExecutor;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/19/19 6:10 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DataCenterOperationRule extends AbstractRule {
    /**
     * Get the data center manager.
     */
    @Autowired
    private IDataCenterManager dataCenterManager;
    private String executorName;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.executorName = _conf.getString("ExecutorName");
        if (StringUtils.isBlank(this.executorName)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The ExecutorName is mandatory for DataCenterOperationRule.");
        }

    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply DataCenterOperationRule.applyRule().");
        IStatementExecutor tempExecutor = this.dataCenterManager.getStatementExecutor(this.executorName);
        tempExecutor.execute(_context);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}
