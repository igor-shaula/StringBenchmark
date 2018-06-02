package com.igor.shaula.string_benchmark;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.log_wrappers.double_args_logger.DAL;
import com.igor.shaula.string_benchmark.log_wrappers.superior_logger.SLInt;
import com.igor.shaula.string_benchmark.log_wrappers.superior_logger.SLVoid;
import com.igor.shaula.string_benchmark.log_wrappers.var_args_logger_1_safe.VAL1;
import com.igor.shaula.string_benchmark.log_wrappers.var_args_logger_2_configurable.VAL2;
import com.igor.shaula.string_benchmark.log_wrappers.var_args_logger_3_objects.VAL3;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "06-11-2017", purpose = "" +
        "simplest way of performing heavy jobs queue on the separate thread")

public class TestingIntentService extends IntentService {

    private static final String CN = "TestingIntentService";

    // placing variables here avoids creation of those in each test's loop iteration \\
    @SuppressWarnings("FieldCanBeLocal")
    private long logNanoTime,
            salNanoTime,
            dalNanoTime,
            val0NanoTime, val1NanoTime, val2NanoTime, val3NanoTime,
            soutNanoTime;

    public TestingIntentService() {
        super(CN);
    }

    public static void prepareTheBurdenForTest(@NonNull Context context,
                                               final String basicString,
                                               final int count) {
        // count is assured to be > 0 - by this method's invocation condition \\
        context.startService(new Intent(context, TestingIntentService.class)
                .setAction(C.Intent.ACTION_START_BURDEN_PREPARATION)
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
            case C.Intent.ACTION_START_BURDEN_PREPARATION:
                final String basicString = intent.getStringExtra(C.Intent.NAME_BASIC_STRING);
                final int howManyStrings = intent.getIntExtra(C.Intent.NAME_COUNT, 0);
                prepareInitialBurden(basicString, howManyStrings);
                L.w(CN, "onHandleIntent ` basicString = " + basicString);
                L.w(CN, "onHandleIntent ` howManyStrings = " + howManyStrings);
                break;
            case C.Intent.ACTION_START_ALL_TESTS:
                final int howManyIterations = intent.getIntExtra(C.Intent.NAME_ITERATIONS, 1);
                measurePerformanceInLoop(howManyIterations);
                L.w(CN, "onHandleIntent ` howManyIterations = " + howManyIterations);
                break;
            default:
                L.w(CN, "onHandleIntent ` unknown intentAction: " + intentAction);
        }
    }

    @Override
    public void onDestroy() {
        L.d(CN, "onDestroy");
        sendInfoToUI(C.Choice.DESTROYED, -1);
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

    @MeDoc("this is launched in the worker thread only, here we assume that count is always > 0")
    private void prepareInitialBurden(@Nullable String stringExtra, int count) {
        if (count <= 0) {
            L.w(CN, "prepareInitialBurden ` count <= 0" + count);
            sendInfoToUI(C.Choice.PREPARATION, -1);
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
        ((DataTransport) getApplication()).setLongStringForTest(longStringForTest);

        sendInfoToUI(C.Choice.PREPARATION, nanoTimeDelta);

        L.v(CN, "prepareInitialBurden = " + longStringForTest);
    }

    private void sendInfoToUI(int whichWay, long nanoTimeDelta) {
        final Intent intent;
        switch (whichWay) {
            case C.Choice.PREPARATION:
                intent = new Intent(C.Intent.ACTION_GET_PREPARATION_RESULT)
                        .putExtra(C.Intent.NAME_PREPARATION_TIME, nanoTimeDelta);
                break;
            case C.Choice.DESTROYED:
                intent = new Intent(C.Intent.ACTION_ON_SERVICE_STOPPED);
                break;
            default:
                intent = null;
        }
        if (intent != null) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private void measurePerformanceInLoop(final int howManyIterations) {

        final DataTransport appLink = (DataTransport) getApplication();
        // longStringForTest may be null - but it's normally processed by all our logging variants \\
        final String longStringForTest = appLink.getLongStringForTest();
        final List<Long> oneIterationResults = new ArrayList<>(C.Order.VARIANTS_TOTAL);

        for (int i = 0; i < howManyIterations; i++) {
            if (appLink.isMarkedForStop()) {
                L.i(CN, "measurePerformanceInLoop ` isMarkedForStop worked -> stopping now");
                break;
            }
            oneIterationResults.clear();
            // trying to exclude strange numbers for the first test method by pre-heating it \\
            runSoutMethod(longStringForTest);
            // TODO: 02.06.2018 try to avoid hardcoded indexes by using auto-incremented counter \\
            oneIterationResults.add(C.Order.INDEX_OF_SOUT, runSoutMethod(longStringForTest));
            // pre-heating standard Android's Log to avoid it's slowing down for the first time \\
            runLogMethod(longStringForTest);
            oneIterationResults.add(C.Order.INDEX_OF_LOG, runLogMethod(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_DAL, runDalMethod(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_VAL_1, runVal1Method(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_VAL_2, runVal2Method(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_VAL_3, runVal3Method(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_SL_VOID, runSLVoidMethod(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_SL_INT, runSLIntMethod(longStringForTest));
/*
            // as this part of code is hot - no need of debug logging here during normal usage \\
            L.w("measurePerformanceInLoop", "i = " + i +
                    " oneIterationResults = " + oneIterationResults);
*/
            appLink.transportOneIterationsResult(oneIterationResults);
//            System.gc(); // i guess this might cause crash - NullPointerException: Attempt to read from null array in sumForSout += array[C.Order.INDEX_OF_SOUT];
        }
        // for experiment's clarity it's better to initiate garbage-collector before the next step \\
        System.gc();
    }

    private long runSoutMethod(@Nullable String longStringForTest) {
        soutNanoTime = System.nanoTime();
        System.out.println(longStringForTest);
        return System.nanoTime() - soutNanoTime;
    }

    private long runLogMethod(@Nullable String longStringForTest) {
        logNanoTime = System.nanoTime();
        Log.v(CN, longStringForTest);
        return System.nanoTime() - logNanoTime;
    }

    private long runDalMethod(@Nullable String longStringForTest) {
        dalNanoTime = System.nanoTime();
        DAL.v(CN, longStringForTest);
        return System.nanoTime() - dalNanoTime;
    }

    private long runVal1Method(@Nullable String longStringForTest) {
        val1NanoTime = System.nanoTime();
        VAL1.v(CN, longStringForTest);
        return System.nanoTime() - val1NanoTime;
    }

    private long runVal2Method(@Nullable String longStringForTest) {
        val2NanoTime = System.nanoTime();
        VAL2.v(CN, longStringForTest);
        return System.nanoTime() - val2NanoTime;
    }

    private long runVal3Method(@Nullable String longStringForTest) {
        val3NanoTime = System.nanoTime();
        VAL3.v(CN, longStringForTest);
        return System.nanoTime() - val3NanoTime;
    }

    private long runSLVoidMethod(@Nullable String longStringForTest) {
        salNanoTime = System.nanoTime();
        SLVoid.v(CN, longStringForTest);
        return System.nanoTime() - salNanoTime;
    }

    private long runSLIntMethod(@Nullable String longStringForTest) {
        val0NanoTime = System.nanoTime();
        SLInt.v(CN, longStringForTest);
        return System.nanoTime() - val0NanoTime;
    }

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