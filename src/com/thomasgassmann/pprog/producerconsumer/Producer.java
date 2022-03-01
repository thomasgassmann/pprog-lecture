package com.thomasgassmann.pprog.producerconsumer;

public class Producer implements Runnable {
    private UnboundedBuffer _buffer;

    public Producer(UnboundedBuffer buffer) {
        _buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            synchronized (_buffer) {
                _buffer.add(i);
                _buffer.notifyAll();
            }

            i++;
        }
    }
}
