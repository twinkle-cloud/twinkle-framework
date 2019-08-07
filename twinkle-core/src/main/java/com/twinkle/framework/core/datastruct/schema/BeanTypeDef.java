package com.twinkle.framework.core.datastruct.schema;

import org.objectweb.asm.Type;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 23:02<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BeanTypeDef extends BeanRefTypeDef {
    List<String> getInterfaces();

    List<TypeDef> getParents();

    TypeDef addParent(Type var1);

    List<AttributeDef> getAttributes();

    List<AnnotationDef> getAnnotations();
}
