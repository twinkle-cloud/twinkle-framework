package com.twinkle.framework.core.datastruct.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-16 15:17<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaticAttributeValueDefImpl implements StaticAttributeValueDef {
    private String classInternalName;
    private String methodName;
    private String methodDescriptor;
}
