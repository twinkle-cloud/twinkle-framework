package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.converter.AttributeConverter;
import com.twinkle.framework.core.datastruct.converter.LooseAttributeConverter;
import com.twinkle.framework.core.datastruct.define.StructAttributeBeanTypeDef;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 11:00 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeInterfaceDesigner extends RecyclableBeanInterfaceDesigner {
    public StructAttributeInterfaceDesigner(String _className, StructAttributeBeanTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }
    @Override
    protected AttributeConverter initAttributeConverter(String _className) {
        return new LooseAttributeConverter(_className, StructAttributeClassDesigner.RESERVED_NAMES);
    }
}
