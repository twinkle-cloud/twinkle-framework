package com.twinkle.framework.ruleengine.rule.condition;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.core.lang.*;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 11:41<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class AttributeCondition extends AbstractCondition {
    private int attrIndex1 = -1;
    private int attrIndex2 = -1;
    private int operation = 0;
    private int nextOperation = 0;
    private AttributeCondition next = null;
    private Attribute[] value = null;

    public AttributeCondition() {
        log.debug("AttributeCondition.initialize().");
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        log.debug("AttributeCondition.configure().");
        String tempCondition = _conf.getString("Condition");
        this.loadCondition(tempCondition);
    }
    @Override
    public boolean check(NormalizedContext _context) {
        return this.check(_context, false);
    }

    /**
     * Check the condition.
     *
     * @param _context
     * @param _validateFlag : Do the attr validation or not while doing the check.
     * @return
     */
    public boolean check(NormalizedContext _context, boolean _validateFlag) {
        log.debug("AttributeCondition.check(nme, validate_attrs)");
        boolean tempCheckResult = true;
        Attribute tempOp1Attr = _context.getAttribute(this.attrIndex1);

        if (this.attrIndex2 != -1) {
            this.value = new Attribute[1];
            this.value[0] = _context.getAttribute(this.attrIndex2);
        }

        if (tempOp1Attr == null | this.value[0] == null
                && this.operation != OP_IS_NULL && this.operation != OP_IS_NOT_NULL) {
            if (_validateFlag) {
                throw new RuntimeException("NC attribute(s) for checking the condition are not set in incoming NME.");
            }

            tempCheckResult = false;
        } else {
            int tempCompareResult = 0;
            if (this.operation <= OP_GREATER_EQUALS) {
                tempCompareResult = tempOp1Attr.compareTo(this.value[0]);
            }

            int i;
            switch(this.operation) {
                case OP_EQUALS:
                    tempCheckResult = tempCompareResult == 0;
                    break;
                case OP_NOT_EQUALS:
                    tempCheckResult = tempCompareResult != 0;
                    break;
                case OP_LESS_THAN:
                    tempCheckResult = tempCompareResult < 0;
                    break;
                case OP_GREATER_THAN:
                    tempCheckResult = tempCompareResult > 0;
                    break;
                case OP_LESS_EQUALS:
                    tempCheckResult = tempCompareResult <= 0;
                    break;
                case OP_GREATER_EQUALS:
                    tempCheckResult = tempCompareResult >= 0;
                    break;
                case OP_STARTS:
                    tempCheckResult = ((StringAttribute)tempOp1Attr).getValue().startsWith(((StringAttribute)this.value[0]).getValue());
                    break;
                case OP_NOT_STARTS:
                    tempCheckResult = ((StringAttribute)tempOp1Attr).getValue().startsWith(((StringAttribute)this.value[0]).getValue());
                    tempCheckResult = !tempCheckResult;
                    break;
                case OP_ENDS:
                    tempCheckResult = ((StringAttribute)tempOp1Attr).getValue().endsWith(((StringAttribute)this.value[0]).getValue());
                    break;
                case OP_NOT_ENDS:
                    tempCheckResult = ((StringAttribute)tempOp1Attr).getValue().endsWith(((StringAttribute)this.value[0]).getValue());
                    tempCheckResult = !tempCheckResult;
                    break;
                case OP_CONTAINS:
                    tempCheckResult = ((StringAttribute)tempOp1Attr).getValue().indexOf(((StringAttribute)this.value[0]).getValue()) > -1;
                    break;
                case OP_NOT_CONTAINS:
                    tempCheckResult = ((StringAttribute)tempOp1Attr).getValue().indexOf(((StringAttribute)this.value[0]).getValue()) > -1;
                    tempCheckResult = !tempCheckResult;
                    break;
                case OP_IS_NULL:
                    tempCheckResult = tempOp1Attr == null;
                    break;
                case OP_IS_NOT_NULL:
                    tempCheckResult = tempOp1Attr != null;
                    break;
                case OP_CONTAINS_ALL:
                    tempCheckResult = ((ListAttribute)tempOp1Attr).containsAll(this.value[0]);
                    break;
                case OP_NOT_CONTAINS_ALL:
                    tempCheckResult = !((ListAttribute)tempOp1Attr).containsAll(this.value[0]);
                    break;
                case OP_WITHIN:
                    for(i = 0; i < this.value.length && !(tempCheckResult = tempOp1Attr.compareTo(this.value[i]) >= 0 & tempOp1Attr.compareTo(this.value[i + 1]) <= 0); i += 2) {
                    }

                    return (tempCheckResult && this.nextOperation == 1) | (!tempCheckResult && this.nextOperation == 0) | this.next == null ? tempCheckResult : this.next.check(_context);
                case OP_NOT_WITHIN:
                    for(i = 0; i < this.value.length && !(tempCheckResult = tempOp1Attr.compareTo(this.value[i]) >= 0 & tempOp1Attr.compareTo(this.value[i + 1]) <= 0); i += 2) {
                    }

                    tempCheckResult = !tempCheckResult;
            }
        }

        return (tempCheckResult && this.nextOperation == 1) | (!tempCheckResult && this.nextOperation == 0) | this.next == null ? tempCheckResult : this.next.check(_context);
    }

    /**
     * Just like the rule, we can append next condition following the current condition.
     *
     * @param _next
     */
    public void addNextCondition(AttributeCondition _next) {
        this.next = _next;
    }

    /**
     * Next Operation. Do AND or OR with the current condition.
     *
     * @param _nextOperation
     */
    public void setNextOperation(String _nextOperation) {
        _nextOperation = _nextOperation.trim();
        if (_nextOperation.equalsIgnoreCase(S_O_AND)) {
            this.nextOperation = OP_AND;
        } else if (_nextOperation.equalsIgnoreCase(S_O_OR)) {
            this.nextOperation = OP_OR;
        } else {
            log.warn("AttributeConditionMsg1", _nextOperation);
        }

    }

    /**
     * The Condition line parser.
     * The format is:
     * Content,contains,"Abc"
     * also can be region value, such as:
     * SrcIP,within,[192.168.0.1-192.168.0.128,192.168.0.200,192.168.0.242]
     *
     * @param _conditionLine
     * @throws ConfigurationException
     */
    private void loadCondition(String _conditionLine) throws ConfigurationException {
        StringTokenizer tempST = new StringTokenizer(_conditionLine, ",");
        boolean tempConstantFlag = false;
        if (tempST.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, _conditionLine + " - Malformed Condition should of the form <AttributeName>,<Operation>,<Value>");
        } else {
            String tempOp1Token = tempST.nextToken();
            String tempOperationToken = tempST.nextToken();
            String tempOp2Token = tempST.nextToken("\"");
            //Get the constant value if .
            if (tempST.hasMoreTokens()) {
                tempOp2Token = tempST.nextToken();
                tempConstantFlag = true;
            } else {
                tempOp2Token = tempOp2Token.substring(1);
            }

            tempOp1Token = tempOp1Token.trim();
            tempOperationToken = tempOperationToken.trim();

            this.attrIndex1 = this.primitiveAttributeSchema.getAttributeIndex(tempOp1Token, _conditionLine);
            //Get the Operation Type by operation name.
            if (tempOperationToken.equals(S_O_EQUALS)) {
                this.operation = OP_EQUALS;
            } else if (tempOperationToken.equals(S_O_NOT_EQUALS)) {
                this.operation = OP_NOT_EQUALS;
            } else if (tempOperationToken.equals(S_O_LESS_THAN)) {
                this.operation = OP_LESS_THAN;
            } else if (tempOperationToken.equals(S_O_GREATER_THAN)) {
                this.operation = OP_GREATER_THAN;
            } else if (tempOperationToken.equals(S_O_LESS_EQUALS)) {
                this.operation = OP_LESS_EQUALS;
            } else if (tempOperationToken.equals(S_O_GREATER_EQUALS)) {
                this.operation = OP_GREATER_EQUALS;
            } else if (tempOperationToken.equals(S_O_WITHIN)) {
                this.operation = OP_WITHIN;
            } else if (tempOperationToken.equals(S_O_NOT_WITHIN)) {
                this.operation = OP_NOT_WITHIN;
            } else if (tempOperationToken.equals(S_O_STARTS)) {
                this.operation = OP_STARTS;
            } else if (tempOperationToken.equals(S_O_NOT_STARTS)) {
                this.operation = OP_NOT_STARTS;
            } else if (tempOperationToken.equals(S_O_ENDS)) {
                this.operation = OP_ENDS;
            } else if (tempOperationToken.equals(S_O_NOT_ENDS)) {
                this.operation = OP_NOT_ENDS;
            } else if (tempOperationToken.equals(S_O_CONTAINS)) {
                this.operation = OP_CONTAINS;
            } else if (tempOperationToken.equals(S_O_NOT_CONTAINS)) {
                this.operation = OP_NOT_CONTAINS;
            } else if (tempOperationToken.equals(S_O_IS)) {
                if (!tempOp2Token.equals(S_O_NULL)) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, _conditionLine + " for is operation value can only be null");
                }
                // Parameter2 must be null.
                this.operation = OP_IS_NULL;
            } else if (tempOperationToken.equals(S_O_ISNOT)) {
                if (!tempOp2Token.equals(S_O_NULL)) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, _conditionLine + " for isnot operation value can only be null");
                }
                // Parameter2 must be null.
                this.operation = OP_IS_NOT_NULL;
            } else if (tempOperationToken.equals(S_O_CONTAINS_ALL)) {
                this.operation = OP_CONTAINS_ALL;
            } else {
                if (!tempOperationToken.equals(S_O_NOT_CONTAINS_ALL)) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, _conditionLine + " - Unknown operation " + tempOperationToken);
                }
                this.operation = OP_NOT_CONTAINS_ALL;
            }

            int tempOp1PrimitiveType = this.primitiveAttributeSchema.getAttribute(tempOp1Token).getPrimitiveType();
            if (this.operation >= OP_STARTS & this.operation <= OP_NOT_CONTAINS & tempOp1PrimitiveType != Attribute.STRING_TYPE) {
                String tempExpMsg = _conditionLine + " - Cannot support operation (" + tempOperationToken;
                tempExpMsg = tempExpMsg + ") on numeric attribute";
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, tempExpMsg);
            } else {
                AttributeInfo tempOp2Attr = this.primitiveAttributeSchema.getAttribute(tempOp2Token);
                String tempStr;
                if (tempOp2Attr != null && !tempConstantFlag) {
                    if (tempOp2Attr.getPrimitiveType() != tempOp1PrimitiveType) {
                        tempStr = _conditionLine + " - Cannot compare (" + tempOp1Token + ") with (" + tempOp2Token;
                        tempStr = tempStr + ") as they belong to different types";
                        throw new ConfigurationException(ExceptionCode.RULE_CON_EXPRESS_ATTR_MISMATCH, tempStr);
                    }

                    if (this.operation >= OP_WITHIN) {
                        tempStr = _conditionLine + " - Cannot support operation (" + tempOperationToken;
                        tempStr = tempStr + ") against another attribute";
                        throw new ConfigurationException(ExceptionCode.RULE_CON_EXPRESS_ATTR_INVALID, tempStr);
                    }

                    this.attrIndex2 = tempOp2Attr.getIndex();
                } else if (this.operation >= OP_WITHIN) {
                    //for Set or Region operation,  the Op2 should be included in[],
                    //such as: [a,b,c], [1-100]
                    //Get "["'s index in the expression.
                    int tempLeftIndex = _conditionLine.indexOf(91);
                    //Get "]"'s index in the expression.
                    int tempRightIndex = _conditionLine.indexOf(93);
                    if (tempLeftIndex < 0 | tempRightIndex < 0) {
                        tempStr = _conditionLine + " - for operation (" + tempOperationToken;
                        tempStr = tempStr + ") value should be within [ ]";
                        throw new ConfigurationException(ExceptionCode.RULE_CON_EXPRESS_ATTR_INVALID, tempStr);
                    }

                    StringTokenizer tempSetST = new StringTokenizer(_conditionLine.substring(tempLeftIndex + 1, tempRightIndex), ",");
                    int tempSetItemCount = tempSetST.countTokens();
                    this.value = new Attribute[tempSetItemCount * 2];

                    for(int i = 0; i < this.value.length; i += 2) {
                        String tempTokenStr = tempSetST.nextToken();
                        //Get "-"'s index.
                        int tempRegionOprIndex = tempTokenStr.indexOf(45);
                        String tempMinValue = null;
                        String tempMaxValue = null;
                        // if the value is not region value, then
                        // keep the same value for min-max.
                        if (tempRegionOprIndex < 0) {
                            tempMinValue = tempTokenStr;
                            tempMaxValue = tempTokenStr;
                        } else {
                            tempMinValue = tempTokenStr.substring(0, tempRegionOprIndex);
                            tempMaxValue = tempTokenStr.substring(tempRegionOprIndex + 1);
                        }

                        tempMinValue = tempMinValue.trim();
                        tempMaxValue = tempMaxValue.trim();
                        this.value[i] = this.primitiveAttributeSchema.newAttributeInstance(this.attrIndex1);
                        this.value[i].setValue(tempMinValue);
                        this.value[i + 1] = this.primitiveAttributeSchema.newAttributeInstance(this.attrIndex1);
                        this.value[i + 1].setValue(tempMaxValue);
                    }
                } else if (tempOp2Token.equals("null") && (this.operation == OP_IS_NULL || this.operation == OP_IS_NOT_NULL)) {
                    this.value = new Attribute[1];
                    this.value[0] = null;
                } else {
                    if (this.operation == OP_CONTAINS_ALL || this.operation == OP_NOT_CONTAINS_ALL) {
                        throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_NOT_ALLOWED, _conditionLine + ": only NC attributes are allowed as operands");
                    }

                    this.value = new Attribute[1];
                    this.value[0] = this.primitiveAttributeSchema.newAttributeInstance(this.attrIndex1);
                    if (this.value[0] instanceof INumericAttribute) {
                        tempOp2Token = tempOp2Token.trim();
                    }

                    this.value[0].setValue(tempOp2Token);
                }

                if ((this.operation == OP_CONTAINS_ALL || this.operation == OP_NOT_CONTAINS_ALL)
                        && (!(this.primitiveAttributeSchema.getAttribute(this.attrIndex1).newAttributeInstance() instanceof ListAttribute)
                        || this.attrIndex2 == -1
                        || !(this.primitiveAttributeSchema.getAttribute(this.attrIndex2).newAttributeInstance() instanceof ListAttribute))) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_NOT_ALLOWED, "Operation (" + tempOperationToken + ") is supported for ListAttribute arguments only");
                }
            }
        }
    }
}
