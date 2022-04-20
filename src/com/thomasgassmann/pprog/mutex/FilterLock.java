package com.thomasgassmann.pprog.mutex;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class FilterLock {
    private AtomicIntegerArray _level;
    private AtomicIntegerArray _victim;

    private int _threadCount;

    public FilterLock(int threads) {
        _threadCount = threads;
        _level = new AtomicIntegerArray(threads);
        _victim = new AtomicIntegerArray(threads);
    }

    public void acquire(int thread) {
        for (int i = 1; i < _threadCount; i++) {
            _level.set(thread, i);
            _victim.set(i, thread);
            while (thread == _victim.get(i) && others(thread, i));
        }
    }

    public void release(int thread) {
        _level.set(thread, 0);
    }

    private boolean others(int me, int lev) {
        for (int k = 0; k < _threadCount; k++) {
            if (k != me && _level.get(k) >= lev) {
                return true;
            }
        }

        return false;
    }
}
