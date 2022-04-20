package com.thomasgassmann.pprog.mutex;

public interface Lock {
    void acquire(int thread);
    void release(int thread);
}
