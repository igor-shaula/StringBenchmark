package com.igor.shaula.benchmark.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UTest {

    // createReadableStringForTime =================================================================

    @Test
    public void createReadableString_0() {
        assertEquals("0", U.createReadableStringForTime(0));
    }

    @Test
    public void createReadableString_9() {
        assertEquals("9", U.createReadableStringForTime(9));
    }

    @Test
    public void createReadableString_99() {
        assertEquals("99", U.createReadableStringForTime(99));
    }

    @Test
    public void createReadableString_999() {
        assertEquals("999", U.createReadableStringForTime(999));
    }

    @Test
    public void createReadableString_9000() {
        assertEquals("9,000", U.createReadableStringForTime(9000));
    }

    @Test
    public void createReadableString_99000() {
        assertEquals("99,000", U.createReadableStringForTime(99000));
    }

    @Test
    public void createReadableString_999000() {
        assertEquals("999,000", U.createReadableStringForTime(999000));
    }

    @Test
    public void createReadableString_9990009() {
        assertEquals("9,990.009", U.createReadableStringForTime(9990009));
    }

    @Test
    public void createReadableString_99900099() {
        assertEquals("99,900.099", U.createReadableStringForTime(99900099));
    }

    @Test
    public void createReadableString_999000999() {
        assertEquals("999,000.999", U.createReadableStringForTime(999000999));
    }

    @Test
    public void createReadableString_9990009990() {
        assertEquals("9,990.009.990", U.createReadableStringForTime(9990009990L));
    }

    @Test
    public void createReadableString_99900099900() {
        assertEquals("99,900.099.900", U.createReadableStringForTime(99900099900L));
    }

    @Test
    public void createReadableString_999000999000() {
        assertEquals("999,000.999.000", U.createReadableStringForTime(999000999000L));
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

    // createReadableStringForLong =================================================================

    @Test
    public void createReadableStringForLong_0() {
        assertEquals("0", U.createReadableStringForLong(0));
    }

    @Test
    public void createReadableStringForLong_9() {
        assertEquals("9", U.createReadableStringForLong(9));
    }

    @Test
    public void createReadableStringForLong_99() {
        assertEquals("99", U.createReadableStringForLong(99));
    }

    @Test
    public void createReadableStringForLong_999() {
        assertEquals("999", U.createReadableStringForLong(999));
    }

    @Test
    public void createReadableStringForLong_9000() {
        assertEquals("9.000", U.createReadableStringForLong(9000));
    }

    @Test
    public void createReadableStringForLong_99000() {
        assertEquals("99.000", U.createReadableStringForLong(99000));
    }

    @Test
    public void createReadableStringForLong_999000() {
        assertEquals("999.000", U.createReadableStringForLong(999000));
    }

    @Test
    public void createReadableStringForLong_9990009() {
        assertEquals("9.990.009", U.createReadableStringForLong(9990009));
    }

    @Test
    public void createReadableStringForLong_99900099() {
        assertEquals("99.900.099", U.createReadableStringForLong(99900099));
    }

    @Test
    public void createReadableStringForLong_999000999() {
        assertEquals("999.000.999", U.createReadableStringForLong(999000999));
    }

    @Test
    public void createReadableStringForLong_9990009990() {
        assertEquals("9.990.009.990", U.createReadableStringForLong(9990009990L));
    }

    @Test
    public void createReadableStringForLong_99900099900() {
        assertEquals("99.900.099.900", U.createReadableStringForLong(99900099900L));
    }

    @Test
    public void createReadableStringForLong_999000999000() {
        assertEquals("999.000.999.000", U.createReadableStringForLong(999000999000L));
    }

    // defineSeparatorsCount =======================================================================

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

    // reduceStartingZeroes ========================================================================

    @Test
    public void reduceStartingZeroes_() {
        assertEquals("", U.reduceStartingZeroes(""));
    }

    @Test
    public void reduceStartingZeroes_0() {
        assertEquals("", U.reduceStartingZeroes("0"));
    }

    @Test
    public void reduceStartingZeroes_00() {
        assertEquals("", U.reduceStartingZeroes("00"));
    }

    @Test
    public void reduceStartingZeroes_000() {
        assertEquals("0", U.reduceStartingZeroes("000"));
    }

    @Test
    public void reduceStartingZeroes_not() {
        assertEquals("-", U.reduceStartingZeroes("-"));
    }

    @Test
    public void reduceStartingZeroes_not0() {
        assertEquals("-0", U.reduceStartingZeroes("-0"));
    }

    @Test
    public void reduceStartingZeroes_not00() {
        assertEquals("-00", U.reduceStartingZeroes("-00"));
    }

    @Test
    public void reduceStartingZeroes_0not() {
        assertEquals("-", U.reduceStartingZeroes("0-"));
    }

    @Test
    public void reduceStartingZeroes_00not() {
        assertEquals("-", U.reduceStartingZeroes("00-"));
    }
}