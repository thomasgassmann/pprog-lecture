package com.thomasgassmann.pprog.producerconsumer;

public class ProducerConsumer implements Runnable {
    @Override
    public void run() {
        var q = new UnboundedBuffer();
        Thread producer = new Thread(new Producer(q));
        producer.start();

        for (int i = 0; i < 3; i++) {
            Thread consumer = new Thread(new Consumer(q));
            consumer.start();
        }
    }
}
