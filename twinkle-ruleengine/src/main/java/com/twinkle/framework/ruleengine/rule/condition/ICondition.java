package com.twinkle.framework.ruleengine.rule.condition;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.context.NormalizedContext;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 10:07<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ICondition extends Configurable {
    /**
     * Condition Operation Name in the expression line in the configuration file.
     */
    String S_O_AND = "and";
    String S_O_OR = "or";
    String S_O_EQUALS = "=";
    String S_O_NOT_EQUALS = "!=";
    String S_O_LESS_THAN = "<";
    String S_O_GREATER_THAN = ">";
    String S_O_LESS_EQUALS = "<=";
    String S_O_GREATER_EQUALS = ">=";
    String S_O_WITHIN = "within";
    String S_O_NOT_WITHIN = "!within";
    String S_O_STARTS = "starts";
    String S_O_NOT_STARTS = "!starts";
    String S_O_ENDS = "ends";
    String S_O_NOT_ENDS = "!ends";
    String S_O_CONTAINS = "contains";
    String S_O_NOT_CONTAINS = "!contains";
    String S_O_IS = "is";
    String S_O_ISNOT = "isnot";
    String S_O_NULL = "null";
    String S_O_CONTAINS_ALL = "containsAll";
    String S_O_NOT_CONTAINS_ALL = "!containsAll";
    /**
     * The Mapped Operation Type.
     */
    int OP_AND = 0;
    int OP_OR = 1;
    int OP_EQUALS = 2;
    int OP_NOT_EQUALS = 3;
    int OP_LESS_THAN = 4;
    int OP_GREATER_THAN = 5;
    int OP_LESS_EQUALS = 6;
    int OP_GREATER_EQUALS = 7;
    int OP_STARTS = 100;
    int OP_NOT_STARTS = 101;
    int OP_ENDS = 102;
    int OP_NOT_ENDS = 103;
    int OP_CONTAINS = 104;
    int OP_NOT_CONTAINS = 105;
    int OP_IS_NULL = 106;
    int OP_IS_NOT_NULL = 107;
    int OP_CONTAINS_ALL = 108;
    int OP_NOT_CONTAINS_ALL = 109;
    int OP_WITHIN = 200;
    int OP_NOT_WITHIN = 201;

    /**
     * Do the check.
     *
     * @param _context
     * @return
     * @throws RuleException
     */
    boolean check(NormalizedContext _context) throws RuleException;
}
