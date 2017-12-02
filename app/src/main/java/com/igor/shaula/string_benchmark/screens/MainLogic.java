package com.igor.shaula.string_benchmark.screens;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.DataTransport;
import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "25-11-2017", purpose = "",
        comment = "logic should not have any Android-specific imports & dependencies - only pure Java")

public final class MainLogic implements MainHub.LogicLink, DataTransport.IterationResultConsumer {

    private static final String CN = "MainLogic";

    private static final char[] CHARS = {'-', '\\', '|', '/', '-'};

    @NonNull
    private final MainHub.SystemLink systemLink;
    @NonNull
    private final MainHub.UiLink uiLink;
    @NonNull
    private final DataTransport dataTransport;

    private boolean isJobRunning;
    private boolean backWasPressedOnce;

    private int counter;
    @NonNull
    private List<long[]> totalResultList = new LinkedList<>();

    @NonNull
    private String pendingPreparationResult = "";

    @Nullable
    private Timer twisterTimer;

    MainLogic(@NonNull MainHub.SystemLink systemLink,
              @NonNull MainHub.UiLink uiLink,
              @NonNull DataTransport dataTransport) {
        this.systemLink = systemLink;
        this.uiLink = uiLink;
        this.dataTransport = dataTransport;
        uiLink.setLogicLink(this);
        dataTransport.setDataConsumer(this); // register for receiving portions of result \\
        uiLink.init();
    }

    // FROM LogicLink ==============================================================================

    @Override
    public void toggleJobState(boolean isRunning) {
        isJobRunning = isRunning;
        uiLink.toggleJobActiveUiState(isRunning);
    }

    @Override
    public void onBackPressed() {
        if (backWasPressedOnce) {
            // stopping service & clearing used resources \\
            interruptPerformanceTest();
            systemLink.finishItself();
            U.immediatelyDisableToast();
        } else {
            uiLink.informUser(C.Choice.TOAST, R.string.pressBackAgainToExit, 0);
            backWasPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backWasPressedOnce = false;
                }
            }, C.Delay.EXIT_WITH_BACK_MILLIS);
        }
    }

    @Override
    public void onFabClick() {
        if (isJobRunning) {
            stopCurrentJob();
        } else {
            startNewJob();
        }
    }

    @Override
    public void prepareMainJob() {
        int count;
        try {
            count = Integer.parseInt(uiLink.getIterationsAmount());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            count = 0;
        }
        // condition in the main loop will work only for count > 0 but any numbers are safe there \\
        totalResultList.clear();
        systemLink.launchAllMeasurements(count);
        L.d(CN, "prepareMainJob() finished");
    }

    @Override
    public void showPreparationsResult(int whatInfoToShow, long resultNanoTime) {
        L.d("showPreparationsResult", "whatInfoToShow = " + whatInfoToShow);
        L.d("showPreparationsResult", "resultNanoTime = " + resultNanoTime);
        switch (whatInfoToShow) {
            case C.Choice.PREPARATION:
                stopTwisterTimer();
                pendingPreparationResult = systemLink.getAdaptedString(resultNanoTime);
                updatePreparationResult("");
                break;
            case C.Choice.TEST_SYSTEM_LOG:
                uiLink.updateResultForLog(resultNanoTime);
                break;
            case C.Choice.TEST_SAL:
                uiLink.updateResultForSAL(resultNanoTime);
                break;
            case C.Choice.TEST_DAL:
                uiLink.updateResultForDAL(resultNanoTime);
                break;
            case C.Choice.TEST_VAL:
                uiLink.updateResultForVAL(resultNanoTime);
                break;
            default:
                L.w(CN, "selectInfoToShow ` unknown whatInfoToShow = " + whatInfoToShow);
        }
    }

//    private void showPreparationsResult(@Nullable long[] oneIterationResults) {
//        if (oneIterationResults != null && oneIterationResults.length == C.Order.VARIANTS_TOTAL) {
//            uiLink.showPreparationsResultOnMainThread(oneIterationResults);
//        }
//    }

    @MeDoc("invoked in activity's onStop")
    @Override
    public void unLinkDataTransport() {
        dataTransport.setDataConsumer(null); // preventing possible memory leak here \\
    }

    @Override
    public void interruptPerformanceTest() {
        systemLink.stopTestingService();
    }

    // FROM IterationResultConsumer ================================================================

    @Override
    public void onNewIterationResult(@NonNull long[] oneIterationsResult) {
        L.w("onNewIterationResult",
                " oneIterationsResult = " + Arrays.toString(oneIterationsResult));
        totalResultList.add(oneIterationsResult);
        final long[] results = calculateMedianResult();
        uiLink.showPreparationsResultOnMainThread(results);
    }

    // PRIVATE =====================================================================================

    private void stopCurrentJob() {
        interruptPerformanceTest();
        toggleJobState(false);
    }

    private void startNewJob() {
        runTestBurdenPreparation();
        toggleJobState(true);
        uiLink.restoreResultViewStates();
    }

    private void runTestBurdenPreparation() {
        final String basicString = uiLink.getBasicString();
        int count = 0;
        try {
            count = Integer.parseInt(uiLink.getRepetitionsCount());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if (count > 0) {
            systemLink.launchPreparation(basicString, count);
            pendingPreparationResult = "";
            showTextyTwister();
        }
        L.d(CN, "runTestBurdenPreparation() finished");
/*
                    VAL.v("" + getString(R.string.vero_test).length());
                    VAL.v("", "");
                    VAL.v("", "", "");
                    VAL.v();
                    VAL.v((String[]) null);
                    VAL.v(null, null);
                    VAL.v(null, null, null);
                    VAL.v(this.toString(), null, null);
                    VAL.v("1");
                    VAL.v("1", "2");
                    VAL.v("1", "2", "3");
                    VAL.v("", "", "", "");
*/
    }

    private void showTextyTwister() {
        final int[] index = new int[1];
        final String[] textForUI = new String[1];
        final TimerTask twisterTask = new TimerTask() {
            @Override
            public void run() {
                // 0 - 1 - 2 - 3 - 0 - 1 - 2 - 3 - 0 - ...
                index[0] = counter % CHARS.length;
                textForUI[0] = String.valueOf(CHARS[index[0]]);
                updatePreparationResult(textForUI[0]);
            }
        };
        twisterTimer = new Timer(true);
        twisterTimer.schedule(twisterTask, 0, 80);
    }

    @NonNull
    private long[] calculateMedianResult() {
        final int listSize = totalResultList.size();
        L.w("calculateMedianResult", "listSize = " + listSize);
        final long[] medianArray = new long[C.Order.VARIANTS_TOTAL];
        if (totalResultList.isEmpty()) { // anyway we should not fall inside this check \\
            // avoiding division by zero in the loop just after this check \\
            return medianArray; // empty here \\
        }
        long sumForLog = 0;
        long sumForSAL = 0;
        long sumForDAL = 0;
        long sumForVAL = 0;
        long sumForSout = 0;
        for (long[] array : totalResultList) {
            L.w("calculateMedianResult", "" + Arrays.toString(array));
            // i hope we'll avoid exceeding the max value for type long \\
            sumForLog += array[C.Order.INDEX_OF_LOG];
            sumForSAL += array[C.Order.INDEX_OF_SAL];
            sumForDAL += array[C.Order.INDEX_OF_DAL];
            sumForVAL += array[C.Order.INDEX_OF_VAL];
            sumForSout += array[C.Order.INDEX_OF_SOUT];
        }
        medianArray[C.Order.INDEX_OF_LOG] = sumForLog / listSize;
        medianArray[C.Order.INDEX_OF_SAL] = sumForSAL / listSize;
        medianArray[C.Order.INDEX_OF_DAL] = sumForDAL / listSize;
        medianArray[C.Order.INDEX_OF_VAL] = sumForVAL / listSize;
        medianArray[C.Order.INDEX_OF_SOUT] = sumForSout / listSize;

        return medianArray;
    }

    private void updatePreparationResult(@NonNull final String result) {
        if (pendingPreparationResult.isEmpty()) {
            uiLink.updatePreparationResultOnMainThread(result);
            counter++;
        } else {
            uiLink.updatePreparationResultOnMainThread(pendingPreparationResult);
        }
    }

    private void stopTwisterTimer() {
        if (twisterTimer != null) {
            twisterTimer.cancel(); // purge() behaves very strangely - so i decided to avoid it
        }
        twisterTimer = null;
        counter = 0;
    }
}