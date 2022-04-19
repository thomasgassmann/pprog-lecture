package com.thomasgassmann.pprog.algorithms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrefixSumTests {
    @Test
    public void checkPrefixSum() {
        int[] values = new int[] { 6, 4, 16, 10, 16, 14, 2, 8 };

        int[] res = PrefixSum.prefixSum(values);

        Assertions.assertArrayEquals(new int[] { 6, 10, 26, 36, 52, 66, 68, 76 }, res);
    }
}
