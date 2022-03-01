package com.thomasgassmann.pprog.producerconsumer;

public class Consumer implements Runnable {
    private UnboundedBuffer _buffer;

    public Consumer(UnboundedBuffer buffer) {
        _buffer = buffer;
    }

    @Override
    public void run() {
        int res;
        while (true) {
            synchronized (_buffer) {
                while (_buffer.isEmpty()) {
                    try {
                        _buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                res = _buffer.remove();
            }

            System.out.println("Consuming " + res);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
