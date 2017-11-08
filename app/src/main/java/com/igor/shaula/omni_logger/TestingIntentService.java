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
import com.igor.shaula.omni_logger.double_args_log_wrapper.DAL;
import com.igor.shaula.omni_logger.single_arg_log_wrapper.SAL;
import com.igor.shaula.omni_logger.utils.C;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "06-11-2017", purpose = "" +
        "simplest way of performing heavy jobs queue on the separate thread")

public class TestingIntentService extends IntentService {

    private static final String CN = "TestingIntentService";

    @NonNull
    private String longStringForTest = "";

    public TestingIntentService() {
        super(CN);
    }

    public static void prepareTheBurdenForTest(@NonNull Context context, int count) {
        // count is assured to be > 0 - by this method's invocation condition \\
        context.startService(new Intent(context, TestingIntentService.class)
                .setAction(C.Intent.ACTION_START_BURDEN_PREPARATION)
                .putExtra(C.Intent.NAME_COUNT, count));
    }

    public static void launchAllMeasurements(@NonNull Context context) {
        context.startService(new Intent(context, TestingIntentService.class)
                .setAction(C.Intent.ACTION_START_ALL_TESTS));
    }

    // SYSTEM CALLBACKS ============================================================================

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
                startPerformanceAppraisal(10);
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
    }

    @Override
    public void onLowMemory() {
        Log.d(CN, "onLowMemory");
        super.onLowMemory();
    }

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

    // PAYLOAD =====================================================================================

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

        long nanoTime = System.nanoTime();
        System.out.println("before preparing the string for logger @ " + nanoTime);
        final StringBuilder longStringForTestBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            for (String string : initialStringSource) {
                longStringForTestBuilder.append(string);
            }
        }
        longStringForTest = longStringForTestBuilder.toString();
//        final String longStringForTest = longStringForTestBuilder.toString();
        long nanoTimeDelta = System.nanoTime() - nanoTime;
        System.out.println("after preparing the string for logger @ " + nanoTimeDelta);

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
            case C.Choice.TEST_SYSTEM_LOG:
                intent = new Intent(C.Intent.ACTION_GET_SYSTEM_LOG_TEST_RESULT)
                        .putExtra(C.Intent.NAME_SYSTEM_LOG_TIME, nanoTimeDelta);
                break;
            case C.Choice.TEST_SAL:
                intent = new Intent(C.Intent.ACTION_GET_SAL_TEST_RESULT)
                        .putExtra(C.Intent.NAME_SAL_TIME, nanoTimeDelta);
                break;
            case C.Choice.TEST_DAL:
                intent = new Intent(C.Intent.ACTION_GET_DAL_TEST_RESULT)
                        .putExtra(C.Intent.NAME_DAL_TIME, nanoTimeDelta);
                break;
            case C.Choice.TEST_VAL:
                intent = new Intent(C.Intent.ACTION_GET_VAL_TEST_RESULT)
                        .putExtra(C.Intent.NAME_VAL_TIME, nanoTimeDelta);
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

    // measure results after making this method synchronized \\
    private void startPerformanceAppraisal(final int howManyTimesToDoLogging) {
        // standard system Log performance test \\
        {
            final long nanoTime = System.nanoTime();
            System.out.println("starting standard logger @ " + nanoTime);
            for (int i = 0; i < howManyTimesToDoLogging; i++) {
                Log.v(CN, longStringForTest);
            }
            long nanoTimeDelta = System.nanoTime() - nanoTime;
            System.out.println("ending standard logger @ " + nanoTimeDelta);
            sendInfoToUI(C.Choice.TEST_SYSTEM_LOG, nanoTimeDelta);
        }
        // single-arg Log wrapper performance test \\
        {
            final long nanoTime = System.nanoTime();
            System.out.println("starting SAL @ " + nanoTime);
            for (int i = 0; i < howManyTimesToDoLogging; i++) {
                SAL.v(longStringForTest);
            }
            long nanoTimeDelta = System.nanoTime() - nanoTime;
            System.out.println("ending SAL @ " + nanoTimeDelta);
            sendInfoToUI(C.Choice.TEST_SAL, nanoTimeDelta);
        }
        // double-args Log wrapper performance test \\
        {
            final long nanoTime = System.nanoTime();
            System.out.println("starting standard logger @ " + nanoTime);
            for (int i = 0; i < howManyTimesToDoLogging; i++) {
                DAL.v(CN, longStringForTest);
            }
            long nanoTimeDelta = System.nanoTime() - nanoTime;
            System.out.println("ending standard logger @ " + nanoTimeDelta);
            sendInfoToUI(C.Choice.TEST_DAL, nanoTimeDelta);
        }
    }
}