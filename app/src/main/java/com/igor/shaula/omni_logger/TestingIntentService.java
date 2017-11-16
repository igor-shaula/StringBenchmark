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

@TypeDoc(createdBy = "Igor Shaula", createdOn = "06-11-2017", purpose = "" +
        "simplest way of performing heavy jobs queue on the separate thread")

public class TestingIntentService extends IntentService {

    private static final String CN = "TestingIntentService";

    @SuppressWarnings("FieldCanBeLocal")
    private long logNanoTime, salNanoTime, dalNanoTime, valNanoTime, soutNanoTime; // volatile ???

    // SYSTEM CALLBACKS ============================================================================

    public TestingIntentService() {
        super(CN);
    }

    public static void prepareTheBurdenForTest(@NonNull Context context, final int count) {
        // count is assured to be > 0 - by this method's invocation condition \\
        context.startService(new Intent(context, TestingIntentService.class)
                .setAction(C.Intent.ACTION_START_BURDEN_PREPARATION)
                .putExtra(C.Intent.NAME_COUNT, count));
    }

    public static void launchAllMeasurements(@NonNull Context context) {
        context.startService(new Intent(context, TestingIntentService.class)
                .setAction(C.Intent.ACTION_START_ALL_TESTS));
    }

    @Override
    public void onCreate() {
        Log.d(CN, "onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d(CN, "onStart ` intent = " + intent);
        Log.d(CN, "onStart ` startId = " + startId);
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(CN, "onStartCommand ` intent = " + intent);
        Log.d(CN, "onStartCommand ` flags = " + flags);
        Log.d(CN, "onStartCommand ` startId = " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(CN, "onHandleIntent ` intent = " + intent);
        if (intent == null) {
            Log.w(CN, "onHandleIntent ` intent is null");
            return;
        }
        final String intentAction = intent.getAction();
        if (intentAction == null) {
            Log.w(CN, "onHandleIntent ` intentAction is null");
            return;
        }
        switch (intentAction) {
            case C.Intent.ACTION_START_BURDEN_PREPARATION:
                prepareInitialBurden(intent.getIntExtra(C.Intent.NAME_COUNT, 0));
                break;
            case C.Intent.ACTION_START_ALL_TESTS:
                measurePerformanceInLoop(intent.getIntExtra(C.Intent.NAME_ITERATIONS, 1));
                break;
            default:
                Log.w(CN, "onHandleIntent ` unknown intentAction: " + intentAction);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(CN, "onDestroy");
        sendInfoToUI(C.Choice.DESTROYED, -1);
        super.onDestroy();
        // TODO: 13.11.2017 also make service stopping at once the stop button was pressed in UI \\
    }

    @Override
    public void onLowMemory() {
        Log.d(CN, "onLowMemory");
        super.onLowMemory();
    }

    // PAYLOAD =====================================================================================

    @Override
    public void onTrimMemory(int level) {
        Log.d(CN, "onTrimMemory ` level = " + level);
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(CN, "onConfigurationChanged ` newConfig = " + newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(CN, "onTaskRemoved ` rootIntent = " + rootIntent);
        super.onTaskRemoved(rootIntent);
    }

    @MeDoc("this is launched in the worker thread only, here we assume that count is always > 0")
    private void prepareInitialBurden(int count) {
        if (count <= 0) {
            Log.w(CN, "prepareInitialBurden ` count <= 0" + count);
            sendInfoToUI(C.Choice.PREPARATION, -1);
            return;
        }
        // TODO: 06.11.2017 later add the ability to set initial String roaster by the user itself \\
        final String[] initialStringSource = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        // for now it's decided to leave this String array here, not moving it into constants \\

        // as StringBuilder is a part of String creation process - it has to be counted in time \\
        final long nanoTime = System.nanoTime();
        final StringBuilder longStringForTestBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            for (String string : initialStringSource) {
                longStringForTestBuilder.append(string);
            }
        }
        final String longStringForTest = longStringForTestBuilder.toString();
        final long nanoTimeDelta = System.nanoTime() - nanoTime;

        // saving this created string into the App for the case if IntentService gets destroyed early \\
        ((App) getApplication()).setLongStringForTest(longStringForTest);

        sendInfoToUI(C.Choice.PREPARATION, nanoTimeDelta);

        Log.v(CN, "prepareInitialBurden = " + longStringForTest);
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

    private void measurePerformanceInLoop(final int numberOfIterationsForAllVariants) {

        final long[] oneIterationResults = new long[5];
        final String longStringForTest = ((App) getApplication()).getLongStringForTest();
        // longStringForTest may be null - but it's normally processed by all logging variants \\

        for (int i = 0; i < numberOfIterationsForAllVariants; i++) {

            oneIterationResults[0] = runLogMethod(longStringForTest);
            oneIterationResults[1] = runSalMethod(longStringForTest);
            oneIterationResults[2] = runDalMethod(longStringForTest);
            oneIterationResults[3] = runValMethod(longStringForTest);
            oneIterationResults[4] = runSoutMethod(longStringForTest);

            sendInfoToUI(oneIterationResults, i);
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

    private void sendInfoToUI(@NonNull long[] oneIterationResults, int i) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(
                new Intent(C.Intent.ACTION_GET_ONE_ITERATION_RESULTS)
                        .putExtra(C.Intent.NAME_ALL_TIME, oneIterationResults)
                        .putExtra(C.Intent.NAME_ITERATION_NUMBER, i)
        );
    }
}