package com.twinkle.framework.core.lang.util;

import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 10:40 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class ImmutableIterator<E> implements Iterator<E> {
    private Iterator<E> iterator = null;

    public ImmutableIterator(Iterator<E> _iterator) {
        this.iterator = _iterator;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public E next() {
        return this.iterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot perform a remove operation on immutable Collection.");
    }
}
