package com.twinkle.framework.ruleengine.rule.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/5/19 3:20 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Criteria {
    /**
     * The operand of this Criteria.
     * Usually to be Attribute.
     */
    private String operand;
    /**
     * The operator.
     */
    private String operation;
    /**
     * Criteria Value.
     */
    private String value;
}
