package com.thomasgassmann.pprog.mutex;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class Peterson implements Lock {
    private AtomicIntegerArray _flags;
    private volatile int _victim;

    public Peterson() {
        _flags = new AtomicIntegerArray(2);
        _victim = 0;
    }

    @Override
    public void acquire(int thread) {
        assert thread == 0 || thread == 1;
        _flags.set(thread, 1);
        _victim = thread;
        while (_flags.get(1 - thread) == 1 && _victim == thread);
    }

    @Override
    public void release(int thread) {
        assert thread == 0 || thread == 1;
        _flags.set(thread, 0);
    }
}
