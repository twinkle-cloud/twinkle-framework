package com.twinkle.framework.ruleengine.pool;

public interface IObjectPool<T> {
    T getObject() throws Exception;

    T getObject(long _timeOutSec) throws Exception;

    void releaseObject(T _obj);

    int getMaxSize();

    int getCurrentSize();

    boolean getStrictSize();
}
