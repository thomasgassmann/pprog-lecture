package com.thomasgassmann.pprog;

import com.thomasgassmann.pprog.mpi.MpiPi;

import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        MpiPi.calculate(args, 100000000);
    }
}
