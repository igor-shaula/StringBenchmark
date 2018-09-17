package com.igor.shaula.benchmark.logic_engine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor_shaula.payload_for_test.threads.SwitchingThreads;

import java.util.Map;

public interface DataTransport extends SwitchingThreads.Callback { // currently implemented by App as a quickest decision \\
    
    @Nullable
    String getLongStringForTest();
    
    boolean isForbiddenToRunIterations();
    
    void forbidIterationsJob(boolean isForbiddenToRunIterations);
    
    void setDataConsumer(@Nullable IterationResultConsumer iterationResultConsumer);
    
    void setLongStringForTest(@Nullable String longStringForTest);
    
    void notifyStarterThatLoadIsAssembled(long nanoTimeDelta);
    
    void transportOneIterationsResult(@NonNull Map<String, Long> oneIterationsResult, int currentIterationNumber);
    
    void stopIterations();
}