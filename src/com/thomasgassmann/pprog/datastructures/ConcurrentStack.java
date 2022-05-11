package com.thomasgassmann.pprog.datastructures;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentStack {
    public static class Node {
        public final Long item;
        public Node next;
        public Node(Long item) {
            this.item = item;
        }
        public Node(Long item, Node n) {
            this.item = item;
            next = n;
        }
    }

    AtomicReference<Node> top = new AtomicReference<Node>();

    // TODO: tests, improve performance using backoff
    public Long pop() {
        Node head, next;
        do {
            head = top.get();
            if (head == null) return null;
            next = head.next;
        } while (!top.compareAndSet(head, next));
        return head.item;
    }

    public void push(Long item) {
        Node newi = new Node(item);
        Node head;
        do {
            head = top.get();
            newi.next = head;
        } while (!top.compareAndSet(head, newi));
    }
}
