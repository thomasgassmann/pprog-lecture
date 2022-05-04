package com.thomasgassmann.pprog.mutex;

import java.util.concurrent.Semaphore;

public class Barrier {
    private Semaphore _mutex = new Semaphore(1, true);
    private Semaphore _barrier1 = new Semaphore(0, true);
    private Semaphore _barrier2 = new Semaphore(1, true);
    private volatile int _count = 0;
    private final int _n;

    public Barrier(int n) {
        _n = n;
    }

    public void sync() throws InterruptedException {
        _mutex.acquire();
        _count++;
        if (_count == _n) {
            _barrier2.acquire();
            _barrier1.release();
        }

        _mutex.release();

        _barrier1.acquire();
        _barrier1.release();

        _mutex.acquire();
        _count--;
        if (_count == 0) {
            _barrier1.acquire();
            _barrier2.release();
        }

        _mutex.release();

        _barrier2.acquire();
        _barrier2.release();
    }

}
