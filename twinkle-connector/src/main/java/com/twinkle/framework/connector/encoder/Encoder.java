package com.twinkle.framework.connector.encoder;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.core.context.model.NormalizedContext;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-14 17:58<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Encoder extends Configurable {
    Object handleEncode(NormalizedContext _nc);
}
