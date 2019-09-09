package com.twinkle.framework.api.constant;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-17 17:08<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ExceptionCode {
    int LOGIC_CONF_INVALID_CONNECTOR = 0x001000;
    int LOGIC_CONF_INVALID_CLIENT_CONNECTOR = 0x001001;
    int LOGIC_CONF_INVALID_SERVER_CONNECTOR = 0x001002;
    int LOGIC_CONF_INVALID_DATACENTER = 0x001003;
    int LOGIC_CONF_INVALID_RULE = 0x001004;
    int LOGIC_CONF_INVALID_RULECHAIN = 0x001005;
    int LOGIC_CONF_DUPLICATE_CONNECTOR_NAME = 0x001006;
    int LOGIC_CONF_DUPLICATE_COMPONENT_FOUND = 0x001007;
    int LOGIC_CONF_INVALID_EXPRESSION = 0x001010;
    int LOGIC_CONF_REQUIRED_ATTR_MISSED = 0x001011;

    int LOGIC_CONF_ATTR_MISSED_IN_SCHEMA = 0x001100;
    int LOGIC_CONF_ATTR_NOT_INIT = 0x001101;
    int LOGIC_CONF_ATTR_INIT_INVALID = 0x001102;
    int LOGIC_CONF_ATTR_NOT_ALLOWED = 0x001103;
    int LOGIC_CONF_ATTR_VALUE_INVALID = 0x001104;
    /**
     * The attributes parameters missed for the Struct Attribute type.
     */
    int LOGIC_CONF_SA_TYPE_ATTR_MISSED = 0x001105;
    /**
     * Circular path found the Struct Attribute's build path.
     */
    int LOGIC_CONF_SA_TYPE_ATTR_CIRCULAR_PATH_FOUND = 0x001106;
    /**
     * Add struct attribute type into the type schema failed.
     */
    int LOGIC_CONF_SA_TYPE_ADD_FAILED = 0x001107;
    /**
     * Required attr field missed.
     */
    int LOGIC_CONF_SA_TYPE_ATTR_FIELD_MISSED = 0x001108;
    /**
     * Struct attribute's field type missed.
     */
    int LOGIC_CONF_ATTR_SA_TYPE_MISSED = 0x001109;
    /**
     * The attribute name is invalid.
     */
    int LOGIC_CONF_SA_NAME_INVALID = 0x00110a;
    /**
     * Struct attribute type is not found in the schema.
     */
    int LOGIC_CONF_SA_TYPE_NOT_FOUND = 0x00110b;
    /**
     * Struct attribute's namespace is not found in the schema.
     */
    int LOGIC_CONF_SA_NAMESPACE_NOT_FOUND = 0x00110c;

    int RULE_ADN_INVALID_URL = 0x010001;
    int RULE_ADN_URL_READ_FAILED = 0x010002;
    int RULE_ADN_URL_TOKEN_MISSED = 0x010003;
    int RULE_ADN_URL_PARSE_FAILED = 0x010004;
    int RULE_ADN_OPERATION_INIT_FAILED = 0x011005;
    int RULE_ADN_OPERATION_MAP_MISSED = 0x011006;
    int RULE_ADN_MAP_OPERATION_INVALID = 0x011007;

    int RULE_ADN_MATH_OPERATION_INVALID = 0x011008;

    int RULE_CON_EXPRES_ATTR_MISMATCH = 0x011009;
    int RULE_CON_EXPRES_ATTR_INVALID = 0x01100A;

    int RULE_MADANTORY_ATTR_MISSED = 0x011000;

    int CONNECTOR_MADANTORY_ATTR_MISSED = 0x012000;

    int COMPONENT_CLASS_MISSED = 0x012001;
    int COMPONENT_FACTORY_NOT_INITIALIZED = 0x012003;
    int CONNECTOR_INSTANTIATED_FAILED = 0x012002;
    /**
     * Access level of the class is incorrect.
     */
    int CONNECTOR_ACCESS_INCORRECT = 0x012003;
    /**
     * Apply Rule error.
     */
    int RULE_APPLY_ERROR = 0x020001;
    int RULE_APPLY_OUTSIDE_RULECHAIN = 0x020002;
}
