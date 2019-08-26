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
    /**
     * Get interfaces list the Bean will implement.
     *
     * @return
     */
    List<String> getInterfaces();

    /**
     * Get class and interface list the Bean will extend or implement.
     *
     * @return
     */
    List<TypeDef> getInterfaceTypeDefs();

    /**
     * Get Super type define.
     *
     * @return
     */
    TypeDef getSuperTypeDef();

    /**
     * To add parent type for this bean.
     *
     * @param _interfaceType
     * @return
     */
    TypeDef addInterfaceTypeDef(Type _interfaceType);

    /**
     * Get the attributes list of this bean.
     *
     * @return
     */
    List<AttributeDef> getAttributes();

    /**
     * Get the annotations list of this bean.
     *
     * @return
     */
    List<AnnotationDef> getAnnotations();
}
