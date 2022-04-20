package com.thomasgassmann.pprog.mutex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MutexTests {
    @Test
    public void checkDekker() {
        Dekker lock = new Dekker();
        new CounterTest(2) {
            @Override
            public void acquire(int thread) {
                lock.acquire(thread == 0 ? Dekker.ThreadChoice.P : Dekker.ThreadChoice.Q);
            }

            @Override
            public void release(int thread) {
                lock.release(thread == 0 ? Dekker.ThreadChoice.P : Dekker.ThreadChoice.Q);
            }
        }.run();
    }

    @Test
    public void checkPeterson() {
        Peterson lock = new Peterson();
        new CounterTest(2) {
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

    @Test
    public void checkFilterLock() {
        final int COUNT = 10;
        FilterLock lock = new FilterLock(COUNT);
        new CounterTest(COUNT) {
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
