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

    private boolean backWasPressedOnce;
    private boolean isBurdenPreparationJobRunning;
    private boolean isIterationsJobRunning;
    private boolean isBurdenReady;

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
        uiLink.setInitialInputFieldsValues();
    }

    // FROM LogicLink ==============================================================================

    @MeDoc("used only in uiLink to sync the bViewBurden availability state")
    @Override
    public boolean isBurdenReady() {
        return isBurdenReady;
    }

    @Override
    public void toggleBurdenPreparationJobState(boolean isRunning) {
        isBurdenPreparationJobRunning = isRunning;
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
    public void onBasicStringChanged() {
        final int basicStringLength = uiLink.getBasicStringText().length();
        // 1 \\
        if (basicStringLength == 0) {
            uiLink.updateBasicStringHint(systemLink.findStringById(R.string.testBasisHintEmpty));
        } else {
            final String testBasisHint = systemLink.findStringById(R.string.testBasisHintBusy)
                    + C.SPACE + U.createReadableStringForLong(basicStringLength);
            uiLink.updateBasicStringHint(testBasisHint);
        }
        // 2 \\
        final int stringsAmountHint = U.convertIntoInt(uiLink.getStringsAmountText());
        final String altStringRepetitionsHint =
                systemLink.findStringById(R.string.stringsAmountHintBusy) + C.SPACE +
                        U.createReadableStringForLong(stringsAmountHint * basicStringLength);
        uiLink.updateStringsAmountHint(altStringRepetitionsHint);
        // 3 \\
        uiLink.resetResultViewStates();
        uiLink.resetResultOfPreparation();
    }

    @Override
    public void onStringsAmountChanged() {
        // safely parsing here - because inputType is number in layout \\
        final int stringsAmountHint = U.convertIntoInt(uiLink.getStringsAmountText());
        if (stringsAmountHint == 0) {
            uiLink.updateStringsAmountHint(systemLink.findStringById(R.string.stringsAmountHintEmpty));
        } else {
            final String stringRepetitionsHint =
                    systemLink.findStringById(R.string.stringsAmountHintBusy) + C.SPACE +
                            U.createReadableStringForLong(
                                    stringsAmountHint * uiLink.getBasicStringText().length()
                            );
            uiLink.updateStringsAmountHint(stringRepetitionsHint);
        }
        uiLink.resetResultViewStates();
        uiLink.resetResultOfPreparation();
    }

    @Override
    public void onIterationsAmountChanged() {
        final int iterationsAmount = U.convertIntoInt(uiLink.getIterationsAmountText());
        if (iterationsAmount == 0) {
            uiLink.updateIterationAmountHint(systemLink.findStringById(R.string.iterationsAmountHintEmpty));
        } else {
            uiLink.updateIterationAmountHint(systemLink.findStringById(R.string.iterationsAmountHintBusy));
        }
        uiLink.resetResultViewStates();
/*
                no need to reset shown value of tvResultOfPreparation here because
                testing loop iterations number has no effect on burden creation time \\
*/
    }

    @Override
    public void onPrepareBurdenClick() {
        if (isBurdenPreparationJobRunning) {
            stopCurrentBurdenPreparationJob();
        } else {
            startNewBurdenPreparationJob();
        }
    }

    @Override
    public void onViewBurdenClick() {
        final String burden = systemLink.getBurden();
        if (isBurdenReady) {
//            if (burden.length() > 1000) {
//                uiLink.setBusy(true);
//            } else {
            uiLink.toggleViewBurdenBusyStateOnMainThread(false);
            uiLink.showBurdenInDialog(burden);
//            }
        }
    }

    @Override
    public void onToggleIterationsClick() {
        if (isIterationsJobRunning) {
            stopCurrentIterationsJob();
        } else {
            startIterationsJob();
        }
    }

    @Override
    public void showDialogWithBuildInfo() {
        uiLink.showBuildInfoDialog();
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
//            uiLink.updatePreparationsResultOnMainThread(oneIterationResults);
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
        uiLink.updatePreparationsResultOnMainThread(results);
    }

    // PRIVATE =====================================================================================

    private void stopCurrentBurdenPreparationJob() {
        interruptPerformanceTest();
        toggleBurdenPreparationJobState(false);
    }

    private void startNewBurdenPreparationJob() {
        isBurdenReady = false;
        runTestBurdenPreparation();
        toggleBurdenPreparationJobState(true);
        uiLink.resetResultViewStates();
    }

    private void runTestBurdenPreparation() {
        int count = U.convertIntoInt(uiLink.getStringsAmountText());
        if (count > 0) {
            systemLink.launchPreparation(uiLink.getBasicStringText(), count);
            pendingPreparationResult = "";
            showTextyTwister();
        }
        L.d(CN, "runTestBurdenPreparation() finished");
        // TODO: 02.12.2017 wright unit-tests with this kind of content \\
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

    private void stopCurrentIterationsJob() {
        interruptPerformanceTest();
        toggleIterationsJobState(false);
    }

    @Override
    public void toggleIterationsJobState(boolean isRunning) {
        isIterationsJobRunning = isRunning;
        uiLink.toggleJobActiveUiState(isRunning);
    }

    private void startIterationsJob() {
        totalResultList.clear();
        int count = U.convertIntoInt(uiLink.getIterationsAmountText());
        // condition in the main loop will work only for count > 0 but any numbers are safe there \\
        if (count > 0) {
            systemLink.launchAllMeasurements(count);
        }
        L.d(CN, "startIterationsJob() finished");
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
        isBurdenReady = true;
        uiLink.toggleViewBurdenBusyStateOnMainThread(true);
    }

    private void stopTwisterTimer() {
        if (twisterTimer != null) {
            twisterTimer.cancel(); // purge() behaves very strangely - so i decided to avoid it
        }
        twisterTimer = null;
        counter = 0;
    }
}