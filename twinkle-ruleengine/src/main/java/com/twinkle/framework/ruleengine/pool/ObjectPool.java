package com.twinkle.framework.ruleengine.pool;

import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntUnaryOperator;

public class ObjectPool<T> implements IObjectPool<T>, IObjectPoolInfo {
    private ArrayBlockingQueue<T> blockingQueue;
    @Getter
    private ObjectFactory<T> objectFactory;
    private SizeCounter _maxObjectCounter;
    private int maxSize;
    private boolean strictFlag;
    private PoolContentionListener poolContentionListener;
    private PoolUsageStatistics usageStatistics;

    ObjectPool(ObjectFactory<T> _objectFactory, T[] var2, int _maxSize, boolean _strictFlag, PoolContentionListener _listener) {
        this.poolContentionListener = _listener;
        this.objectFactory = _objectFactory;
        this.maxSize = _maxSize;
        this.strictFlag = _strictFlag;
        this.blockingQueue = new ArrayBlockingQueue<>(_maxSize);
        if (var2 != null) {
            this.blockingQueue.addAll(Arrays.asList(var2));
        }

        this._maxObjectCounter = new SizeCounter(_maxSize - this.blockingQueue.size());
        this.usageStatistics = new PoolUsageStatistics();
        this.usageStatistics.createCounter.set((long) this.blockingQueue.size());
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
    public T getObject(long var1) throws Exception {
        this.usageStatistics.getCounter.incrementAndGet();
        T tempObj = this.blockingQueue.poll();
        if (tempObj == null) {
            tempObj = this.createObject();
            if (tempObj == null) {
                this.usageStatistics.waitCounter.incrementAndGet();
                tempObj = this.blockingQueue.poll(var1, TimeUnit.MILLISECONDS);
            }
        }

        return tempObj;
    }

    private T createObject() throws Exception {
        if (this._maxObjectCounter.getAndDecrement() == 0) {
            if (this.poolContentionListener != null) {
                this.poolContentionListener.onPoolContention();
            }

            if (this.strictFlag) {
                return null;
            }
        }

        this.usageStatistics.createCounter.incrementAndGet();
        T tempObj = this.objectFactory.create();
        return tempObj;
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
        private AtomicInteger _counter;

        public SizeCounter(int var1) {
            this._counter = new AtomicInteger(var1);
        }

        public int getAndDecrement() {
            return this._counter.getAndUpdate(ObjectPool.SizeCounter.DecrementUntilZeroOperator.INSTANCE);
        }

        public int getValue() {
            return this._counter.get();
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
