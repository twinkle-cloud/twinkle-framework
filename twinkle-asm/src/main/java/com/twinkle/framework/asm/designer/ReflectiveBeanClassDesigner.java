package com.twinkle.framework.asm.designer;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.PartiallyDecodedAware;
import com.twinkle.framework.asm.RecyclableBean;
import com.twinkle.framework.asm.ReflectiveBean;
import com.twinkle.framework.asm.data.PartiallyDecodedData;
import com.twinkle.framework.asm.define.AttributeDef;
import com.twinkle.framework.asm.define.AttributeDefImpl;
import com.twinkle.framework.asm.define.BeanTypeDef;
import com.twinkle.framework.asm.define.ClassTypeDefImpl;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.Collections;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 22:48<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ReflectiveBeanClassDesigner extends SimpleReflectiveBeanClassDesigner {
    public static final AttributeDef PARTIALLY_DECODED_ATTRIBUTE;
    static{
        PARTIALLY_DECODED_ATTRIBUTE = new AttributeDefImpl("partiallyDecodedData", new ClassTypeDefImpl("PartiallyDecodedData", PartiallyDecodedData.class), false, false, null);
    }
    public ReflectiveBeanClassDesigner(String _className, BeanTypeDef _typeDef) {
        super(_className, _typeDef);
    }
    @Override
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName(), RecyclableBean.class.getName(), ReflectiveBean.class.getName(), Cloneable.class.getName(), PartiallyDecodedAware.class.getName()};
    }

    @Override
    protected void addAttributeGettersSetters(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        super.addAttributeGettersSetters(_visitor, _className, _attrDefList);
        List<AttributeDef> tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(boolean[].class));
        this.addAttributeGetterDefinition(_visitor, boolean[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, boolean[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(char[].class));
        this.addAttributeGetterDefinition(_visitor, char[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, char[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(byte[].class));
        this.addAttributeGetterDefinition(_visitor, byte[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, byte[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(short[].class));
        this.addAttributeGetterDefinition(_visitor, short[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, short[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(int[].class));
        this.addAttributeGetterDefinition(_visitor, int[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, int[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(long[].class));
        this.addAttributeGetterDefinition(_visitor, long[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, long[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(float[].class));
        this.addAttributeGetterDefinition(_visitor, float[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, float[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(double[].class));
        this.addAttributeGetterDefinition(_visitor, double[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, double[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.getType(String[].class));
        this.addAttributeGetterDefinition(_visitor, String[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, String[].class, _className, tempAttrDefList);
        tempAttrDefList = this.filterObjectArraysExcludes(_attrDefList, Collections.singletonList(Type.getType(String[].class)));
        this.addAttributeGetterDefinition(_visitor, Object[].class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Object[].class, _className, tempAttrDefList, true);
        this.addPartiallyDecoded(_visitor, _className);
    }

    protected void addPartiallyDecoded(ClassVisitor _visitor, String _className) {
        this.addField(_visitor, PARTIALLY_DECODED_ATTRIBUTE);
        this.addFlagField(_visitor, PARTIALLY_DECODED_ATTRIBUTE);
        this.addGetterDefinition(_visitor, _className, PARTIALLY_DECODED_ATTRIBUTE);
        this.addSetterDefinition(_visitor, _className, PARTIALLY_DECODED_ATTRIBUTE);
    }
    @Override
    protected void addAdditionalAttributesToString(MethodVisitor _visitor, String _className) {
        super.addAdditionalAttributesToString(_visitor, _className);
        this.addAppendStringStatement(_visitor, ",");
        this.addAppendStatement(_visitor, _className, PARTIALLY_DECODED_ATTRIBUTE);
    }
}
