package com.igor.shaula.string_benchmark;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface DataTransport {

    void setDataConsumer(@Nullable App.Consumer consumer);

    @Nullable
    String getLongStringForTest();

    interface Consumer { // implemented by MainActivity \\

        void onNewIterationResult(@NonNull long[] oneIterationsResult);

//        void onNewIterationResult(@NonNull List<Long> oneIterationsResult);
    }
}