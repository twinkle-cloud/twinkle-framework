package com.twinkle.framework.core.datastruct;

import com.twinkle.framework.core.context.StructAttributeSchema;
import com.twinkle.framework.core.error.AttributeNotFoundException;
import com.twinkle.framework.core.error.AttributeTypeMismatchException;
import com.twinkle.framework.core.error.BadAttributeNameException;
import com.twinkle.framework.core.lang.ref.AttributeRef;
import com.twinkle.framework.core.lang.struct.StructAttributeType;
import com.twinkle.framework.core.lang.util.ArrayAllocator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 10:37 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractStructAttributeFactoryCenter implements StructAttributeFactoryCenter{
    private volatile StructAttributeSchema structAttributeSchema = null;

    public AbstractStructAttributeFactoryCenter() {
    }

    public ArrayAllocator getArrayAllocator() {
        return this.getStructAttributeFactory().getArrayAllocator();
    }

    public AttributeRef getAttributeRef(StructAttributeType var1, String var2) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        return this.getStructAttributeFactory().getAttributeRef(var1, var2);
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
