package com.twinkle.framework.connector.convert.encoder;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.context.model.DefaultNormalizedContext;

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
    Object handleEncode(DefaultNormalizedContext _nc);
}
