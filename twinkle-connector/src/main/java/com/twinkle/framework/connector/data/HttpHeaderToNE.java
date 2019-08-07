package com.twinkle.framework.connector.data;

import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.context.ContextSchema;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-29 18:40<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class HttpHeaderToNE {
    private static final int CHAR_COMMA = 44;
    /**
     * Http header attribute.
     */
    private final String attributeName;
    /**
     * NE attribute's index in ContextSchema.
     */
    private final int attributeIndex;
    /**
     * Default value for attribute.
     */
    private final String defaultValue;

    public HttpHeaderToNE(String _expression) throws ConfigurationException {
        int tempIndex = _expression.indexOf(CHAR_COMMA);
        String tempAttrName = null;
        String tempDefaultValue = null;
        if (tempIndex == -1) {
            this.attributeName = _expression;
        } else {
            this.attributeName = _expression.substring(0, tempIndex);
            int tempSecondIndex = _expression.indexOf(CHAR_COMMA, tempIndex + 1);
            if (tempSecondIndex == -1) {
                tempAttrName = _expression.substring(tempIndex + 1);
            } else {
                tempAttrName = _expression.substring(tempIndex + 1, tempSecondIndex);
                tempDefaultValue = _expression.substring(tempSecondIndex + 1);
            }
        }

        if (tempAttrName != null && tempAttrName.trim().length() != 0) {
            this.attributeIndex = ContextSchema.getInstance().getAttributeIndex(tempAttrName, _expression);
        } else {
            this.attributeIndex = -1;
        }

        this.defaultValue = tempDefaultValue;
    }

    /**
     * Get the default value in this expression.
     *
     * @return
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Get the NE Attribute index in the context schema.
     *
     * @return
     */
    public int getAttributeIndex() {
        return this.attributeIndex;
    }

    /**
     * Get the Http Header parameter name.
     *
     * @return
     */
    public String getAttributeName() {
        return this.attributeName;
    }
}
