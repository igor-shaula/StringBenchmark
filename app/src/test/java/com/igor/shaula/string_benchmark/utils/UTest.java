package com.igor.shaula.string_benchmark.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UTest {

    @Test
    public void adaptForUser() {
        assertEquals(4, 2 + 2);
    }

    // getReadableNumber ---------------------------------------------------------------------------

    @Test
    public void getReadableNumber0() {
        assertEquals("0", U.getReadableNumber(0));
    }

    @Test
    public void getReadableNumber999() {
        assertEquals("999", U.getReadableNumber(999));
    }

    @Test
    public void getReadableNumber1000() {
        assertEquals("1" + C.COMMA + "000", U.getReadableNumber(1_000));
    }

    @Test
    public void getReadableNumber999000() {
        assertEquals("999" + C.COMMA + "000", U.getReadableNumber(999_000));
    }

    @Test
    public void getReadableNumber999000555() {
        assertEquals("999" + C.COMMA + "000" + C.DOT + "555", U.getReadableNumber(999_000_555));
    }
    // getNumberOfSeparatorsForNumber --------------------------------------------------------------

    @Test
    public void getNumberOfSeparatorsForNumber0() {
        assertEquals(0, U.getNumberOfSeparatorsForNumber(0));
    }

    @Test
    public void getNumberOfSeparatorsForNumber1() {
        assertEquals(0, U.getNumberOfSeparatorsForNumber(1));
    }

    @Test
    public void getNumberOfSeparatorsForNumber10() {
        assertEquals(0, U.getNumberOfSeparatorsForNumber(10));
    }

    @Test
    public void getNumberOfSeparatorsForNumber100() {
        assertEquals(0, U.getNumberOfSeparatorsForNumber(100));
    }

    @Test
    public void getNumberOfSeparatorsForNumber1000() {
        assertEquals(1, U.getNumberOfSeparatorsForNumber(1_000));
    }

    @Test
    public void getNumberOfSeparatorsForNumberMillion() {
        assertEquals(2, U.getNumberOfSeparatorsForNumber(1_000_000));
    }

    @Test
    public void getNumberOfSeparatorsForNumberMillionAlt() {
        assertNotEquals(1, U.getNumberOfSeparatorsForNumber(1_000_000));
    }
}