package com.thomasgassmann.pprog.mpi;

import mpi.MPI;

public class MpiPi {
    public static void calculate(String[] args, int numSteps) {
        MPI.Init(args);
        // declare and initialize variables (sum=0 etc.)
        int sum = 0;
        double h = 1 / (double)numSteps;
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        for (int i = rank; i < numSteps; i = i + size) {
            double x = (i + 0.5) * h;
            sum += 4.0 / (1.0 + x * x);
        }

        if (rank != 0) {
            double[] sendBuf = new double[]{sum};
            // 1-element array containing sum
            MPI.COMM_WORLD.Send(sendBuf, 0, 1, MPI.DOUBLE, 0, 10);
        } else { // rank == 0
            double[] recvBuf = new double[1];
            for (int src = 1; src < size; src++) {
                MPI.COMM_WORLD.Recv(recvBuf, 0, 1, MPI.DOUBLE, src, 10);
                sum += recvBuf[0];
            }
        }

        double pi = h * sum; // output pi at rank 0 only!
        MPI.Finalize();

        if (rank == 0) {
            System.out.println(pi);
        }
    }
}
