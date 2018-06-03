package com.igor.shaula.string_benchmark;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

import java.util.List;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "13-11-2017", purpose = "" +
        "fastest & easiest way of preserving the burden from being destroyed with IntentService")

public final class App extends Application implements DataTransport {

    private static final String CN = "App";

    private boolean isAllowedToRunIterations;
    @Nullable
    private IterationResultConsumer linkToIterationResultConsumer;
    @Nullable
    private String longStringForTest = ""; // ma be rather long & heavy \\

    // LIFECYCLE ===================================================================================

    @Override
    public void onCreate() {
        // the only method calling to super where logging is placed after super-method's invocation \\
        super.onCreate();
        L.restore(); // initial flag state mey be anything from code or possible previous launches \\
        L.w(CN, "onCreate");
        L.silence(); // for avoiding mess in logs in all further places \\
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        L.restore();
        L.w(CN, "onConfigurationChanged ` newConfig = " + newConfig);
        L.silence();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTrimMemory(int level) {
        L.restore();
        final String levelExplanation;
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                levelExplanation = "TRIM_MEMORY_BACKGROUND";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                levelExplanation = "TRIM_MEMORY_COMPLETE";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                levelExplanation = "TRIM_MEMORY_MODERATE";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                levelExplanation = "TRIM_MEMORY_RUNNING_CRITICAL";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                levelExplanation = "TRIM_MEMORY_RUNNING_LOW";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                levelExplanation = "TRIM_MEMORY_RUNNING_MODERATE";
                break;
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                levelExplanation = "TRIM_MEMORY_UI_HIDDEN";
                break;
            default:
                levelExplanation = "undefined";
        }
        L.w(CN, "onTrimMemory ` level = " + level + " = " + levelExplanation);
        L.silence();
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        L.restore();
        L.w(CN, "onLowMemory");
        L.silence();
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        L.restore();
        L.w(CN, "onTerminate");
        L.silence();
        super.onTerminate();
    }

    // GETTERS & SETTERS ===========================================================================

    @Override
    @Nullable
    public String getLongStringForTest() {
        return longStringForTest;
    }

    @Override
    public boolean isAllowedToRunIterations() {
        return isAllowedToRunIterations;
    }

    @Override
    public void allowIterationsJob(boolean isAllowedToRunIterations) {
        this.isAllowedToRunIterations = isAllowedToRunIterations;
    }

    @Override
    public void setDataConsumer(@Nullable IterationResultConsumer iterationResultConsumer) {
        this.linkToIterationResultConsumer = iterationResultConsumer;
    }

    @Override
    @MeDoc("invoked from working IntentService as for now")
    public void setLongStringForTest(@Nullable String longStringForTest) {
        this.longStringForTest = longStringForTest;
    }

    @Override
    @MeDoc("invoked from working IntentService as for now")
    public void transportOneIterationsResult(@NonNull List<Long> oneIterationsResult,
                                             int currentIterationNumber) {
        if (linkToIterationResultConsumer != null) {
            linkToIterationResultConsumer.onNewIterationResult(
                    U.convertIntoArray(oneIterationsResult), currentIterationNumber);
            // not used oneIterationsResult.toArray(new Long[] {}); for avoiding Long-long conversion \\
        }
    }
}