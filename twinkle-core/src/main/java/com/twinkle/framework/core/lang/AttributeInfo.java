package com.twinkle.framework.core.lang;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-12 10:48<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Slf4j
public class AttributeInfo {
    /**
     * Attribute's TYPE.
     */
    private int type;
    /**
     * Map to PRIMITIVE TYE of the attribute.
     */
    private int primitiveType;
    /**
     * Name of the attribute.
     */
    private String name;
    /**
     * The index of the attribute.
     */
    private int index;
    /**
     * The class name of the attribute
     */
    private String className;
    /**
     * The class of the attribute.
     */
    private Class<?> attributeClass;
    /**
     * The description of the attribute class.
     */
    private String description;

    public AttributeInfo(int _type, int _primitiveType, String _name, int _index, String _className) {
        this(_type, _primitiveType, _name, _index, _className, null);
    }

    public AttributeInfo(int _type, int _primitiveType, String _name, int _index, String _className, String _description) {
        this.type = _type;
        this.primitiveType = _primitiveType;
        this.name = _name;
        this.index = _index;
        this.className = _className;

        try {
            this.attributeClass = Class.forName(_className);
        } catch (Exception ex) {
            log.debug("Cannot get new instance of {}", _className, ex);
        }

        this.description = _description;
    }

    /**
     * Build a new instance.
     *
     * @return
     */
    public Attribute newAttributeInstance() {
        Attribute tempAttr = null;

        try {
            tempAttr = (Attribute)this.attributeClass.newInstance();
        } catch (Exception ex) {
            log.debug("Cannot get new instance of {}.", this.className, ex);
        }
        return tempAttr;
    }
}
