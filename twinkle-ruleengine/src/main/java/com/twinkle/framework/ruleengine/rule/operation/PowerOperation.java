package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 8:16 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class PowerOperation extends MathOperation {
    int powerOpDstAttrType = -1;

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        super.loadOperation(_operation);
        this.powerOpDstAttrType = this.primitiveAttributeSchema.newAttributeInstance(this.mathDestAttrIndex).getPrimitiveType();
    }

    @Override
    protected boolean calculate(INumericAttribute _destAttr, INumericAttribute _op1Attr, INumericAttribute _op2Attr) throws RuleException {
        double tempResult = Math.pow(_op1Attr.getDouble(), _op2Attr.getDouble());
        switch (this.powerOpDstAttrType) {
            case Attribute.INTEGER_TYPE:
                ((IntegerAttribute) _destAttr).setValue((int) tempResult);
                break;
            case Attribute.STRING_TYPE:
            default:
                throw new RuleException(ExceptionCode.RULE_APPLY_ATTR_TYPE_UNEXPECTED, "Cannot store the result in the target attribute:" + this.primitiveAttributeSchema.getAttribute(this.mathDestAttrIndex).getName() + " as it is not of the type: IntegerAttribute, FloatAttribute, LongAttribute or DoubleAttribute");
            case Attribute.LONG_TYPE:
                ((LongAttribute) _destAttr).setValue((long) tempResult);
                break;
            case Attribute.FLOAT_TYPE:
                ((FloatAttribute) _destAttr).setValue((float) tempResult);
                break;
            case Attribute.DOUBLE_TYPE:
                ((DoubleAttribute) _destAttr).setValue(tempResult);
        }

        return true;
    }

    @Override
    protected boolean validate(String _operation, String _tempOpr) throws ConfigurationException {
        log.debug("Going to apply PowerOperation.validate()");
        return this.checkNumeric();
    }
}
