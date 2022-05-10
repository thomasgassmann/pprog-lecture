package com.thomasgassmann.pprog.mutex;

// TODO: add tests
// TODO: exercise Exercise: come up with a
// better performing version
// using condition variables!
public class ReaderWriterLock {
    int writers = 0;
    int readers = 0;
    int writersWaiting = 0;

    synchronized void acquire_read() {
        while (writers > 0 || writersWaiting > 0)
            try { wait(); }
            catch (InterruptedException e) {}
        readers++;
    }

    synchronized void release_read() {
        readers--;
        notifyAll();
    }

    synchronized void acquire_write() {
        writersWaiting++;
        while (writers > 0 || readers > 0)
            try { wait(); }
            catch (InterruptedException e) {}
        writersWaiting--;
        writers++;
    }

    synchronized void release_write() {
        writers--;
        notifyAll();
    }
}