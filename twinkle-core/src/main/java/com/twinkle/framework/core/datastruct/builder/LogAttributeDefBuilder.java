package com.twinkle.framework.core.datastruct.builder;

import com.twinkle.framework.core.datastruct.schema.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-16 16:38<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class LogAttributeDefBuilder {

    public static AttributeDef getAttributeDef(){
        AttributeDef tempAttrDef = new AttributeDefImpl(
                "log", getLogTypeDef(),
                Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
                true,
                true,
                getValueDef()
        );
        return tempAttrDef;
    }

    private static TypeDef getLogTypeDef(){
        return new ClassTypeDefImpl("Logger", Logger.class);
    }

    /**
     * Get Object value.
     *
     * @return
     */
    private static StaticAttributeValueDef getValueDef(){
        StaticAttributeValueDefImpl tempDef = new StaticAttributeValueDefImpl();
        tempDef.setClassInternalName(Type.getInternalName(LoggerFactory.class));
        tempDef.setMethodName("getLogger");
        tempDef.setMethodDescriptor(Type.getMethodDescriptor(Type.getType(Logger.class), Type.getType(Class.class)));
        return tempDef;
    }
}
