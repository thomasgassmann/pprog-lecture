package com.thomasgassmann.pprog.mutex;

public class Dekker implements Lock {
    public enum ThreadChoice {
        P,
        Q
    }

    private volatile boolean _wantP;
    private volatile boolean _wantQ;
    private volatile ThreadChoice _turn = ThreadChoice.P;

    @Override
    public void acquire(int thread) {
        acquire(thread == 0 ? ThreadChoice.P : ThreadChoice.Q);
    }

    @Override
    public void release(int thread) {
        release(thread == 0 ? ThreadChoice.P : ThreadChoice.Q);
    }

    public void acquire(ThreadChoice choice) {
        setWant(choice, true);
        while (readWant(other(choice))) {
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

    private boolean readWant(ThreadChoice choice) {
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
        if (choice == ThreadChoice.P) {
            _wantP = want;
            return;
        } else if (choice == ThreadChoice.Q) {
            _wantQ = want;
            return;
        }

        throw new IllegalArgumentException();
    }
}
