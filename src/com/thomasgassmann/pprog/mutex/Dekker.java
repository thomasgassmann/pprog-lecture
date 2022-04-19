package com.thomasgassmann.pprog.mutex;

public class Dekker {
    public enum ThreadChoice {
        P,
        Q
    }

    private volatile boolean _wantP;
    private volatile boolean _wantQ;
    private volatile ThreadChoice _turn = ThreadChoice.P;

    public void acquire(ThreadChoice choice) {
        setWant(choice, true);
        while (read(other(choice))) {
            if (_turn == other(choice)) {
                setWant(choice, false);
                while (_turn != choice);
                setWant(choice, true);
            }
        }
    }

    public void release(ThreadChoice choice) {
        _turn = other(choice);
        setWant(choice, false);
    }

    private boolean read(ThreadChoice choice) {
        if (choice == ThreadChoice.P) {
            return _wantP;
        } else if (choice == ThreadChoice.Q) {
            return _wantQ;
        }

        throw new IllegalArgumentException();
    }

    private ThreadChoice other(ThreadChoice choice) {
        if (choice == ThreadChoice.P) {
            return ThreadChoice.Q;
        } else if (choice == ThreadChoice.Q) {
            return ThreadChoice.P;
        }

        throw new IllegalArgumentException();
    }

    private void setWant(ThreadChoice choice, boolean want) {
        switch (choice) {
            case P -> _wantP = want;
            case Q -> _wantQ = want;
        }
    }
}
