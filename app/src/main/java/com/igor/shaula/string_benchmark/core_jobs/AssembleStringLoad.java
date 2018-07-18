package com.igor.shaula.string_benchmark.core_jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.utils.L;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "")
public final class AssembleStringLoad {

    private static final String CN = "AssembleStringLoad";

    @MeDoc("this is launched in the worker thread only, here we assume that count is always > 0")
    public void prepareStartingLoad(@Nullable String stringBasis, int count,
                                    final @NonNull DataTransport dataTransport) {
        if (count < 0) {
            L.w(CN, "prepareStartingLoad ` wrong count value = " + count);
            dataTransport.notifyStarterThatLoadIsAssembled(-1);
        } else if (count == 0) {
            assembleEmptyLoad(dataTransport);
        } else if (count == 1) {
            assembleHeavyLoad(stringBasis, dataTransport);
        } else {
            assembleHeavyLoad(stringBasis, count, dataTransport);
        }
    }

    @MeDoc("no need to use StringBuilder here - we're just passing empty string further in the chain")
    private void assembleEmptyLoad(@NonNull DataTransport dataTransport) {

        final long nanoTime = System.nanoTime();
        final String emptyStringForTest = "";
        final long nanoTimeDelta = System.nanoTime() - nanoTime;

        dataTransport.setLongStringForTest(emptyStringForTest);
        dataTransport.notifyStarterThatLoadIsAssembled(nanoTimeDelta);
    }

    private void assembleHeavyLoad(@Nullable String stringBasis, @NonNull DataTransport dataTransport) {
        // no need to use StringBuilder or create a new String - because we already have stringBasis \\
        dataTransport.setLongStringForTest(stringBasis);
        // as we haven't created a new String instance - hence there is nothing to measure here \\
        dataTransport.notifyStarterThatLoadIsAssembled(0);

        L.v(CN, "assembleHeavyLoad = " + stringBasis);
    }

    @MeDoc("count is meant to be > 1 here")
    private void assembleHeavyLoad(@Nullable String heavyLoadBasis, int count,
                                   @NonNull DataTransport dataTransport) {
        // as StringBuilder is a part of String creation process - it has to be counted in time \\
        final long nanoTime = System.nanoTime();
        final StringBuilder longStringForTestBuilder = new StringBuilder(heavyLoadBasis);
        for (int i = 1; i < count; i++) { // initial string with i = 0 is already in StringBuilder \\
            longStringForTestBuilder.append(heavyLoadBasis);
        }
        final String longStringForTest = longStringForTestBuilder.toString();
        final long nanoTimeDelta = System.nanoTime() - nanoTime;

        // saving this created string into the App for the case if IntentService gets destroyed early \\
        dataTransport.setLongStringForTest(longStringForTest);
        dataTransport.notifyStarterThatLoadIsAssembled(nanoTimeDelta);

        L.v(CN, "assembleHeavyLoad = " + longStringForTest);
    }
}