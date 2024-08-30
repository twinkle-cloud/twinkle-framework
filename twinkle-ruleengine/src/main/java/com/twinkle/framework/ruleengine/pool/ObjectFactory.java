package com.twinkle.framework.ruleengine.pool;

public interface ObjectFactory<T> {
    T create() throws Exception;
}
