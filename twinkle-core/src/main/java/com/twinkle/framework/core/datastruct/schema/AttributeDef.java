package com.twinkle.framework.core.datastruct.schema;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 22:58<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeDef {
    String GETTER_PREFIX = "get";
    String SETTER_PREFIX = "set";
    String FIELD_PREFIX = "_";
    String FLAG_SUFFIX = "Flag";
    String DEFAULT_SUFFIX = "Default";

    /**
     * Get Field Access.
     *
     * @return
     */
    int getAccess();

    String getName();

    TypeDef getType();

    String getGetterName();

    String getSetterName();

    String getConstantName();

    String getFieldName();

    boolean isRequired();

    boolean isReadOnly();

    Object getDefaultValue();

    boolean isConstant();

    List<AnnotationDef> getAnnotations();

    /**
     * Get extra info.
     *
     * @return
     */
    JSONObject getExtraInfo();

    /**
     * Get extra info.
     *
     * @param _key
     * @return
     */
    Object getExtraInfoByKey(String _key);
}
