package com.twinkle.framework.ruleengine.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Object Pool Factory
 *
 * @author chenxj
 * @date 2024/09/03
 */
public abstract class ObjectPoolFactory {
    public static <T> IObjectPool<T> newObjectPool(ObjectFactory<T> _objFactory, int _num, int _maxSize, boolean _strictFlag, PoolContentionListener _listener, ExecutorService _executor, boolean _isPrototype) throws Exception {
        T[] tempObjArray = init(_objFactory, _num, _executor, _isPrototype);
        return new ObjectPool<>(_objFactory, tempObjArray, _maxSize, _strictFlag, _listener);
    }

    static <T> T[] init(final ObjectFactory<T> _objFactory, int _num, ExecutorService _executor, boolean _isPrototype) throws InterruptedException, Exception, ExecutionException {
        List<T> tempResultPoolObjList = new ArrayList<>();

        T tempObj = _isPrototype ? null : _objFactory.create();

        List<Callable<T>> tempObjList = Collections.nCopies(_num, new Callable<T>() {
            @Override
            public T call() throws Exception {
                if(_isPrototype){
                    return _objFactory.create();
                }
                return tempObj;//如果有性能瓶颈，可放开，并将Bean注册机制调整为多例模式。
            }
        });

        if (_executor != null) {
            List<Future<T>> tempPoolObjList = new ArrayList<>(_executor.invokeAll(tempObjList));

            for (Future<T> tempFuture : tempPoolObjList) {
                tempResultPoolObjList.add(tempFuture.get());
            }
        } else {
            for (int i = 0; i < _num; ++i) {
                tempResultPoolObjList.add(_objFactory.create());
            }
        }

        return (T[]) tempResultPoolObjList.toArray();
    }
}
