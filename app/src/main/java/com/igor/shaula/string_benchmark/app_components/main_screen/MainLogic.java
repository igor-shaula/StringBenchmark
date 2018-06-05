package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.log_wrappers.superior_logger.SLInt;
import com.igor.shaula.string_benchmark.payload_jobs.DataTransport;
import com.igor.shaula.string_benchmark.payload_jobs.IterationResultConsumer;
import com.igor.shaula.string_benchmark.payload_jobs.TextyTwister;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

import org.json.JSONObject;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "25-11-2017", purpose = "",
        comment = "logic should not have any Android-specific imports & dependencies - only pure Java")

public final class MainLogic implements MainHub.LogicLink {

    private static final String CN = "MainLogic";

    private boolean backWasPressedOnce;
    private boolean isLoadPreparationJobRunning;
    private boolean isIterationsJobRunning;
    private boolean isLoadReady;
    private boolean isPrefsFragmentShownHere;

    @NonNull
    private String pendingPreparationResult = "";

    @NonNull
    private final MainHub.SystemLink systemLink;
    @NonNull
    private final MainHub.UiLink uiLink;
    @NonNull
    private final DataTransport dataTransport;
    @NonNull
    private final DataTransport.IterationResultConsumer resultConsumer;
    @NonNull
    private TextyTwister textyTwister;

    MainLogic(@NonNull MainHub.SystemLink systemLink,
              @NonNull MainHub.UiLink uiLink,
              @NonNull DataTransport dataTransport) {
        this.systemLink = systemLink;
        this.uiLink = uiLink;
        this.dataTransport = dataTransport;
        uiLink.setLogicLink(this);
        uiLink.init();
        uiLink.setInitialInputFieldsValues();
        resultConsumer = new IterationResultConsumer(this);
        dataTransport.setDataConsumer(resultConsumer); // register for receiving portions of result \\
        textyTwister = new TextyTwister();
    }

    // FROM LogicLink ==============================================================================

    @MeDoc("used only in uiLink to sync the bViewLoad availability state")
    @Override
    public boolean isLoadReady() {
        return isLoadReady;
    }

    @Override
    public boolean isPrefsFragmentShownHere() {
        return isPrefsFragmentShownHere;
    }

    @Override
    public void toggleLoadPreparationJobState(boolean isRunning) {
        isLoadPreparationJobRunning = isRunning;
        uiLink.toggleJobActiveUiState(isRunning);
    }

    @Override
    public void toggleIterationsJobState(boolean isRunning) {
        isIterationsJobRunning = isRunning;
        uiLink.toggleJobActiveUiState(isRunning);
        if (isRunning) {
            textyTwister.showTextyTwister(this);
        } else {
            textyTwister.stopTwisterTimer();
        }
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
    public void onPrepareLoadClick() {
        if (isLoadPreparationJobRunning) {
            stopCurrentLoadPreparationJob();
        } else {
            startNewLoadPreparationJob();
        }
        // the following is temporary placed here \\
        doSingleTesting();
    }

    private void stopCurrentLoadPreparationJob() {
        interruptPerformanceTest();
        toggleLoadPreparationJobState(false);
    }

    private void startNewLoadPreparationJob() {
        isLoadReady = false;
        runTestLoadPreparation();
        toggleLoadPreparationJobState(true);
    }

    private void runTestLoadPreparation() {
        int count = U.convertIntoInt(uiLink.getStringsAmountText());
        if (count > 0) {
            systemLink.launchPreparation(uiLink.getBasicStringText(), count);
            pendingPreparationResult = "";
        }
        L.d(CN, "runTestLoadPreparation() finished");
    }

    private void doSingleTesting() {
        // TODO: 02.12.2017 wright unit-tests with this kind of content \\
        SLInt.v("");
        SLInt.v("", "");
        SLInt.v("", "", "");
        SLInt.v();
        SLInt.v((String[]) null);
        SLInt.v(null, null);
        SLInt.v(null, null, null);
        SLInt.v(this.toString(), null, null);
        SLInt.v("1");
        SLInt.v("1", "2");
        SLInt.v("1", "2", "3");
        SLInt.v("", "", "", "");
        SLInt.v("the fact of posting the expression itself"); // just logging the statement here \\
        SLInt.isV("expression-2", "result");
        SLInt.setConnector(" IS ");
        SLInt.isV(new JSONObject(), "json result");
        SLInt.pV("single p");
        SLInt.pV(null);
        SLInt.o(null);
        SLInt.pV(null, null);
        SLInt.pV("multiple ps", "_+_");
    }

    @Override
    public void onViewLoadClick() {
        final String load = systemLink.getLoad();
        if (isLoadReady) {
            uiLink.toggleViewLoadBusyStateOnMainThread(false);
            uiLink.showLoadInDialog(load);
        }
    }

    @Override
    public void onToggleIterationsClick(boolean isEndless) {
        if (isIterationsJobRunning) {
            systemLink.allowIterationsJob(false);
            // IntentService will stop shortly after this \\
        } else {
            systemLink.allowIterationsJob(true);
            startIterationsJob(isEndless);
        }
    }

    private void startIterationsJob(boolean isEndless) {
        resultConsumer.prepareForNewJob();
        int count;
        if (isEndless) {
            count = Integer.MAX_VALUE;
        } else {
            count = U.convertIntoInt(uiLink.getIterationsAmountText());
        }
        // condition in the main loop will work only for count > 0 but any numbers are safe there \\
        if (count > 0) {
            systemLink.launchAllMeasurements(count);
            toggleIterationsJobState(true);
            uiLink.showTotalIterationsNumber(count);
        }
        L.d(CN, "startIterationsJob() finished");
    }

    @Override
    public void showDialogWithBuildInfo() {
        uiLink.showBuildInfoDialog();
    }

    @Override
    public void togglePrefsFragmentHere() {
        if (isPrefsFragmentShownHere) {
            systemLink.togglePrefsFragment(false);
            isPrefsFragmentShownHere = false;
        } else {
            systemLink.togglePrefsFragment(true);
            isPrefsFragmentShownHere = true;
        }
    }

    @Override
    public void showPreparationsResult(int whatInfoToShow, long resultNanoTime) {
        L.d("showPreparationsResult", "whatInfoToShow = " + whatInfoToShow);
        L.d("showPreparationsResult", "resultNanoTime = " + resultNanoTime);
        switch (whatInfoToShow) {
            case C.Choice.PREPARATION:
                pendingPreparationResult = systemLink.getAdaptedString(resultNanoTime);
                updatePreparationResult("");
                break;
            case C.Choice.TEST_SYSTEM_LOG:
                uiLink.updateResultForLog(resultNanoTime);
                break;
            case C.Choice.TEST_DAL:
                uiLink.updateResultForDAL(resultNanoTime);
                break;
            case C.Choice.TEST_VAL_1:
                uiLink.updateResultForVAL1(resultNanoTime);
                break;
            case C.Choice.TEST_VAL_2:
                uiLink.updateResultForVAL2(resultNanoTime);
                break;
            case C.Choice.TEST_VAL_3:
                uiLink.updateResultForVAL3(resultNanoTime);
                break;
            case C.Choice.TEST_SL_VOID:
                uiLink.updateResultForSLVoid(resultNanoTime);
                break;
            case C.Choice.TEST_SL_INT:
                uiLink.updateResultForSLInt(resultNanoTime);
                break;
            default:
                L.w(CN, "selectInfoToShow ` unknown whatInfoToShow = " + whatInfoToShow);
        }
    }

    @Override
    public void updatePreparationResult(@NonNull final String result) {
        if (pendingPreparationResult.isEmpty()) {
            uiLink.updatePreparationResultOnMainThread(result);
//            twisterCounter++;
        } else {
            uiLink.updatePreparationResultOnMainThread(pendingPreparationResult);
        }
        isLoadReady = true;
        uiLink.toggleViewLoadBusyStateOnMainThread(true);
        uiLink.updateLoadLengthOnMainThread(systemLink.getLoad().length());
    }

//    private void showPreparationsResult(@Nullable long[] oneIterationResults) {
//        if (oneIterationResults != null && oneIterationResults.length == C.Order.VARIANTS_TOTAL) {
//            uiLink.transportIterationsResult(oneIterationResults);
//        }
//    }

    @MeDoc("invoked in activity's onStop")
    @Override
    public void linkDataTransport() {
        dataTransport.setDataConsumer(resultConsumer); // for avoiding lost link after app was restored \\
    }

    @MeDoc("invoked in activity's onStop")
    @Override
    public void unLinkDataTransport() {
        dataTransport.setDataConsumer(null); // preventing possible memory leak here \\
    }

    @Override
    public void interruptPerformanceTest() {
        systemLink.stopTestingService();
    }

    @Override
    public void transportIterationsResult(@NonNull long[] results, int currentIterationNumber) {
        uiLink.updateIterationsResultOnMainThread(results, currentIterationNumber);
    }
}