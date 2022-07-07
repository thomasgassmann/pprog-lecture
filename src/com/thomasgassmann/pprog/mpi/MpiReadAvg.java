package com.thomasgassmann.pprog.mpi;

import mpi.MPI;

import java.util.Arrays;
import java.util.Random;

public class MpiReadAvg {
    private static int[] getData(int n) {
        int[] res = new int[n];
        Random random = new Random(42);
        for (int i = 0; i < n; i++) {
            res[i] = random.nextInt();
        }

        return res;
    }

    public static void calculate(String[] args) {
        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        int N = 1000;
        int[] data = rank == 0 ? getData(N) : new int[N];

        int chunkSize = N / size;
        int[] local = new int[chunkSize];

        MPI.COMM_WORLD.Scatter(data, 0, chunkSize, MPI.INT, local, 0, chunkSize, MPI.INT, 0);

        int sum = 0;
        for (int i = 0; i < chunkSize; i++) {
            sum += local[i];
        }

        double[] avgs = new double[size];
        avgs[0] = sum / (double)chunkSize;

        MPI.COMM_WORLD.Gather(avgs, 0, 1, MPI.DOUBLE, avgs, 0, 1, MPI.DOUBLE, 0);
        if (rank == 0) {
            double total = 0.0;
            for (int i = 0; i < avgs.length; i++) {
                total += avgs[i];
            }

            System.out.println(total / avgs.length);
        }

        MPI.Finalize();
    }
}
