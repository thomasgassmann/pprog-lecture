package com.thomasgassmann.pprog.mpi;

import mpi.MPI;

import java.util.Arrays;
import java.util.Random;

public class MpiFactors {

    private static int[] getData(int size) {
        var random = new Random(42);
        int[] res = new int[size];
        for (int i = 0; i < res.length; i++) {
            res[i] = random.nextInt();
            System.out.println(res[i]);
        }

        return res;
    }

    public static void calculate(String[] args) {
        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        int[] data = getData(2 * size);
        int[] initialData = Arrays.stream(data).toArray();

        int partSize = data.length / size;
        int[] res = new int[partSize];

        for (int i = rank * partSize, j = 0; j < res.length; i++, j++) {
            res[j] = data[i] * 2;
        }

        MPI.COMM_WORLD.Allgather(res, 0, partSize, MPI.INT, data, 0, partSize, MPI.INT);

        MPI.Finalize();

        if (rank == 0) {
            for (int i = 0; i < data.length; i++) {
                System.out.println(data[i]);
                assert data[i] == 2 * initialData[i];
            }
        }
    }
}
