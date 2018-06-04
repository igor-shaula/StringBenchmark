package com.igor.shaula.string_benchmark.job_core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface DataTransport { // currently implemented by App as a quickest decision \\

    @Nullable
    String getLongStringForTest();

    boolean isAllowedToRunIterations();

    void allowIterationsJob(boolean isAllowedToRunIterations);

    void setDataConsumer(@Nullable IterationResultConsumer iterationResultConsumer);

    void setLongStringForTest(@Nullable String longStringForTest);

    void notifyStarterThatLoadIsAssembled(long nanoTimeDelta);

    void transportOneIterationsResult(@NonNull List<Long> oneIterationsResult, int currentIterationNumber);

    void stopIterations();

    interface IterationResultConsumer { // implemented by LogicLink \\

        void onNewIterationResult(@NonNull long[] oneIterationsResult, int currentIterationNumber);

        void prepareForNewJob();
    }
}