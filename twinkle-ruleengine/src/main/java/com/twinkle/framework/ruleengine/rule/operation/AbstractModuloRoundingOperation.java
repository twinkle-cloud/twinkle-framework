package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.*;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:08 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractModuloRoundingOperation extends AbstractBinaryOperation {
    private int modulo_ = 0;
    private String moduloAttrName_ = null;
    private boolean moduloAttrInTree_ = false;
    private int moduloAttrIdx_ = -1;
    private boolean srcIsLong_ = false;
    private boolean moduloIsLong_ = false;
    private IScalarAttribute dstInstance_ = null;

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringBuffer tempBuffer = new StringBuffer();
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 4) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Insufficient parameters for operation \"" + _operation + "\". The syntax is \"<operation> <modulo> <srcAttr> <dstAttr>\"");
        }
        tempBuffer.append(tempTokenizer.nextToken());
        tempBuffer.append(" ");
        String tempModulo = tempTokenizer.nextToken();

        Attribute tempDestAttr;
        try {
            this.modulo_ = Integer.parseInt(tempModulo);
            if (this.modulo_ == 0) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Modulo argument of operation \"" + _operation + "\" should be non zero");
            }
        } catch (NumberFormatException e) {
            this.moduloAttrName_ = tempModulo;
            try {
                this.moduloAttrIdx_ = this.primitiveAttributeSchema.getAttributeIndex(tempModulo, "");
            } catch (ConfigurationException ex) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION,"Modulo argument of operation \"" + _operation + "\" should be either an integer or an attribute name");
            }

            tempDestAttr = this.primitiveAttributeSchema.newAttributeInstance(this.moduloAttrIdx_);
            if (!(tempDestAttr instanceof IScalarAttribute)) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Modulo argument of operation \"" + _operation + "\" should be of scalar type");
            }

            this.moduloIsLong_ = tempDestAttr instanceof LongAttribute;
        }

        tempBuffer.append(tempTokenizer.nextToken());
        tempBuffer.append(" ");
        tempBuffer.append(tempTokenizer.nextToken());
        super.loadOperation(tempBuffer.toString());
        Attribute tempSrcAttr = this.primitiveAttributeSchema.newAttributeInstance(this.srcIndex);
        tempDestAttr = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        if (!(tempSrcAttr instanceof IScalarAttribute)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION,"Operation source in \"" + _operation + "\" should be any scalar attribute");
        }
        this.srcIsLong_ = tempSrcAttr instanceof LongAttribute;
        if ((this.srcIsLong_ || this.moduloIsLong_) && !(tempDestAttr instanceof LongAttribute)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "One of arguments in \"" + _operation + "\" is a LongAttribute. So operation destination should be a LongAttribute");
        } else if (!(tempDestAttr instanceof IScalarAttribute)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation destination in \"" + _operation + "\" should be any scalar attribute");
        } else {
            this.dstInstance_ = (IScalarAttribute) tempDestAttr;
        }
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply AbstractModuloRoundingOperation.applyRule()");

        Attribute tempAttr = null;
        if (this.moduloAttrIdx_ != -1) {
            tempAttr = _context.getAttribute(this.moduloAttrIdx_);
            if (tempAttr == null) {
                throw new RuleException(ExceptionCode.RULE_MANDATORY_ATTR_MISSED, "Attribute " + this.moduloAttrName_ + " not found in Context.");
            }
            if (((IScalarAttribute)tempAttr).getLong() == 0L) {
                throw new RuleException(ExceptionCode.RULE_ATTR_VALUE_UNEXPECTED, "Modulo value is zero");
            }
        }

        INumericAttribute tempSrcAttr = (INumericAttribute)_context.getAttribute(this.srcIndex);
        if (!this.srcIsLong_ && !this.moduloIsLong_) {
            int tempDestValue = this.moduloRoundingMethod(tempSrcAttr.getInt(), this.moduloAttrIdx_ != -1 ? ((IScalarAttribute)tempAttr).getInt() : this.modulo_);
            ((IIntegerAttribute)this.dstInstance_).setValue(tempDestValue);
        } else {
            long tempDestValue = this.moduloRoundingMethod(tempSrcAttr.getLong(), this.moduloAttrIdx_ != -1 ? ((IScalarAttribute)tempAttr).getLong() : (long)this.modulo_);
            ((ILongAttribute)this.dstInstance_).setValue(tempDestValue);
        }

        this.setDstAttribute(_context, this.dstInstance_);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    /**
     * Do long attribute modulo rounding.
     *
     * @param _srcValue
     * @param _modulo
     * @return
     */
    protected abstract long moduloRoundingMethod(long _srcValue, long _modulo);

    /**
     * Do int attribute modulo rounding.
     *
     * @param _srcValue
     * @param _modulo
     * @return
     */
    protected abstract int moduloRoundingMethod(int _srcValue, int _modulo);

}
