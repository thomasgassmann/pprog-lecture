package com.thomasgassmann.pprog.mutex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class BarrierTests {
    @Test
    public void check() {
        final int THREADS = 1000;
        final int BOUND = 1000;
        Barrier b = new Barrier(THREADS);
        Thread[] threads = new Thread[THREADS];
        final Random random = new Random();
        long start = System.nanoTime();
        int threadWithMax = random.nextInt(THREADS);
        for (int i = 0; i < THREADS; i++) {
            final int delay = i == threadWithMax ? BOUND : random.nextInt(BOUND);
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep(delay);
                    b.sync();
                } catch (InterruptedException e) {
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < THREADS; i++) {
            try {
                threads[i].join();
                long total = System.nanoTime() - start;
                Assertions.assertTrue(total >= Math.pow(10, 6) * BOUND);
            } catch (InterruptedException e) {
                Assertions.fail();
            }
        }
    }
}
