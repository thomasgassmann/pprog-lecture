package com.thomasgassmann.pprog.producerconsumer;

import java.util.ArrayList;

public class UnboundedBuffer {
    private ArrayList<Integer> _items = new ArrayList<>();

    public int remove() {
        return _items.remove(0);
    }

    public void add(int i) {
        _items.add(i);
    }

    public boolean isEmpty() {
        return _items.size() == 0;
    }
}
