package com.thomasgassmann.pprog.mpi;

import mpi.MPI;

import java.awt.event.WindowFocusListener;
import java.util.Random;

public class Waterfall {
    public static void waterfall(String[] args) {
        MPI.Init(args);

        Random random = new Random();
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        int[] sbuf = new int[1];
        if (size == 1) {
            MPI.Finalize();
            return;
        }

        if (rank == 0) {
            sbuf[0] = random.nextInt(1000);
            sbuf[0]++;
            System.out.println("On process " + rank + " with buf: " + sbuf[0]);
            MPI.COMM_WORLD.Send(sbuf, 0, 1, MPI.INT, rank + 1, MPI.ANY_TAG);
        } else if (rank == size - 1) {
            MPI.COMM_WORLD.Recv(sbuf, 0, 1, MPI.INT, rank - 1, MPI.ANY_TAG);
            sbuf[0]++;
            System.out.println("On process " + rank + " with buf: " + sbuf[0]);
        } else {
            MPI.COMM_WORLD.Recv(sbuf, 0, 1, MPI.INT, rank - 1, MPI.ANY_TAG);
            sbuf[0]++;
            System.out.println("On process " + rank + " with buf: " + sbuf[0]);
            MPI.COMM_WORLD.Send(sbuf, 0, 1, MPI.INT, rank + 1, MPI.ANY_TAG);
        }

        MPI.Finalize();
    }
}
