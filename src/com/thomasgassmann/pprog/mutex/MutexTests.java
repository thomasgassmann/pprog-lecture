package com.thomasgassmann.pprog.mutex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MutexTests {
    @Test
    public void checkDekker() {
        Dekker lock = new Dekker();
        run(2, lock);
    }

    @Test
    public void checkPeterson() {
        Peterson lock = new Peterson();
        run(2, lock);
    }

    @Test
    public void checkFilterLock() {
        final int COUNT = 10;
        FilterLock lock = new FilterLock(COUNT);
        run(COUNT, lock);
    }

    @Test
    public void checkBakeryLock() {
        final int COUNT = 5;
        BakeryLock lock = new BakeryLock(COUNT);
        run(COUNT, lock);
    }

    @Test
    public void checkTASLock() {
        final int COUNT = 10;
        TASLock lock = new TASLock();
        run(COUNT, lock);
    }

    @Test
    public void checkBackoffTASLock() {
        final int COUNT = 10;
        BackoffTASLock lock = new BackoffTASLock();
        run(COUNT, lock);
    }

    private static void run(int n, Lock lock) {
        new CounterTest(n) {
            @Override
            public void acquire(int thread) {
                lock.acquire(thread);
            }

            @Override
            public void release(int thread) {
                lock.release(thread);
            }
        }.run();
    }

    private static abstract class CounterTest {
        private int _threads;
        private volatile int _value = 0;
        private final int COUNT = 10000;

        public CounterTest(int threads) {
            _threads = threads;
        }

        public abstract void acquire(int thread);
        public abstract void release(int thread);

        public void run() {
            Thread[] threads = new Thread[_threads];
            for (int i = 0; i < _threads; i++) {
                final int c = i;
                threads[c] = new Thread(() -> {
                    for (int j = 0; j < COUNT; j++) {
                        acquire(c);
                        _value++;
                        release(c);
                    }
                });
                threads[c].start();
            }

            try {
                for (int i = 0; i < _threads; i++)
                    threads[i].join();
            } catch (InterruptedException e) {
                Assertions.fail();
                return;
            }

            Assertions.assertEquals(COUNT * _threads, _value);
        }
    }
}
