package com.twinkle.framework.core.type;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/3/19 2:16 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public enum BaseType {
    INT("int", 101, 1),
    STRING("string", 102, 2),
    LONG("long", 104, 3),
    FLOAT("float", 105, 4),
    DOUBLE("double", 106, 5),
    BYTE_ARRAY("byte[]", 109, 6),
    UNICODE_STRING_ARRAY("ustring", 107, 7),
    OBJECT("object", 0, 8),
    LIST("list", 0, 2),
    STRUCT("struct", 0, 8);

    private String name;
    private int type;
    private int primitiveType;
    private BaseType(String _name, int _type, int _primitiveType) {
        this.name = _name;
        this.type = _type;
        this.primitiveType = _primitiveType;
    }
}
