package com.igor.shaula.string_benchmark.payload_jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.app_components.main_screen.for_ui.OneIterationResultModel;

import java.util.List;
import java.util.Map;

public interface DataTransport { // currently implemented by App as a quickest decision \\

    @NonNull
    List<OneIterationResultModel> getIterationResultList();

    @Nullable
    String getLongStringForTest();

    boolean isAllowedToRunIterations();

    void allowIterationsJob(boolean isAllowedToRunIterations);

    void setDataConsumer(@Nullable IterationResultConsumer iterationResultConsumer);

    void setLongStringForTest(@Nullable String longStringForTest);

    void notifyStarterThatLoadIsAssembled(long nanoTimeDelta);

    void transportOneIterationsResult(@NonNull Map<String, Long> oneIterationsResult, int currentIterationNumber);

    void stopIterations();

    interface IterationResultConsumer { // implemented by LogicLink \\

        void onNewIterationResult(@NonNull Map<String, Long> oneIterationsResult, int currentIterationNumber);

        void prepareForNewJob();
    }
}