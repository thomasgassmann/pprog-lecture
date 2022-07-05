#!/bin/bash

export MPJ_HOME=./mpi
export PATH=$MPJ_HOME/bin:$PATH
./mpi/bin/mpjrun.sh -np 8 ./out/artifacts/pprog_lecture_jar/pprog-lecture.jar

