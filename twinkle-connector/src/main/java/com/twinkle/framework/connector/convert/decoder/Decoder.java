package com.twinkle.framework.connector.convert.decoder;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.context.model.DefaultNormalizedContext;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-14 18:07<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Decoder extends Configurable {
    void doDecode(DefaultNormalizedContext _nc);
}
