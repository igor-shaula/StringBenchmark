package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.app_components.main_screen.for_ui.OneIterationResultModel;
import com.igor.shaula.string_benchmark.log_wrappers.superior_logger.SLInt;
import com.igor.shaula.string_benchmark.core_jobs.AssembleStringLoad;
import com.igor.shaula.string_benchmark.core_jobs.DataTransport;
import com.igor.shaula.string_benchmark.core_jobs.IterationResultConsumer;
import com.igor.shaula.string_benchmark.core_jobs.TextyTwister;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

import org.json.JSONObject;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "25-11-2017", purpose = "",
        comment = "logic should not have any Android-specific imports & dependencies - only pure Java")

public final class MainLogic implements MainHub.LogicLink {

    private static final String CN = "MainLogic";

    private boolean backWasPressedOnce;
    private boolean isIterationsJobRunning;
    private boolean shouldShowExplanations;
    private boolean isAppBarLayoutFullyExpanded;

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

    @NonNull
    @Override
    public List<OneIterationResultModel> getIterationResultList() {
        return dataTransport.getIterationResultList();
    }

    @NonNull
    @Override
    public Map<String, Long> getInitialEmptyMap() {
        return dataTransport.getInitialEmptyMap();
    }

    @Override
    public boolean isPreparationBlockShown() {
        return isAppBarLayoutFullyExpanded;
    }

    @Override
    public void setAppBarLayoutFullyExpanded(boolean isFullyExpanded) {
        isAppBarLayoutFullyExpanded = isFullyExpanded;
//        systemLink.toggleAppBarExpansionIcon(isFullyExpanded);
        uiLink.toggleAppBarExpansionIcon(isFullyExpanded);
//        uiLink.togglePreparationTButton(isFullyExpanded);
        // TODO: 24.06.2018 break this self-locking chain of reaction on listener \\
/*        if (isFullyExpanded) {
            uiLink.clearFocusFromAllInputFields();
            uiLink.hideKeyboard();
        }*/
//        uiLink.invalidateAppBar();
    }

    @Override
    public void toggleLoadPreparationJobState(boolean isRunning) {
        uiLink.toggleJobActiveUiState(isRunning);
        if (isRunning) {
            uiLink.clearFocusFromAllInputFields();
        }
    }

    @Override
    public void toggleIterationsJobState(boolean isRunning) {
        isIterationsJobRunning = isRunning;
        uiLink.toggleJobActiveUiState(isRunning);
        if (isRunning) {
            uiLink.clearFocusFromAllInputFields();
            textyTwister.showTextyTwister(this);
        } else {
            textyTwister.stopTwisterTimer();
        }
    }

    @Override
    public void onLoadPreparationBlockAction() {
        uiLink.toggleLoadPreparationBlock(!isAppBarLayoutFullyExpanded);
        uiLink.clearFocusFromAllInputFields();
        uiLink.hideKeyboard();
    }

    @Override
    public void onToggleAllExplanationsAction() {
        uiLink.toggleAllExplanations(shouldShowExplanations);
        shouldShowExplanations = !shouldShowExplanations;
        // for upcoming changes we need already reversed flag state \\
//        systemLink.toggleMenuItemAboutExplanations(shouldShowExplanations);
        // additional workaround for preventing appearance of bottom part of expanded AppBar \\
        if (!isAppBarLayoutFullyExpanded) {
            uiLink.toggleLoadPreparationBlock(false);
        }
    }

    @Override
    public void onShowDialogWithBuildInfoAction() {
        uiLink.showBuildInfoDialog();
    }

    @Override
    public void onBackPressed() {
        if (backWasPressedOnce) {
            // stopping service & clearing used resources \\
            interruptPerformanceTest();
            systemLink.finishItself();
            U.immediatelyDisableToast();
        } else {
            uiLink.clearFocusFromAllInputFields();
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
    public void onLoadEmptyChecked(boolean isChecked) {
        if (isChecked) {
            // the next line prepares UI state for later deciding which load to prepare - empty or heavy \\
            uiLink.setStringsAmountText(0);
            // the following is temporary placed here \\
            doSingleTesting();
            return;
        }
        final int previousCount = U.convertIntoInt(uiLink.getStringsAmountText());
        if (previousCount == 0) {
            // recovering from possible previous resetLoad-click \\
            uiLink.setStringsAmountText(1);
        }
    }

    @Override
    public void onPrepareLoadClick() {
        // now going further - all expected logic branching is inside the following method \\
        runTestLoadPreparation();
    }

    private void runTestLoadPreparation() {
        uiLink.clearFocusFromAllInputFields();
        uiLink.hideKeyboard();
        // the rest is for preparing potentially heavy load \\
        int count = U.convertIntoInt(uiLink.getStringsAmountText());
        if (count < 0) {
            // first of all protecting from potentially wrong values \\
            L.w(CN, "runTestLoadPreparation ` count < 0 - this should never happen");
        } else if (count == 0) {
            // for this case there is no need to use worker thread as for creating heavy load \\
            new AssembleStringLoad().prepareStartingLoad("", 0, dataTransport);
        } else { // if (count > 0) \\
            systemLink.launchPreparation(uiLink.getBasicStringText(), count);
            pendingPreparationResult = "";
        }
        L.d(CN, "runTestLoadPreparation() finished");
    }

    @Override
    public void onViewLoadClick() {
        final String load = systemLink.getLoad();
        uiLink.showLoadInDialog(load);
    }

    @Override
    public void onToggleIterationsClick() {
        boolean isEndless = uiLink.isEndless();
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
    public void showPreparationsResult(int whatInfoToShow, long resultNanoTime) {
        L.d("showPreparationsResult", "whatInfoToShow = " + whatInfoToShow);
        L.d("showPreparationsResult", "resultNanoTime = " + resultNanoTime);
        switch (whatInfoToShow) {
            case C.Choice.PREPARATION:
                pendingPreparationResult = systemLink.getAdaptedString(resultNanoTime);
                updatePreparationResult("");
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
        uiLink.updateLoadLengthOnMainThread(systemLink.getLoad().length());
    }

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
    public void transportIterationsResult(@NonNull Map<String, Long> resultModelMap,
//    public void transportIterationsResult(@NonNull List<OneIterationResultModel> resultModelList,
                                          int currentIterationIndex) {
        uiLink.updateIterationsResultOnMainThread(resultModelMap, currentIterationIndex);
    }

    // ADDITIONAL TESTING WHILE UNIT_TESTS ARE NOT YET IMPLEMENTED =================================

    private void doSingleTesting() {
        // TODO: 02.12.2017 write unit-tests with this kind of content \\
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

        // android.os.CpuUsageInfo - practically useless \\
        // android.util.DebugUtils - useless here \\

        // TODO: 18.11.2017 use android.os.Debug.MemoryInfo \\
//        android.os.Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
//        memoryInfo.getMemoryStats()

        SLInt.isV("this thread's priority", Thread.currentThread().getPriority());

        // TODO: 18.11.2017 use android.os.Process - should investigate this more \\
        android.os.Process.setThreadPriority(0);

        Enumeration<?> properties = System.getProperties().propertyNames();
        while (properties.hasMoreElements()) {
            SLInt.isV("property", properties.nextElement());
        }
/*
    // add into info screen - from java.lang.System \ getProperties() \\
    Key                             Description of Associated Value
    java.version	                Java Runtime Environment version
    java.vendor	                    Java Runtime Environment vendor
    java.vendor.url	                Java vendor URL
    java.home	                    Java installation directory
    java.vm.specification.version	Java Virtual Machine specification version
    java.vm.specification.vendor	Java Virtual Machine specification vendor
    java.vm.specification.name	    Java Virtual Machine specification name
    java.vm.version	                Java Virtual Machine implementation version
    java.vm.vendor	                Java Virtual Machine implementation vendor
    java.vm.name	                Java Virtual Machine implementation name
    java.specification.version	    Java Runtime Environment specification version
    java.specification.vendor	    Java Runtime Environment specification vendor
    java.specification.name	        Java Runtime Environment specification name
    java.class.version	            Java class format version number
    java.class.path	                Java class path
    java.library.path	            List of paths to search when loading libraries
    java.io.tmpdir	                Default temp file path
    java.compiler	                Name of JIT compiler to use
    java.ext.dirs	                Path of extension directory or directories
    os.name	                        Operating system name
    os.arch	                        Operating system architecture
    os.version	                    Operating system version
    file.separator	                File separator ("/" on UNIX)
    path.separator	                Path separator (":" on UNIX)
    line.separator	                Line separator ("\n" on UNIX)
    user.name	                    User's account name
    user.home	                    User's home directory
    user.dir	                    User's current working directory
*/
    }
}