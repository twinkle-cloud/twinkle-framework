package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.*;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 5:25 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractRoundingOperation extends AbstractBinaryOperation {
    private int precision = 0;
    private int precisionAttrIndex = -1;
    private double defaultPrecision = 1.0D;
    private boolean destIsDouble = false;
    private String precisionAttrName = null;
    private IFloatAttribute destInstance = null;

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringBuffer tempBuffer = new StringBuffer();
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 4) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Insufficient parameters for operation \"" + _operation + "\". The syntax is \"<operation> <precision> <srcAttr> <dstAttr>\"");
        }
        tempBuffer.append(tempTokenizer.nextToken());
        tempBuffer.append(" ");
        String tempPrecisionToken = tempTokenizer.nextToken();

        try {
            this.precision = Integer.parseInt(tempPrecisionToken);
        } catch (NumberFormatException e) {
            this.precisionAttrName = tempPrecisionToken;
            try {
                this.precisionAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(tempPrecisionToken, "");
            } catch (ConfigurationException ex) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Precision argument of operation \"" + _operation + "\" should be either an integer or an attribute name.");
            }
        }

        tempBuffer.append(tempTokenizer.nextToken());
        tempBuffer.append(" ");
        tempBuffer.append(tempTokenizer.nextToken());
        super.loadOperation(tempBuffer.toString());
        if (!(this.primitiveAttributeSchema.newAttributeInstance(this.srcIndex) instanceof INumericAttribute)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation source in \"" + _operation + "\" should be any numeric attribute.");
        }
        Attribute tempAttr = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        if (tempAttr == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, "Unable to create " + this.primitiveAttributeSchema.getAttribute(this.destIndex).getName() + " attribute instance.");
        } else if (!(tempAttr instanceof IFloatAttribute)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation destination in \"" + _operation + "\" should be of type FloatAttribute or DoubleAttribute");
        }
        this.destInstance = (IFloatAttribute) tempAttr;
        this.destIsDouble = tempAttr instanceof IDoubleAttribute;
        if (this.precisionAttrIndex != -1) {
            if (!(this.primitiveAttributeSchema.newAttributeInstance(this.precisionAttrIndex) instanceof IScalarAttribute)) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, "Precision argument of operation \"" + _operation + "\" should be of scalar type");
            }
        } else {
            this.defaultPrecision = Math.pow(10.0D, this.precision);
        }
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        INumericAttribute tempSrcAttr = (INumericAttribute)_context.getAttribute(this.srcIndex);
        if (this.precisionAttrIndex != -1) {
            Attribute tempPrecisionAttr = _context.getAttribute(this.precisionAttrIndex);
            if (tempPrecisionAttr == null) {
                throw new RuleException(ExceptionCode.RULE_MANDATORY_ATTR_MISSED, "Attribute " + this.precisionAttrName + " not found in Context.");
            }
            this.precision = ((IScalarAttribute)tempPrecisionAttr).getInt();
            this.defaultPrecision = Math.pow(10.0D, this.precision);
        }

        double tempResultValue = this.roundingMethod(tempSrcAttr.getDouble(), this.defaultPrecision);
        if (this.destIsDouble) {
            ((IDoubleAttribute)this.destInstance).setValue(tempResultValue);
        } else {
            this.destInstance.setValue((float)tempResultValue);
        }

        this.setDstAttribute(_context, this.destInstance);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    /**
     * Do round precision.
     *
     * @param _srcValue
     * @param _precision
     * @return
     */
    protected abstract double roundingMethod(double _srcValue, double _precision);
}
