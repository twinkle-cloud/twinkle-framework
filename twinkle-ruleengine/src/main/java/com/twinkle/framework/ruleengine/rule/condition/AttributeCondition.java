package com.twinkle.framework.ruleengine.rule.condition;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.INumericAttribute;
import com.twinkle.framework.core.lang.ListAttribute;
import com.twinkle.framework.core.lang.StringAttribute;
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
    private ConditionOperator operation;
    private ConditionOperator nextOperation;
    private AttributeCondition next = null;
    private Attribute[] value = null;

    public AttributeCondition() {
        log.debug("AttributeCondition.initialize().");
    }

    public AttributeCondition(String _expression) throws ConfigurationException {
        log.debug("AttributeCondition.AttributeCondition({})", _expression);
        this.loadCondition(_expression);
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        log.debug("AttributeCondition.configure().");
        String tempCondition = _conf.getString("Condition");
        this.loadCondition(tempCondition);
    }

    /**
     * Check the condition.
     *
     * @param _context
     * @param _validateFlag : Do the attr validation or not while doing the check.
     * @return
     */
    @Override
    public boolean check(NormalizedContext _context, boolean _validateFlag) {
        log.debug("Going to apply AttributeCondition.check().");
        boolean tempCheckResult = true;
        Attribute tempOp1Attr = _context.getAttribute(this.attrIndex1);

        if (this.attrIndex2 != -1) {
            this.value = new Attribute[1];
            this.value[0] = _context.getAttribute(this.attrIndex2);
        }

        if (tempOp1Attr == null | this.value[0] == null
                && this.operation != ConditionOperator.IS_NULL && this.operation != ConditionOperator.IS_NOT_NULL) {
            if (_validateFlag) {
                throw new RuntimeException("Attribute(s) for checking the condition are not set in incoming context.");
            }

            tempCheckResult = false;
        } else {
            int tempCompareResult = 0;
            if (this.operation.ordinal() <= ConditionOperator.GE.ordinal()) {
                tempCompareResult = tempOp1Attr.compareTo(this.value[0]);
            }

            int i;
            switch (this.operation) {
                case EQUALS:
                    tempCheckResult = tempCompareResult == 0;
                    break;
                case NE:
                    tempCheckResult = tempCompareResult != 0;
                    break;
                case LT:
                    tempCheckResult = tempCompareResult < 0;
                    break;
                case GT:
                    tempCheckResult = tempCompareResult > 0;
                    break;
                case LE:
                    tempCheckResult = tempCompareResult <= 0;
                    break;
                case GE:
                    tempCheckResult = tempCompareResult >= 0;
                    break;
                case STARTS:
                    tempCheckResult = ((StringAttribute) tempOp1Attr).getValue().startsWith(((StringAttribute) this.value[0]).getValue());
                    break;
                case NOT_STARTS:
                    tempCheckResult = ((StringAttribute) tempOp1Attr).getValue().startsWith(((StringAttribute) this.value[0]).getValue());
                    tempCheckResult = !tempCheckResult;
                    break;
                case ENDS:
                    tempCheckResult = ((StringAttribute) tempOp1Attr).getValue().endsWith(((StringAttribute) this.value[0]).getValue());
                    break;
                case NOT_ENDS:
                    tempCheckResult = ((StringAttribute) tempOp1Attr).getValue().endsWith(((StringAttribute) this.value[0]).getValue());
                    tempCheckResult = !tempCheckResult;
                    break;
                case CONTAINS:
                    tempCheckResult = ((StringAttribute) tempOp1Attr).getValue().indexOf(((StringAttribute) this.value[0]).getValue()) > -1;
                    break;
                case NOT_CONTAINS:
                    tempCheckResult = ((StringAttribute) tempOp1Attr).getValue().indexOf(((StringAttribute) this.value[0]).getValue()) > -1;
                    tempCheckResult = !tempCheckResult;
                    break;
                case IS_NULL:
                    tempCheckResult = tempOp1Attr == null;
                    break;
                case IS_NOT_NULL:
                    tempCheckResult = tempOp1Attr != null;
                    break;
                case CONTAINS_ALL:
                    tempCheckResult = ((ListAttribute) tempOp1Attr).containsAll(this.value[0]);
                    break;
                case NOT_CONTAINS_ALL:
                    tempCheckResult = !((ListAttribute) tempOp1Attr).containsAll(this.value[0]);
                    break;
                case WITHIN:
                    for (i = 0; i < this.value.length && !(tempCheckResult = tempOp1Attr.compareTo(this.value[i]) >= 0 & tempOp1Attr.compareTo(this.value[i + 1]) <= 0); i += 2) {
                    }
                    return (tempCheckResult && this.nextOperation == ConditionOperator.OR) | (!tempCheckResult && this.nextOperation == ConditionOperator.AND) | this.next == null ? tempCheckResult : this.next.check(_context);
                case NOT_WITHIN:
                    for (i = 0; i < this.value.length && !(tempCheckResult = tempOp1Attr.compareTo(this.value[i]) >= 0 & tempOp1Attr.compareTo(this.value[i + 1]) <= 0); i += 2) {
                    }
                    tempCheckResult = !tempCheckResult;
            }
        }

        return (tempCheckResult && this.nextOperation == ConditionOperator.OR) | (!tempCheckResult && this.nextOperation == ConditionOperator.AND) | this.next == null ? tempCheckResult : this.next.check(_context);
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
        this.nextOperation = ConditionOperator.valueOfOperator(_nextOperation);
        if (this.nextOperation == ConditionOperator.UNKNOWN) {
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
        }
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

        this.operation = ConditionOperator.valueOfOperator(tempOperationToken);
        switch (this.operation) {
            case IS:
                if (!tempOp2Token.equals(S_O_NULL)) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, _conditionLine + " for is operation value can only be null");
                }
                // Parameter2 must be null.
                this.operation = ConditionOperator.IS_NULL;
                break;
            case IS_NOT:
                if (!tempOp2Token.equals(S_O_NULL)) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, _conditionLine + " for isnot operation value can only be null");
                }
                // Parameter2 must be null.
                this.operation = ConditionOperator.IS_NOT_NULL;
                break;
            case UNKNOWN:
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_INIT_INVALID, _conditionLine + " - Unknown operation " + tempOperationToken);
            default:
                log.debug("The Operation is [{}]", this.operation);
        }

        int tempOp1PrimitiveType = this.primitiveAttributeSchema.getAttribute(tempOp1Token).getPrimitiveType();
        if (this.operation.ordinal() >= ConditionOperator.STARTS.ordinal()
                & this.operation.ordinal() <= ConditionOperator.NOT_CONTAINS.ordinal()
                & tempOp1PrimitiveType != Attribute.STRING_TYPE) {
            String tempExpMsg = _conditionLine + " - Cannot support operation (" + tempOperationToken;
            tempExpMsg = tempExpMsg + ") on numeric attribute";
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, tempExpMsg);
        }
        AttributeInfo tempOp2Attr = this.primitiveAttributeSchema.getAttribute(tempOp2Token);
        String tempStr;
        if (tempOp2Attr != null && !tempConstantFlag) {
            if (tempOp2Attr.getPrimitiveType() != tempOp1PrimitiveType) {
                tempStr = _conditionLine + " - Cannot compare (" + tempOp1Token + ") with (" + tempOp2Token;
                tempStr = tempStr + ") as they belong to different types";
                throw new ConfigurationException(ExceptionCode.RULE_CON_EXPRESS_ATTR_MISMATCH, tempStr);
            }

            if (this.operation.ordinal() >= ConditionOperator.WITHIN.ordinal()) {
                tempStr = _conditionLine + " - Cannot support operation (" + tempOperationToken;
                tempStr = tempStr + ") against another attribute";
                throw new ConfigurationException(ExceptionCode.RULE_CON_EXPRESS_ATTR_INVALID, tempStr);
            }

            this.attrIndex2 = tempOp2Attr.getIndex();
        } else if (this.operation.ordinal() >= ConditionOperator.WITHIN.ordinal()) {
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

            for (int i = 0; i < this.value.length; i += 2) {
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
        } else if (tempOp2Token.equals("null") && (this.operation == ConditionOperator.IS_NULL || this.operation == ConditionOperator.IS_NOT_NULL)) {
            this.value = new Attribute[1];
            this.value[0] = null;
        } else {
            if (this.operation == ConditionOperator.CONTAINS_ALL || this.operation == ConditionOperator.NOT_CONTAINS_ALL) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_NOT_ALLOWED, _conditionLine + ": only Primitive Attribute are allowed as operands.");
            }

            this.value = new Attribute[1];
            this.value[0] = this.primitiveAttributeSchema.newAttributeInstance(this.attrIndex1);
            if (this.value[0] instanceof INumericAttribute) {
                tempOp2Token = tempOp2Token.trim();
            }

            this.value[0].setValue(tempOp2Token);
        }

        if ((this.operation == ConditionOperator.CONTAINS_ALL || this.operation == ConditionOperator.NOT_CONTAINS_ALL)
                && (!(this.primitiveAttributeSchema.getAttribute(this.attrIndex1).newAttributeInstance() instanceof ListAttribute)
                || this.attrIndex2 == -1
                || !(this.primitiveAttributeSchema.getAttribute(this.attrIndex2).newAttributeInstance() instanceof ListAttribute))) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_NOT_ALLOWED, "Operation (" + tempOperationToken + ") is supported for ListAttribute arguments only");
        }
    }
}
