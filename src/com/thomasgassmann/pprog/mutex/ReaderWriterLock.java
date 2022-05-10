package com.thomasgassmann.pprog.mutex;

// TODO: add tests
// TODO: exercise Exercise: come up with a
// better performing version
// using condition variables!
// Introduce an upper bound of k readers!
public class ReaderWriterLock {
    int writers = 0; // writers in CS
    int readers = 0; // readers in CS
    int writersWaiting = 0; // writers trying to enter CS
    int readersWaiting = 0; // readers trying to enter CS
    int writersWait = 0; // number of readers the writers have to wait for

    synchronized void acquire_read() {
        readersWaiting++;
        // Writers are waiting and the readers don't
        // have priority any more.
        while (writers > 0 || (writersWaiting > 0 && writersWait <= 0))
            try { wait(); }
            catch (InterruptedException e) {}
        readersWaiting--;
        writersWait--;
        readers++;
    }

    synchronized void release_read() {
        readers--;
        notifyAll();
    }

    synchronized void acquire_write() {
        writersWaiting++;
        // Writers have to wait until the waiting readers
        // have finished
        while (writers > 0 || readers > 0 || writersWait > 0)
            try { wait(); }
            catch (InterruptedException e) {}
        writersWaiting--;
        writers++;
    }

    synchronized void release_write() {
        writers--;
        // When a writer finishes, the number of
        // currently waiting readers may pass.
        writersWait = readersWaiting;
        notifyAll();
    }
}
