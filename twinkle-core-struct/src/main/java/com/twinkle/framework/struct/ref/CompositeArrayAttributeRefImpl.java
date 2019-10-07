package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptorImpl;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.BadAttributeNameException;
import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.type.StructType;

import java.text.ParseException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/3/19 3:48 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CompositeArrayAttributeRefImpl extends CompositeAttributeRefImpl implements ArrayAttributeRef, Cloneable {

    public CompositeArrayAttributeRefImpl(StructType _saType, String _compositeName) throws BadAttributeNameException, AttributeNotFoundException, ParseException {
        this(_saType, (new CompositeName(_compositeName)).head(), StructAttributeSchemaManager.getStructAttributeFactory());
    }

    protected CompositeArrayAttributeRefImpl(StructType _saType, CompositeName _compositeName, StructAttributeFactory _factory) throws BadAttributeNameException, AttributeNotFoundException {
        super(_saType, _compositeName, _factory);
        AttributeRef tempAttrRef = this.getTailAttributeRef();
        if (!(tempAttrRef instanceof ArrayAttributeRef)) {
            throw new IllegalArgumentException("Attribute " + _compositeName + " is not an array");
        }
    }

    protected CompositeArrayAttributeRefImpl(StructType _saType, String _compositeName, StructAttributeFactory _factory) throws BadAttributeNameException, AttributeNotFoundException, ParseException {
        this(_saType, (new CompositeName(_compositeName)).head(), _factory);
    }

    /**
     * Replicate the [_index]'s composite name with given name.
     *
     * @param _compositeName
     * @param _index
     * @return
     * @throws ParseException
     */
    private String replaceLastIndex(String _compositeName, int _index) throws ParseException {
        CompositeName tempName = new CompositeName(_compositeName, false);
        return tempName.replicate(_index).fullName();
    }

    @Override
    public String getArrayAttributeName() {
        return this.tail.getCompositeName().name();
    }

    @Override
    public ArrayType getArrayType() {
        return ((ArrayAttributeRef) this.attrRef).getArrayType();
    }

    @Override
    public int getIndex() {
        return ((ArrayAttributeRef) this.attrRef).getIndex();
    }

    @Override
    public ArrayAttributeRef replicate(int _index) {
        ArrayAttributeRef tempAttrRef = (ArrayAttributeRef) this.attrRef;
        if (_index < 0) {
            throw new IllegalArgumentException("Invalid index: " + _index);
        } else if (tempAttrRef.getIndex() < 0) {
            throw new IllegalStateException("Cannot replicate non-indexed name: " + this.getName());
        }
        try {
            CompositeArrayAttributeRefImpl tempArrayAttrRef = (CompositeArrayAttributeRefImpl) super.clone();
            tempArrayAttrRef.attrRef = (StructAttributeRef) tempAttrRef.replicate(_index);
            tempArrayAttrRef.descriptor = new SAAttributeDescriptorImpl(this.descriptor, this.replaceLastIndex(this.descriptor.getName(), _index));
            tempArrayAttrRef.tail = this.tail.replicate(_index);
            return tempArrayAttrRef;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Unable to clone", e);
        } catch (ParseException e) {
            throw new RuntimeException("Failed parsing \"" + this.descriptor.getName() + "\"", e);
        }
    }

    @Override
    public void ensureSize(StructAttribute _attr) {
        ((ArrayAttributeRef) this.attrRef).ensureSize(_attr);
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
