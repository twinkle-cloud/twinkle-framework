package com.twinkle.framework.core.asm.designer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.objectweb.asm.Label;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-18 21:41<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalAttributeIndexInfo {
    /**
     * Start label the first instruction corresponding to the scope of some local variable
     * (inclusive)
     */
    private Label startLabel;
    /**
     * End label the first instruction corresponding to the scope of some local variable
     * (inclusive)
     */
    private Label endLabel;
    /**
     * the local variable's index.
     */
    private int index;
}
