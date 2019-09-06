package com.twinkle.framework.struct.resolver;

import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.type.StructAttributeType;
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
public class DefaultStructAttributeTypeResolver implements StructAttributeTypeResolver {
    private final StructAttributeSchema saSchema;

    public DefaultStructAttributeTypeResolver(StructAttributeSchema _schema) {
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
