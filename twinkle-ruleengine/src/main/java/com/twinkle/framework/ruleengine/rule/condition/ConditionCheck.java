package com.twinkle.framework.ruleengine.rule.condition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.*;
import com.twinkle.framework.ruleengine.rule.support.Criteria;
import com.twinkle.framework.ruleengine.rule.support.CriteriaList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/5/19 3:18 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class ConditionCheck extends AbstractCondition {
    protected AttributeCondition conditions_ = null;
    protected String defaultOp_ = "and";

    public ConditionCheck() {
    }

    /**
     * Build a cretria check with given criteria list.
     *
     * @param _criteriaList
     * @throws ConfigurationException
     */
    public ConditionCheck(CriteriaList _criteriaList) throws ConfigurationException {
        log.debug("ConditionCheck.ConditionCheck()");
        if (_criteriaList != null) {
            Criteria[] tempCriterias = _criteriaList.getCriteriaArray();
            String[] tempCriteriaStrArray = new String[tempCriterias.length];
            String tempOperators = new String();

            for (int i = 0; i < tempCriterias.length; ++i) {
                if (i != 0) {
                    tempOperators = tempOperators + "and,";
                }

                StringBuffer tempBuffer = new StringBuffer();
                String tempOperand = tempCriterias[i].getOperand();
                String tempOperator = tempCriterias[i].getOperation();
                String tempValue = tempCriterias[i].getValue();
                tempBuffer.append(tempOperand);
                tempBuffer.append(",");
                tempBuffer.append(tempOperator);
                tempBuffer.append(",");
                if (!tempOperator.endsWith("within")) {
                    tempValue = this.convertValue(tempOperand, tempValue);
                }

                tempBuffer.append(tempValue);
                tempCriteriaStrArray[i] = tempBuffer.toString();
            }

            this.loadConditions(tempCriteriaStrArray, tempOperators);
        }
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        log.debug("ConditionCheck.configure().");
        JSONArray tempConditionArray = _conf.getJSONArray("Conditions");
        if (tempConditionArray != null) {
            String tempOperators = _conf.getString("Operations");
            if (StringUtils.isBlank(tempOperators)) {
                tempOperators = new String("");
            }
            this.defaultOp_ = _conf.getString("DefaultOp");
            if (StringUtils.isBlank(this.defaultOp_)) {
                this.defaultOp_ = new String("and");
            }
            this.loadConditions(tempConditionArray.toArray(new String[tempConditionArray.size()]), tempOperators);
        }
    }

    /**
     * Load the Conditions with given criterias and operations.
     *
     * @param _criterias
     * @param _operators
     * @throws ConfigurationException
     */
    public void loadConditions(String[] _criterias, String _operators) throws ConfigurationException {
        log.debug("ConditionCheck.loadConditions().");
        AttributeCondition tempCondition = null;
        AttributeCondition tempNextCondition = null;
        StringTokenizer tempOprTokenizer = new StringTokenizer(_operators, ",");

        for (int i = 0; i < _criterias.length; ++i) {
            tempCondition = tempNextCondition;
            tempNextCondition = new AttributeCondition(_criterias[i]);
            if (tempCondition != null) {
                tempCondition.addNextCondition(tempNextCondition);
                String tempOperator = null;
                if (tempOprTokenizer.hasMoreTokens()) {
                    tempOperator = tempOprTokenizer.nextToken();
                } else {
                    tempOperator = this.defaultOp_;
                }
                tempCondition.setNextOperation(tempOperator);
            }
            if (i == 0) {
                this.conditions_ = tempNextCondition;
            }
        }
    }

    @Override
    public boolean check(NormalizedContext _context, boolean _validateFlag) throws RuleException {
        log.debug("Going to Apply ConditionCheck.check() with verify attribute.");
        if (this.conditions_ != null) {
            boolean tempResult = this.conditions_.check(_context, _validateFlag);
            return tempResult;
        } else {
            return false;
        }
    }

    /**
     * Convert the _value to Attribute, and then return the Attribute.toString() as the final value.
     *
     * @param _attr
     * @param _value
     * @return
     * @throws ConfigurationException
     */
    private String convertValue(String _attr, String _value) throws ConfigurationException {
        AttributeInfo tempAttrInfo = this.primitiveAttributeSchema.getAttribute(_value);
        if (tempAttrInfo != null) {
            return _value;
        }
        int tempAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(_attr, _attr + "Not found");
        Attribute tempAttr = this.primitiveAttributeSchema.newAttributeInstance(tempAttrIndex);
        if (tempAttr.getPrimitiveType() == TimeAttribute.getTypeID()) {
            return _value;
        }
        switch (tempAttr.getPrimitiveType()) {
            case Attribute.INTEGER_TYPE:
                ((IIntegerAttribute) tempAttr).setValue(Integer.parseInt(_value));
                break;
            case Attribute.STRING_TYPE:
            case Attribute.BYTE_ARRAY_TYPE:
            case Attribute.UNICODE_STRING_TYPE:
                tempAttr.setValue(_value);
                break;
            case Attribute.LONG_TYPE:
                ((ILongAttribute) tempAttr).setValue(Long.parseLong(_value));
                break;
            case Attribute.FLOAT_TYPE:
                ((IFloatAttribute) tempAttr).setValue(Float.parseFloat(_value));
                break;
            case Attribute.DOUBLE_TYPE:
                ((IDoubleAttribute) tempAttr).setValue(Double.parseDouble(_value));
        }
        return tempAttr.toString();
    }
}
