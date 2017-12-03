package com.igor.shaula.string_benchmark.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UTest {

    @Test
    public void adaptForUser() {
        assertEquals(4, 2 + 2);
    }

    // createReadableString ------------------------------------------------------------------------

    @Test
    public void createReadableString_0() {
        assertEquals("0", U.createReadableString(0));
    }

    @Test
    public void createReadableString_999() {
        assertEquals("999", U.createReadableString(999));
    }

    @Test
    public void createReadableString_1000() {
        assertEquals("1" + C.COMMA + "000", U.createReadableString(1_000));
    }

    @Test
    public void createReadableString_999000() {
        assertEquals("999" + C.COMMA + "000", U.createReadableString(999_000));
    }

    @Test
    public void createReadableString_999000555() {
        assertEquals("999" + C.COMMA + "000" + C.DOT + "555", U.createReadableString(999_000_555));
    }

    // defineSeparatorsCount -----------------------------------------------------------------------

    @Test
    public void defineSeparatorsCount_0() {
        assertEquals(0, U.defineSeparatorsCount(0));
    }

    @Test
    public void defineSeparatorsCount_1() {
        assertEquals(0, U.defineSeparatorsCount(1));
    }

    @Test
    public void defineSeparatorsCount_10() {
        assertEquals(0, U.defineSeparatorsCount(10));
    }

    @Test
    public void defineSeparatorsCount_100() {
        assertEquals(0, U.defineSeparatorsCount(100));
    }

    @Test
    public void defineSeparatorsCount_1000() {
        assertEquals(1, U.defineSeparatorsCount(1_000));
    }

    @Test
    public void defineSeparatorsCount_Million() {
        assertEquals(2, U.defineSeparatorsCount(1_000_000));
    }

    @Test
    public void defineSeparatorsCount_MillionAlt() {
        assertNotEquals(1, U.defineSeparatorsCount(1_000_000));
    }

    // replaceFirstDotWithComma ====================================================================

    @Test
    public void replaceFirstDotWithComma_0() {
        assertEquals("0", U.replaceFirstDotWithComma("0"));
    }

    @Test
    public void replaceFirstDotWithComma_dot() {
        assertEquals(",", U.replaceFirstDotWithComma("."));
    }

    @Test
    public void replaceFirstDotWithComma_dotDot() {
        assertEquals(",.", U.replaceFirstDotWithComma(".."));
    }

    @Test
    public void replaceFirstDotWithComma_space() {
        assertEquals(",", U.replaceFirstDotWithComma(" "));
    }

    @Test
    public void replaceFirstDotWithComma_dot0() {
        assertEquals(",0", U.replaceFirstDotWithComma(".0"));
    }

    @Test
    public void replaceFirstDotWithComma_0dot() {
        assertEquals("0,", U.replaceFirstDotWithComma("0."));
    }

    @Test
    public void replaceFirstDotWithComma_00() {
        assertEquals("00", U.replaceFirstDotWithComma("00"));
    }

    @Test
    public void replaceFirstDotWithComma_0dot0() {
        assertEquals("0,0", U.replaceFirstDotWithComma("0.0"));
    }

    @Test
    public void replaceFirstDotWithComma_0aSymbol0() {
        assertEquals("0,0", U.replaceFirstDotWithComma("0-0"));
    }

    @Test
    public void replaceFirstDotWithComma_0dot0dot0() {
        assertEquals("0,0.0", U.replaceFirstDotWithComma("0.0.0"));
    }

    @Test
    public void replaceFirstDotWithComma_0aSymbol0dot0() {
        assertEquals("0,0.0", U.replaceFirstDotWithComma("0-0.0"));
    }
}