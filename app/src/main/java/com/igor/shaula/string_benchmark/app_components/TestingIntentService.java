package com.igor.shaula.string_benchmark.app_components;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.core_jobs.AssembleStringLoad;
import com.igor.shaula.string_benchmark.core_jobs.DataTransport;
import com.igor.shaula.string_benchmark.core_jobs.IterationsMeasurement;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;

import java.util.Arrays;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "06-11-2017", purpose = "" +
        "simplest way of performing heavy jobs queue on the separate thread")

public final class TestingIntentService extends IntentService {

    private static final String CN = "TestingIntentService";

    public TestingIntentService() {
        super(CN);
    }

    public static void prepareTheLoadForTest(@NonNull Context context,
                                             final @NonNull String basicString,
                                             final int count) {
        // count is assured to be > 0 - by this method's invocation condition \\
        context.startService(new Intent(context, TestingIntentService.class)
                .setAction(C.Intent.ACTION_START_LOAD_PREPARATION)
                .putExtra(C.Intent.NAME_BASIC_STRING, basicString)
                .putExtra(C.Intent.NAME_COUNT, count)
        );
    }

    public static void launchAllMeasurements(@NonNull Context context, int howManyIterations) {
        context.startService(new Intent(context, TestingIntentService.class)
                .setAction(C.Intent.ACTION_START_ALL_TESTS)
                .putExtra(C.Intent.NAME_ITERATIONS, howManyIterations)
        );
    }

    // SYSTEM CALLBACKS ============================================================================

    @Override
    public void onCreate() {
        L.d(CN, "onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        L.d(CN, "onStart ` intent = " + intent);
        L.d(CN, "onStart ` startId = " + startId);
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        L.d(CN, "onStartCommand ` intent = " + intent);
        L.d(CN, "onStartCommand ` flags = " + flags);
        L.d(CN, "onStartCommand ` startId = " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        L.d(CN, "onHandleIntent ` intent = " + intent);
        if (intent == null) {
            L.w(CN, "onHandleIntent ` intent is null");
            return;
        }
        final String intentAction = intent.getAction();
        if (intentAction == null) {
            L.w(CN, "onHandleIntent ` intentAction is null");
            return;
        }
        switch (intentAction) {
            case C.Intent.ACTION_START_LOAD_PREPARATION:
                final String basicString = intent.getStringExtra(C.Intent.NAME_BASIC_STRING);
                final int howManyStrings = intent.getIntExtra(C.Intent.NAME_COUNT, 0);
                new AssembleStringLoad().prepareStartingLoad(
                        basicString, howManyStrings, (DataTransport) getApplication());
                L.w(CN, "onHandleIntent ` basicString = " + basicString);
                L.w(CN, "onHandleIntent ` howManyStrings = " + howManyStrings);
                break;
            case C.Intent.ACTION_START_ALL_TESTS:
                final int howManyIterations = intent.getIntExtra(C.Intent.NAME_ITERATIONS, 1);
                new IterationsMeasurement().measurePerformanceInLoop(
                        (DataTransport) getApplication(), howManyIterations);
                L.w(CN, "onHandleIntent ` howManyIterations = " + howManyIterations);
                break;
            default:
                L.w(CN, "onHandleIntent ` unknown intentAction: " + intentAction);
        }
    }

    @Override
    public void onDestroy() {
        L.d(CN, "onDestroy");
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(C.Intent.ACTION_ON_SERVICE_STOPPED));
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        L.d(CN, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        L.d(CN, "onTrimMemory ` level = " + level);
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        L.d(CN, "onConfigurationChanged ` newConfig = " + newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        L.d(CN, "onTaskRemoved ` rootIntent = " + rootIntent);
        super.onTaskRemoved(rootIntent);
    }

    // PRIVATE PAYLOAD =============================================================================

    @SuppressWarnings("unused")
    @MeDoc("actually iterative passing data via intent works very strange - we cannot rely on it")
    private void sendInfoToUI(@NonNull long[] oneIterationResults, int i) {
        L.w("sendInfoToUI", "i = " + i +
                " oneIterationResults = " + Arrays.toString(oneIterationResults));
        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(C.Intent.ACTION_GET_ONE_ITERATION_RESULTS)
                        .putExtra(C.Intent.NAME_ALL_TIME, oneIterationResults)
                        .putExtra(C.Intent.NAME_ITERATION_NUMBER, i)
        );
    }
}