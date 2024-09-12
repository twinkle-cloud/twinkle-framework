package com.twinkle.framework.ruleengine.pool;

import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntUnaryOperator;
/**
 * Object Pool
 *
 * @author chenxj
 * @date 2024/09/03
 */
public class ObjectPool<T> implements IObjectPool<T>, IObjectPoolInfo {
    private ArrayBlockingQueue<T> blockingQueue;
    @Getter
    private ObjectFactory<T> objectFactory;
    private final SizeCounter maxObjectCounter;
    private final int maxSize;
    private final boolean strictFlag;
    private PoolContentionListener poolContentionListener;
    private PoolUsageStatistics usageStatistics;

    ObjectPool(ObjectFactory<T> _objectFactory, T[] _poolObjArray, int _maxSize, boolean _strictFlag, PoolContentionListener _listener) {
        this.poolContentionListener = _listener;
        this.objectFactory = _objectFactory;
        this.maxSize = _maxSize;
        this.strictFlag = _strictFlag;
        this.blockingQueue = new ArrayBlockingQueue<>(_maxSize);
        if (_poolObjArray != null) {
            this.blockingQueue.addAll(Arrays.asList(_poolObjArray));
        }

        this.maxObjectCounter = new SizeCounter(_maxSize - this.blockingQueue.size());
        this.usageStatistics = new PoolUsageStatistics();
        this.usageStatistics.createCounter.set(this.blockingQueue.size());
    }

    public ObjectPool(ObjectFactory<T> _objectFactory, int _maxSize, boolean _strictFlag, PoolContentionListener _listener) {
        this(_objectFactory, null, _maxSize, _strictFlag, _listener);
    }

    @Override
    public T getObject() throws Exception {
        this.usageStatistics.getCounter.incrementAndGet();
        T tempObj = this.blockingQueue.poll();
        if (tempObj == null) {
            tempObj = this.createObject();
            if (tempObj == null) {
                this.usageStatistics.waitCounter.incrementAndGet();
                tempObj = this.blockingQueue.take();
            }
        }

        return tempObj;
    }

    @Override
    public T getObject(long _timeOutSec) throws Exception {
        this.usageStatistics.getCounter.incrementAndGet();
        T tempObj = this.blockingQueue.poll();
        if (tempObj == null) {
            tempObj = this.createObject();
            if (tempObj == null) {
                this.usageStatistics.waitCounter.incrementAndGet();
                tempObj = this.blockingQueue.poll(_timeOutSec, TimeUnit.MILLISECONDS);
            }
        }

        return tempObj;
    }

    private T createObject() throws Exception {
        if (this.maxObjectCounter.getAndDecrement() == 0) {
            if (this.poolContentionListener != null) {
                this.poolContentionListener.onPoolContention();
            }

            if (this.strictFlag) {
                return null;
            }
        }

        this.usageStatistics.createCounter.incrementAndGet();
        return this.objectFactory.create();
    }

    @Override
    public void releaseObject(T _obj) {
        this.usageStatistics.releaseCounter.incrementAndGet();
        if (this.strictFlag) {
            this.blockingQueue.add(_obj);
        } else {
            this.blockingQueue.offer(_obj);
        }

    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override
    public int getCurrentSize() {
        return this.blockingQueue.size();
    }

    @Override
    public boolean getStrictSize() {
        return this.strictFlag;
    }

    @Override
    public long getGetOperations() {
        return this.usageStatistics.getCounter.get();
    }

    @Override
    public long getReleaseOperations() {
        return this.usageStatistics.releaseCounter.get();
    }

    @Override
    public long getWaitOperations() {
        return this.usageStatistics.waitCounter.get();
    }

    @Override
    public long getCreateOperations() {
        return this.usageStatistics.createCounter.get();
    }

    static class PoolUsageStatistics {
        private AtomicLong getCounter = new AtomicLong(0L);
        private AtomicLong releaseCounter = new AtomicLong(0L);
        private AtomicLong createCounter = new AtomicLong(0L);
        private AtomicLong waitCounter = new AtomicLong(0L);

        PoolUsageStatistics() {
        }
    }

    static class SizeCounter {
        private AtomicInteger counter;

        public SizeCounter(int _counter) {
            this.counter = new AtomicInteger(_counter);
        }

        public int getAndDecrement() {
            return this.counter.getAndUpdate(ObjectPool.SizeCounter.DecrementUntilZeroOperator.INSTANCE);
        }

        public int getValue() {
            return this.counter.get();
        }

        static class DecrementUntilZeroOperator implements IntUnaryOperator {
            static final DecrementUntilZeroOperator INSTANCE = new DecrementUntilZeroOperator();

            DecrementUntilZeroOperator() {
            }

            @Override
            public int applyAsInt(int _value) {
                return _value > 0 ? _value - 1 : _value;
            }
        }
    }
}
