package com.twinkle.framework.struct.utils;

import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.type.StructAttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/14/19 10:29 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeUtil {
    /**
     * Build a new empty StructAttribute with RootType name.
     *
     * @param _qualifiedName
     * @return
     */
    public static StructAttribute newStructAttribute(String _qualifiedName) {
        StructAttributeSchema tempStructSchema = StructAttributeSchemaManager.getStructAttributeSchema();
        StructAttributeFactory tempFactory = StructAttributeSchemaManager.getStructAttributeFactory();
        StructAttributeType tempType = tempStructSchema.getStructAttributeType(_qualifiedName);
        return tempFactory.newStructAttribute(tempType);
    }
}
