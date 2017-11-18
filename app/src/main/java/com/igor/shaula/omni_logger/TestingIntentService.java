package com.igor.shaula.omni_logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.MeDoc;
import com.igor.shaula.omni_logger.annotations.TypeDoc;
import com.igor.shaula.omni_logger.log_wrappers.double_args_logger.DAL;
import com.igor.shaula.omni_logger.log_wrappers.single_arg_logger.SAL;
import com.igor.shaula.omni_logger.log_wrappers.var_args_logger.VAL;
import com.igor.shaula.omni_logger.utils.C;
import com.igor.shaula.omni_logger.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "06-11-2017", purpose = "" +
        "simplest way of performing heavy jobs queue on the separate thread")

public class TestingIntentService extends IntentService {

    private static final String CN = "TestingIntentService";

    // placing variables here avoids creation of those in each test's loop iteration \\
    @SuppressWarnings("FieldCanBeLocal")
    private long logNanoTime, salNanoTime, dalNanoTime, valNanoTime, soutNanoTime;

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

    // PAYLOAD =====================================================================================

    @MeDoc("this is launched in the worker thread only, here we assume that count is always > 0")
    private void prepareInitialBurden(@Nullable String stringExtra, int count) {
        if (count <= 0) {
            L.w(CN, "prepareInitialBurden ` count <= 0" + count);
            sendInfoToUI(C.Choice.PREPARATION, -1);
            return;
        }
//        final String[] initialStringSource = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        // for now it's decided to leave this String array here, not moving it into constants \\

        // as StringBuilder is a part of String creation process - it has to be counted in time \\
        final long nanoTime = System.nanoTime();
        final StringBuilder longStringForTestBuilder = new StringBuilder(stringExtra);
        for (int i = 1; i < count; i++) { // initial string with i = 0 is already in StringBuilder \\
            longStringForTestBuilder.append(stringExtra);
//            for (String string : initialStringSource) {
//                longStringForTestBuilder.append(string);
//            }
        }
        final String longStringForTest = longStringForTestBuilder.toString();
        final long nanoTimeDelta = System.nanoTime() - nanoTime;

        // saving this created string into the App for the case if IntentService gets destroyed early \\
        ((App) getApplication()).setLongStringForTest(longStringForTest);

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

        final App appLink = (App) getApplication();
        // longStringForTest may be null - but it's normally processed by all our logging variants \\
        final String longStringForTest = appLink.getLongStringForTest();
        final List<Long> oneIterationResults = new ArrayList<>(C.Order.VARIANTS_TOTAL);

        for (int i = 0; i < howManyIterations; i++) {
            oneIterationResults.clear();
            oneIterationResults.add(C.Order.INDEX_OF_LOG, runLogMethod(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_SAL, runSalMethod(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_DAL, runDalMethod(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_VAL, runValMethod(longStringForTest));
            oneIterationResults.add(C.Order.INDEX_OF_SOUT, runSoutMethod(longStringForTest));
/*
            // as this part of code is hot - no need of debug logging here during normal usage \\
            L.w("measurePerformanceInLoop", "i = " + i +
                    " oneIterationResults = " + oneIterationResults);
//                    " oneIterationResults = " + Arrays.toString(oneIterationResults));
*/
            appLink.transportOneIterationsResult(oneIterationResults);
//            sendInfoToUI(oneIterationResults, i);
        }
        // for experiment's clarity it's better to initiate garbage-collector before the next step \\
        System.gc();
    }

    private long runLogMethod(@Nullable String longStringForTest) {
        // measuring standard Android's Log time \\
        logNanoTime = System.nanoTime();
        Log.v("StandardAndroidLogTag", longStringForTest);
        return System.nanoTime() - logNanoTime;
    }

    private long runSalMethod(@Nullable String longStringForTest) {
        // measuring SingleArgLogger's time \\
        salNanoTime = System.nanoTime();
        SAL.v(longStringForTest);
        return System.nanoTime() - salNanoTime;
    }

    private long runDalMethod(@Nullable String longStringForTest) {
        // measuring DoubleArgsLogger's time \\
        dalNanoTime = System.nanoTime();
        DAL.v(CN, longStringForTest);
        return System.nanoTime() - dalNanoTime;
    }

    private long runValMethod(@Nullable String longStringForTest) {
        // measuring VariableArgsLogger's time \\
        valNanoTime = System.nanoTime();
        VAL.v(CN, longStringForTest);
        return System.nanoTime() - valNanoTime;
    }

    private long runSoutMethod(@Nullable String longStringForTest) {
        // measuring standard Android's Log time \\
        soutNanoTime = System.nanoTime();
        System.out.println(longStringForTest);
        return System.nanoTime() - soutNanoTime;
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