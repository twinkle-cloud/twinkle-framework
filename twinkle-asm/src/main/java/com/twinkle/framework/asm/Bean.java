package com.twinkle.framework.asm;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-28 21:48<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Bean {
    /**
     * Default Bean packages.
     */
    String DEFAULT_PACKAGE = "com.twinkle.framework.core.datastruct.beans.";
    /**
     * Default Rest controller package.
     */
    String REST_CONTROL_PACKAGE = "com.twinkle.framework.connector.http.controller.";
    /**
     * Default Struct Attribute's class package.
     */
    String STRUCT_ATTRIBUTE_PACKAGE = "com.twinkle.framework.struct.beans.";
    /**
     * General Beans' package
     */
    String STRUCT_ATTRIBUTE_GENERAL_PACKAGE = "com.twinkle.framework.struct.beans.general.";
    /**
     * Bean's impl suffix.
     */
    String IMPL_SUFFIX = "Impl";
    /**
     * Bean's impl builder suffix.
     */
    String IMPL_BUILDER_SUFFIX = "$ImplBuilder";
    /**
     * Impl builder's inner name.
     */
    String IMPL_BUILDER_INNER_NAME = "ImplBuilder";
    /**
     * Impl builder's separator.
     */
    String INNER_SEPARATOR = "$";
    /**
     * Impl's Impl Builder suffix.
     */
    String IMPL_IMPL_BUILDER_SUFFIX = "Impl$ImplBuilder";
    /**
     * Legacy type separator.
     */
    String LEGACY_TYPE_SEPARATOR = ":";
    /**
     * Type Separator.
     */
    String TYPE_SEPARATOR = ".";
}
