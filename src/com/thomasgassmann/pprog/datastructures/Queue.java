package com.thomasgassmann.pprog.datastructures;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Queue {
    private int size;
    private long[] buf;
    private final Lock lock = new ReentrantLock();
    private int n = 0;
    private int m = 0;
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private int in = 0;
    private int out = 0;

    public Queue(int s) {
        size = s;
        m = size - 1;
        buf = new long[size];
    }

    public void enqueue(long x) {
        lock.lock();
        m--;
        if (m < 0) {
            while (isFull()) {
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                }
            }
        }

        doEnqueue(x);
        n++;
        if (n <= 0) {
            notEmpty.signal();
        }

        lock.unlock();
    }

    public long dequeue() {
        lock.lock();
        n--;
        if (n < 0) {
            while (isEmpty()) {
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                }
            }
        }

        long x = doDequeue();
        m++;
        if (m <= 0) {
            notFull.signal();
        }

        lock.unlock();
        return x;
    }

    public boolean isEmpty() {
        return in == out;
    }

    private long doDequeue() {
        long item = buf[out];
        out = next(out);
        return item;
    }

    private void doEnqueue(long x) {
        buf[in] = x;
        in = next(in);
    }

    private boolean isFull() {
        return next(in) == out;
    }

    private int next(int i) {
        return (i + 1) % size;
    }
}
