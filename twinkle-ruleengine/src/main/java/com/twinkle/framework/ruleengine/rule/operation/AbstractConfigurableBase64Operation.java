package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.ruleengine.rule.support.OperationAttributeInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 7:02 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractConfigurableBase64Operation extends AbstractConfigurableConfigurableOperation {
    protected OperationAttributeInfo sourceAttrInfo;
    protected OperationAttributeInfo targetAttrInfo;

    /**
     * Load Operation.
     *
     * @param _operation
     * @param _oprName
     * @throws ConfigurationException
     */
    protected void loadOperation(String _operation, String _oprName) throws ConfigurationException {
        log.debug("Base64Operation.loadOperation {}", _operation);
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() != 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, String.format("In Base64Operation.loadOperation(): operation incorrect number of fields (%s)", _operation));
        }
        String tempOprName = tempTokenizer.nextToken();
        if (!_oprName.equals(tempOprName)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, String.format("In Base64Operation.loadOperation(): only %s operation supported, not (%s)", _oprName, tempOprName));
        }
        this.sourceAttrInfo = new OperationAttributeInfo(tempTokenizer.nextToken());
        this.targetAttrInfo = new OperationAttributeInfo(tempTokenizer.nextToken());
    }
}
