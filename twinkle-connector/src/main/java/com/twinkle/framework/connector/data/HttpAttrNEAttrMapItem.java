package com.twinkle.framework.connector.data;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-29 22:24<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class HttpAttrNEAttrMapItem {
    /**
     * Name of the HTTP request header or request parameter
     * whose value will be copied to the NE.
     *
     * This Attribute can be response header as will, NE's value will
     * be copied to this value in this situation.
     */
    @NonNull
    private String httpAttr;
    /**
     * NE Attribute Name.
     */
    @NonNull
    private String neAttr;
    /**
     * NE Attribute Index in the PrimitiveAttributeSchema.
     */
    private int neAttrIndex;
    /**
     * The default value for httpAttr.
     * Default value that will be populated in the NE attribute
     * if the header is not set in the HTTP request.
     * If you do not specify a default value, that is,
     * if you leave this portion empty,
     * an empty value will be set as the value of the NE attribute.
     */
    private String defaultValue;
}
