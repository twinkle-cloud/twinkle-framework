package com.twinkle.framework.asm;

import com.twinkle.framework.asm.data.PartiallyDecodedData;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-06 14:57<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface PartiallyDecodedAware<E> {
    /**
     * Get the partially decoded data.
     *
     * @return
     */
    PartiallyDecodedData<E> getPartiallyDecodedData();

    /**
     * Update(set) some partially decoded data.
     *
     * @param _date
     */
    void setPartiallyDecodedData(PartiallyDecodedData<E> _date);
}
