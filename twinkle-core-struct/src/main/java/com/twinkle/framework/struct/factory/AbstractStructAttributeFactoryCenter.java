package com.twinkle.framework.struct.factory;

import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.AttributeTypeMismatchException;
import com.twinkle.framework.struct.error.BadAttributeNameException;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.util.ArrayAllocator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 10:37 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractStructAttributeFactoryCenter implements StructAttributeFactoryCenter {
    private volatile StructAttributeSchema structAttributeSchema = null;

    public AbstractStructAttributeFactoryCenter() {
    }

    public ArrayAllocator getArrayAllocator() {
        return this.getStructAttributeFactory().getArrayAllocator();
    }

    public AttributeRef getAttributeRef(StructType _saType, String _className) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        return this.getStructAttributeFactory().getAttributeRef(_saType, _className);
    }

    public StructAttributeSchema getSchema() {
        StructAttributeSchema tempSchema = this.structAttributeSchema;
        if (tempSchema == null) {
            throw new IllegalStateException("Schema is not set");
        } else {
            return tempSchema;
        }
    }

    public void setSchema(StructAttributeSchema _schema) {
        this.setSchemaInternal(_schema);
    }

    protected void setSchemaInternal(StructAttributeSchema _schema) {
        this.structAttributeSchema = _schema;
    }
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    @Override
    public String toString() {
        return this.getName();
    }
}
