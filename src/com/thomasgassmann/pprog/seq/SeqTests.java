package com.thomasgassmann.pprog.seq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class SeqTests {
    @Test
    public void check() {
        Random r = new Random(42);
        int[] a = new int[400];
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(0, 10);
        }

        var c = LongestCommonSequence.longestCommonSequence(a);
        var e = LongestCommonSequenceMulti.longestCommonSequence(a, 4);

        Assertions.assertEquals(c, e);
    }
}
