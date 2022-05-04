package com.thomasgassmann.pprog.datastructures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueueTests {
    @Test
    public void check() {
        final Queue q = new Queue(10);
        final int THREADS = 100;
        final int OPS = 1000;

        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPS; j++) {
                    q.enqueue(j);
                    q.dequeue();
                }
            });
            threads[i].start();
        }

        try {
            for (int i = 0; i < THREADS; i++)
                threads[i].join();
        } catch (InterruptedException e) {
            Assertions.fail();
        }

        Assertions.assertTrue(q.isEmpty());
    }
}
