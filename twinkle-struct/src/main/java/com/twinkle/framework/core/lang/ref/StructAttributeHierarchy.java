package com.twinkle.framework.core.lang.ref;

import com.twinkle.framework.core.asm.factory.StructAttributeFactoryImpl;
import com.twinkle.framework.core.error.BadAttributeNameException;
import com.twinkle.framework.core.lang.struct.ArrayType;
import com.twinkle.framework.core.lang.struct.StructAttributeFactory;
import com.twinkle.framework.core.lang.struct.StructAttributeType;
import com.twinkle.framework.core.lang.struct.StructType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 6:56 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeHierarchy implements Cloneable {
    private CompositeName compositeName;
    private final StructAttributeType parentType;
    private final StructAttributeRef thisRef;
    private final StructAttributeHierarchy head;
    private final StructAttributeHierarchy previous;
    private final StructAttributeHierarchy next;

    private StructAttributeHierarchy(StructAttributeHierarchy _previous, StructAttributeType _parentType, CompositeName _compositeName, StructAttributeFactory _factory) {
        this.compositeName = _compositeName;
        this.parentType = _parentType;
        this.previous = _previous;
        if (_previous == null) {
            this.head = this;
        } else {
            this.head = this.previous.head();
        }

        String tempName = _compositeName.name();
        this.thisRef = (StructAttributeRef)((StructAttributeFactoryImpl)_factory)._getAttributeRef(_parentType, tempName);
        StructType tempStructType = this.thisRef.getType();
        if (_compositeName.isTail()) {
            this.next = null;
        } else {
            StructAttributeType tempSAType;
            if (tempStructType.isArrayType()) {
                tempSAType = (StructAttributeType)((ArrayType)tempStructType).getElementType();
            } else {
                if (_compositeName.index() >= 0) {
                    throw new BadAttributeNameException(_compositeName.fullName());
                }
                tempSAType = (StructAttributeType)tempStructType;
            }
            this.next = new StructAttributeHierarchy(this, tempSAType, _compositeName.next(), _factory);
        }
    }

    public StructAttributeHierarchy(StructAttributeType _saType, CompositeName _compositeName, StructAttributeFactory _factory) {
        this(null, _saType, _compositeName.head(), _factory);
    }

    public CompositeName getCompositeName() {
        return this.compositeName;
    }

    public StructAttributeType getBaseType() {
        return this.head.getParentType();
    }

    public StructAttributeType getParentType() {
        return this.parentType;
    }

    public StructAttributeRef getAttributeRef() {
        return this.thisRef;
    }

    public StructAttributeHierarchy head() {
        return this.head;
    }

    public boolean isHead() {
        return this.previous == null;
    }

    public StructAttributeHierarchy tail() {
        StructAttributeHierarchy tempHierarchy;
        for(tempHierarchy = this; !tempHierarchy.isTail(); tempHierarchy = tempHierarchy.next()) {
        }

        return tempHierarchy;
    }

    public boolean isTail() {
        return this.next == null;
    }

    public StructAttributeHierarchy previous() {
        return this.previous;
    }

    public StructAttributeHierarchy next() {
        return this.next;
    }

    /**
     * Replicate the StructAttributeHierarchy.
     *
     * @param _index
     * @return
     */
    public StructAttributeHierarchy replicate(int _index) {
        try {
            StructAttributeHierarchy tempHierarchy = (StructAttributeHierarchy)super.clone();
            tempHierarchy.compositeName = this.compositeName.replicate(_index);
            StructAttributeHierarchy tempHierarchy1 = tempHierarchy;
            //Copy the up nodes.
            CompositeName tempCompositeName;
            for(tempCompositeName = tempHierarchy.compositeName; !tempHierarchy1.isHead(); tempHierarchy1.compositeName = tempCompositeName) {
                tempHierarchy1 = tempHierarchy1.previous;
                tempCompositeName = tempCompositeName.previous();
            }

            tempHierarchy1 = tempHierarchy;
            //Copy the down nodes.
            for(tempCompositeName = tempHierarchy.compositeName; !tempHierarchy1.isTail(); tempHierarchy1.compositeName = tempCompositeName) {
                tempHierarchy1 = tempHierarchy1.next;
                tempCompositeName = tempCompositeName.next();
            }

            return tempHierarchy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return this.compositeName.toString();
    }

}
