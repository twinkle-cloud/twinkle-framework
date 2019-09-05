package com.twinkle.framework.core.utils;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 14:48<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CollectionEnumeration <E> implements Collection<E>, Enumeration<E> {
    private final Collection<E> collection;
    private Iterator<E> iterator;

    public CollectionEnumeration(Collection<E> _collection) {
        this.collection = _collection;
    }

    @Override
    public boolean hasMoreElements() {
        if (this.iterator == null) {
            this.iterator = this.iterator();
        }

        return this.iterator.hasNext();
    }
    @Override
    public E nextElement() {
        if (this.iterator == null) {
            this.iterator = this.iterator();
        }

        return this.iterator.next();
    }
    @Override
    public int size() {
        return this.collection.size();
    }

    @Override
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    @Override
    public boolean contains(Object _obj) {
        return this.collection.contains(_obj);
    }

    @Override
    public Iterator<E> iterator() {
        return this.collection.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.collection.toArray();
    }

    @Override
    public <T> T[] toArray(T[] _array) {
        return this.collection.toArray(_array);
    }

    @Override
    public boolean add(E _item) {
        return this.collection.add(_item);
    }

    @Override
    public boolean remove(Object _item) {
        return this.collection.remove(_item);
    }

    @Override
    public boolean containsAll(Collection<?> _collection) {
        return this.collection.containsAll(_collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> _collection) {
        return this.collection.addAll(_collection);
    }

    @Override
    public boolean removeAll(Collection<?> _collection) {
        return this.collection.removeAll(_collection);
    }

    @Override
    public boolean retainAll(Collection<?> _collection) {
        return this.collection.retainAll(_collection);
    }

    @Override
    public void clear() {
        this.collection.clear();
    }

    @Override
    public boolean equals(Object var1) {
        return this.collection.equals(var1);
    }

    @Override
    public int hashCode() {
        return this.collection.hashCode();
    }
}
