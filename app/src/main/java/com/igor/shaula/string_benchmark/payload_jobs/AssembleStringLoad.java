package com.igor.shaula.string_benchmark.payload_jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.utils.L;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "")
public class AssembleStringLoad {

    private static final String CN = "AssembleStringLoad";

    @MeDoc("this is launched in the worker thread only, here we assume that count is always > 0")
    public void prepareStartingLoad(@Nullable String stringExtra, int count,
                                    final @NonNull DataTransport dataTransport) {
        if (count <= 0) {
            L.w(CN, "prepareStartingLoad ` count <= 0" + count);
            dataTransport.notifyStarterThatLoadIsAssembled(-1);
            return;
        }
        // as StringBuilder is a part of String creation process - it has to be counted in time \\
        final long nanoTime = System.nanoTime();
        final StringBuilder longStringForTestBuilder = new StringBuilder(stringExtra);
        for (int i = 1; i < count; i++) { // initial string with i = 0 is already in StringBuilder \\
            longStringForTestBuilder.append(stringExtra);
        }
        final String longStringForTest = longStringForTestBuilder.toString();
        final long nanoTimeDelta = System.nanoTime() - nanoTime;

        // saving this created string into the App for the case if IntentService gets destroyed early \\
        dataTransport.setLongStringForTest(longStringForTest);

        dataTransport.notifyStarterThatLoadIsAssembled(nanoTimeDelta);

        L.v(CN, "prepareStartingLoad = " + longStringForTest);
    }
}