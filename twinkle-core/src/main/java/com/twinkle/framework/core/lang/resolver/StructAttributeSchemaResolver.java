package com.twinkle.framework.core.lang.resolver;

import com.twinkle.framework.core.context.StructAttributeSchema;
import com.twinkle.framework.core.lang.struct.StructAttributeType;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 5:04 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class StructAttributeSchemaResolver implements StructAttributeTypeResolver {
    private final StructAttributeSchema saSchema;

    public StructAttributeSchemaResolver(StructAttributeSchema _schema) {
        this.saSchema = _schema;
    }

    @Override
    public StructAttributeType getStructAttributeType(String _namespace, String _structTypeName) {
        return this.saSchema.getStructAttributeType(_namespace, _structTypeName);
    }

    @Override
    public StructAttributeType getStructAttributeType(String _structTypeName) {
        return this.saSchema.getStructAttributeType(_structTypeName);
    }
}
