package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.INumericAttribute;
import com.twinkle.framework.core.lang.IScalarAttribute;
import com.twinkle.framework.ruleengine.utils.TreeMarker;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 17:42<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class MathOperation extends AbstractConfigurableAttributeOperation {
    private MathOperator mathOperation;
    protected int mathDestAttrIndex;
    private boolean isDstTree_ = false;
    private int mathOp1AttrIndex;
    private int mathOp2AttrIndex;
    private boolean isSrc1Tree_ = false;
    private boolean isSrc2Tree_ = false;
    private Attribute mathConstOp1;
    private Attribute mathConstOp2;
    private boolean nonNumeric;

    public MathOperation() {
        log.debug("MathOperation.initialize().");
        this.mathOperation = MathOperator.UNKNOWN;
        this.mathDestAttrIndex = -1;
        this.mathOp1AttrIndex = -1;
        this.mathOp2AttrIndex = -1;
        this.mathConstOp1 = null;
        this.mathConstOp2 = null;
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempST = new StringTokenizer(_operation);
        if (tempST.countTokens() < 4) {
            throw new ConfigurationException(ExceptionCode.RULE_ADN_MATH_OPERATION_INVALID, "In MathOperation.loadOperation(): operation missing fields (" + _operation + ")");
        } else {
            String tempOperation = tempST.nextToken();
            String tempFirstParam = tempST.nextToken();
            String tempSecondParam = tempST.nextToken();
            String tempResultParam = tempST.nextToken();
            if (TreeMarker.isTreeAttribute(tempResultParam)) {
                this.isDstTree_ = true;
                tempResultParam = TreeMarker.extractAttributeName(tempResultParam);
            }

            int tempDestAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(tempResultParam, _operation);
            if (tempDestAttrIndex == -1) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_MISSED_IN_SCHEMA, tempResultParam + " in MathOperation " + _operation + " should be defined in Context Shema.");
            } else {
                AttributeInfo tempAttrInfo = this.primitiveAttributeSchema.getAttribute(tempFirstParam);
                if (tempAttrInfo == null && TreeMarker.isTreeAttribute(tempFirstParam)) {
                    tempFirstParam = TreeMarker.extractAttributeName(tempFirstParam);
                    tempAttrInfo = this.primitiveAttributeSchema.getAttribute(tempFirstParam);
                    this.isSrc1Tree_ = true;
                }

                int tempOp1AttrIndex;
                if (tempAttrInfo != null) {
                    tempOp1AttrIndex = tempAttrInfo.getIndex();
                } else {
                    tempOp1AttrIndex = -1;
                    this.mathConstOp1 = this.primitiveAttributeSchema.newAttributeInstance(tempDestAttrIndex);

                    try {
                        this.mathConstOp1.setValue(tempFirstParam);
                    } catch (NumberFormatException e) {
                        throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_MISSED_IN_SCHEMA, tempFirstParam + " in MathOperation " + _operation + " should be either defined in NMESchema or a Constant Value of required Type");
                    }
                }

                tempAttrInfo = this.primitiveAttributeSchema.getAttribute(tempSecondParam);
                if (tempAttrInfo == null && TreeMarker.isTreeAttribute(tempSecondParam)) {
                    tempSecondParam = TreeMarker.extractAttributeName(tempSecondParam);
                    tempAttrInfo = this.primitiveAttributeSchema.getAttribute(tempSecondParam);
                    this.isSrc2Tree_ = true;
                }

                int tempOp2AttrIndex;
                if (tempAttrInfo != null) {
                    tempOp2AttrIndex = tempAttrInfo.getIndex();
                } else {
                    tempOp2AttrIndex = -1;
                    this.mathConstOp2 = this.primitiveAttributeSchema.newAttributeInstance(tempDestAttrIndex);

                    try {
                        this.mathConstOp2.setValue(tempSecondParam);
                    } catch (NumberFormatException e) {
                        throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_MISSED_IN_SCHEMA, tempSecondParam + " in MathOperation " + _operation + " should be either defined in NMESchema or a Constant Value of required Type");
                    }
                }

                this.mathOp1AttrIndex = tempOp1AttrIndex;
                this.mathOp2AttrIndex = tempOp2AttrIndex;
                this.mathDestAttrIndex = tempDestAttrIndex;
                boolean tempValidateFlag = this.validate(_operation, tempOperation);
                if (!tempValidateFlag) {
                    throw new ConfigurationException(ExceptionCode.RULE_ADN_MATH_OPERATION_INVALID, "Operation unsupported: One or more attributes do not support this operation - " + _operation);
                }
            }
        }
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply MathOperation.applyRule()");

        if (this.mathOperation == MathOperator.ADD && this.nonNumeric) {
            this.concatenate(_context);
            if (this.nextRule != null) {
                this.nextRule.applyRule(_context);
            }
        } else {
            INumericAttribute tempDestAttr = (INumericAttribute) _context.getAttribute(this.mathDestAttrIndex);
            INumericAttribute tempOp1Attr;
            if (this.mathOp1AttrIndex != -1) {
                tempOp1Attr = (INumericAttribute) _context.getAttribute(this.mathOp1AttrIndex);
                if (tempOp1Attr == null) {
                    throw new RuleException(ExceptionCode.LOGIC_CONF_ATTR_NOT_INIT, "NC attribute " + this.primitiveAttributeSchema.getAttribute(this.mathOp1AttrIndex).getName() + " not set");
                }
            } else {
                tempOp1Attr = (INumericAttribute) this.mathConstOp1;
            }

            INumericAttribute tempOp2Attr;
            if (this.mathOp2AttrIndex != -1) {
                tempOp2Attr = (INumericAttribute) _context.getAttribute(this.mathOp2AttrIndex);
                if (tempOp2Attr == null) {
                    throw new RuleException(ExceptionCode.LOGIC_CONF_ATTR_NOT_INIT, "NC attribute " + this.primitiveAttributeSchema.getAttribute(this.mathOp2AttrIndex).getName() + " not set");
                }
            } else {
                tempOp2Attr = (INumericAttribute) this.mathConstOp2;
            }

            if (tempDestAttr == null) {
                if (!_context.getType().isMember(this.mathDestAttrIndex)) {
                    AttributeInfo tempDestAttrInfo = this.primitiveAttributeSchema.getAttribute(this.mathDestAttrIndex);
                    _context.getType().addAttribute(tempDestAttrInfo);
                }

                tempDestAttr = (INumericAttribute) this.primitiveAttributeSchema.newAttributeInstance(this.mathDestAttrIndex);
                _context.setAttribute(tempDestAttr, this.mathDestAttrIndex);
            }

            boolean tempCalResultFlag = this.calculate(tempDestAttr, tempOp1Attr, tempOp2Attr);
            if (!tempCalResultFlag) {
                throw new RuleException(ExceptionCode.RULE_ADN_MATH_OPERATION_INVALID, "Operation is not supported by the target attribute - " + this.mathOperation);
            } else {
                if (this.nextRule != null) {
                    this.nextRule.applyRule(_context);
                }

            }
        }
    }

    /**
     * _operation is the entire operation express.
     *
     * @param _operation
     * @param _tempOpr
     * @return
     * @throws ConfigurationException
     */
    protected boolean validate(String _operation, String _tempOpr) throws ConfigurationException {
        boolean tempResult = true;
        this.mathOperation = MathOperator.valueOfOperator(_tempOpr);
        switch (this.mathOperation) {
            case ADD:
                this.nonNumeric = !this.checkNumeric();
                break;
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
            case MOD:
                tempResult = this.checkNumeric();
                break;
            case AND:
            case OR:
            case XOR:
            case SHIFTL:
            case SHIFTR:
                tempResult = this.checkScalar();
                break;
            default:
                throw new ConfigurationException(ExceptionCode.RULE_ADN_MATH_OPERATION_INVALID, "In MathOperation.loadOperations():  operator '+', '-', '*', '/', '&', '|', '%', '<<', or '>>' (" + _operation + ")");

        }
        return tempResult;
    }

    protected boolean checkNumeric() {
        return (this.mathOp1AttrIndex == -1 || this.primitiveAttributeSchema.newAttributeInstance(this.mathOp1AttrIndex) instanceof INumericAttribute) && (this.mathOp2AttrIndex == -1 || this.primitiveAttributeSchema.newAttributeInstance(this.mathOp2AttrIndex) instanceof INumericAttribute) && (this.mathOp1AttrIndex != -1 || this.mathConstOp1 instanceof INumericAttribute) && (this.mathOp2AttrIndex != -1 || this.mathConstOp2 instanceof INumericAttribute) && this.primitiveAttributeSchema.newAttributeInstance(this.mathDestAttrIndex) instanceof INumericAttribute;
    }

    private boolean checkScalar() {
        return (this.mathOp1AttrIndex == -1 || this.primitiveAttributeSchema.newAttributeInstance(this.mathOp1AttrIndex) instanceof IScalarAttribute) && (this.mathOp2AttrIndex == -1 || this.primitiveAttributeSchema.newAttributeInstance(this.mathOp2AttrIndex) instanceof IScalarAttribute) && (this.mathOp1AttrIndex != -1 || this.mathConstOp1 instanceof INumericAttribute) && (this.mathOp2AttrIndex != -1 || this.mathConstOp2 instanceof INumericAttribute) && this.primitiveAttributeSchema.newAttributeInstance(this.mathDestAttrIndex) instanceof IScalarAttribute;
    }

    protected boolean calculate(INumericAttribute _destAttr, INumericAttribute _op1Attr, INumericAttribute _op2Attr) throws RuleException {
        boolean tempOprResultFlag = true;
        switch (this.mathOperation) {
            case ADD:
                tempOprResultFlag = _destAttr.add(_op1Attr, _op2Attr);
                break;
            case SUBTRACT:
                tempOprResultFlag = _destAttr.subtract(_op1Attr, _op2Attr);
                break;
            case MULTIPLY:
                tempOprResultFlag = _destAttr.multiply(_op1Attr, _op2Attr);
                break;
            case DIVIDE:
                tempOprResultFlag = _destAttr.divide(_op1Attr, _op2Attr);
                break;
            case MOD:
                tempOprResultFlag = _destAttr.mod(_op1Attr, _op2Attr);
                break;
            case AND:
                tempOprResultFlag = ((IScalarAttribute) _destAttr).and((IScalarAttribute) _op1Attr, (IScalarAttribute) _op2Attr);
                break;
            case OR:
                tempOprResultFlag = ((IScalarAttribute) _destAttr).or((IScalarAttribute) _op1Attr, (IScalarAttribute) _op2Attr);
                break;
            case XOR:
                tempOprResultFlag = ((IScalarAttribute) _destAttr).xor((IScalarAttribute) _op1Attr, (IScalarAttribute) _op2Attr);
                break;
            case SHIFTL:
                tempOprResultFlag = ((IScalarAttribute) _destAttr).shiftl((IScalarAttribute) _op1Attr, (IScalarAttribute) _op2Attr);
                break;
            case SHIFTR:
                tempOprResultFlag = ((IScalarAttribute) _destAttr).shiftr((IScalarAttribute) _op1Attr, (IScalarAttribute) _op2Attr);
                break;
            default:
                throw new RuleException(ExceptionCode.RULE_ADN_MATH_OPERATION_INVALID, "Invalid math operation - " + this.mathOperation);
        }

        return tempOprResultFlag;
    }

    protected void concatenate(NormalizedContext _context) {
        Attribute tempDstAttr = _context.getAttribute(this.mathDestAttrIndex);
        String tempAttrOp1Str = null;
        String tempAttrOp2Str = null;
        if (this.mathOp1AttrIndex != -1) {
            tempAttrOp1Str = _context.getAttribute(this.mathOp1AttrIndex).toString();
        } else {
            tempAttrOp1Str = this.mathConstOp1.toString();
        }

        if (this.mathOp2AttrIndex != -1) {
            tempAttrOp2Str = _context.getAttribute(this.mathOp2AttrIndex).toString();
        } else {
            tempAttrOp2Str = this.mathConstOp2.toString();
        }

        if (tempDstAttr == null) {
            tempDstAttr = this.primitiveAttributeSchema.newAttributeInstance(this.mathDestAttrIndex);
            _context.setAttribute(tempDstAttr, this.mathDestAttrIndex);
        }

        tempDstAttr.setValue(tempAttrOp1Str + tempAttrOp2Str);
    }
}
