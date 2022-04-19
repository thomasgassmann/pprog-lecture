package com.thomasgassmann.pprog.algorithms;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class PrefixSum {
    public static int[] prefixSum(int[] input) {
        var forkJoinPool = new ForkJoinPool();
        var root = new Range();
        root.rangeFrom = 0;
        root.rangeTo = input.length - 1;

        forkJoinPool.invoke(new SumTask(input, root));

        int[] output = new int[input.length];
        forkJoinPool.invoke(new PrefixSumTask(input, output, root));

        return output;
    }

    private static class Range {
        private Range left;
        private Range right;
        private int rangeFrom;
        private int rangeTo;
        private int sum;
        private int fromLeft;
    }

    private static class PrefixSumTask extends RecursiveAction {
        private int[] _input;
        private int[] _output;
        private Range _root;

        public PrefixSumTask(int[] input, int[] output, Range root) {
            _input = input;
            _output = output;
            _root = root;
        }

        @Override
        protected void compute() {
            if (_root.left == null && _root.right == null && _root.rangeTo == _root.rangeFrom) {
                _output[_root.rangeFrom] = _root.fromLeft + _input[_root.rangeFrom];
                return;
            }

            assert _root.left != null && _root.right != null;

            _root.left.fromLeft = _root.fromLeft;
            _root.right.fromLeft = _root.fromLeft + _root.left.sum;

            var left = new PrefixSumTask(_input, _output, _root.left);
            var right = new PrefixSumTask(_input, _output, _root.right);

            left.fork();
            right.fork();
            left.join();
            right.join();
        }
    }

    private static class SumTask extends RecursiveAction {
        private int[] _input;
        private Range _root;

        public SumTask(int[] input, Range root) {
            _input = input;
            _root = root;
        }

        @Override
        protected void compute() {
            if (_root.rangeTo - _root.rangeFrom == 0) {
                _root.sum = _input[_root.rangeFrom];
                return;
            }

            int split = (_root.rangeTo + _root.rangeFrom) / 2;

            _root.left = new Range();
            _root.right = new Range();
            _root.left.rangeFrom = _root.rangeFrom;
            _root.left.rangeTo = split;
            _root.right.rangeFrom = split + 1;
            _root.right.rangeTo = _root.rangeTo;

            var leftTask = new SumTask(_input, _root.left);
            var rightTask = new SumTask(_input, _root.right);

            leftTask.fork();
            rightTask.fork();
            rightTask.join();
            leftTask.join();

            _root.sum = _root.left.sum + _root.right.sum;
        }
    }
}
