package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.support.annotation.NonNull;

public interface MainHub {

    interface SystemLink {

        @NonNull
        String getLoad();

        @NonNull
        String getAdaptedString(long resultNanoTime);

        @NonNull
        String findStringById(int stringId);

        void toggleMenuItemAboutExplanations(boolean shouldShowExplanations);

        void toggleAppBarExpansionIcon(boolean shouldCollapseAbbBar);

        void launchPreparation(@NonNull String basicString, int basicStringsCount);

        void launchAllMeasurements(int testRepetitionsCount);

        void allowIterationsJob(boolean isAllowedToRunIterations);

        void stopTestingService();

        void finishItself();
    }

    interface UiLink {

        boolean isEndless();

        @NonNull
        String getBasicStringText();

        void setStringsAmountText(int howManyTimesToRepeatBasicStringInLoad);

        @NonNull
        String getStringsAmountText();

        @NonNull
        String getIterationsAmountText();

        void setLogicLink(@NonNull LogicLink logicLink);

        void setInitialInputFieldsValues();

        void toggleLoadPreparationBlock();

        void toggleViewLoadBusyStateOnMainThread(boolean isBusy);

        void toggleJobActiveUiState(boolean isRunning);

        void toggleAllExplanations(boolean shouldShowExplanations);

        void resetResultViewStates();

        void resetResultOfPreparation();

        void updateIterationsResultOnMainThread(@NonNull long[] results, int currentIterationNumber);

        void updateBasicStringHint(@NonNull String s);

        void updateStringsAmountHint(@NonNull String s);

        void updateIterationAmountHint(@NonNull String s);

        void updatePreparationResultOnMainThread(@NonNull String result);

        void updateLoadLengthOnMainThread(int length);

        void updateResultForLog(long resultNanoTime);

        void updateResultForDAL(long resultNanoTime);

        void updateResultForVAL1(long resultNanoTime);

        void updateResultForVAL2(long resultNanoTime);

        void updateResultForVAL3(long resultNanoTime);

        void updateResultForSLVoid(long resultNanoTime);

        void updateResultForSLInt(long resultNanoTime);

        void informUser(int whichWay, int stringId, int duration);

        void showBuildInfoDialog();

        void showLoadInDialog(@NonNull String load);

        void showTotalIterationsNumber(int totalIterationsCount);

        void init();

        void clearFocusFromAllInputFields();
    }

    interface LogicLink {

        boolean isLoadReady();

        boolean isPreparationBlockShown();

        void setAppBarLayoutFullyExpanded(boolean isFullyExpanded);

        void toggleLoadPreparationBlock();

        void toggleLoadPreparationJobState(boolean isRunning);

        void toggleIterationsJobState(boolean isRunning);

        void toggleAllExplanations();

        void onBackPressed();

        void onBasicStringChanged();

        void onStringsAmountChanged();

        void onIterationsAmountChanged();

        void onPrepareLoadClick();

        void onResetLoadClick();

        void onViewLoadClick();

        void onToggleIterationsClick();

        void showDialogWithBuildInfo();

        void showPreparationsResult(int whatInfoToShow, long resultNanoTime);

        void linkDataTransport();

        void unLinkDataTransport();

        void interruptPerformanceTest();

        void transportIterationsResult(@NonNull long[] results, int currentIterationNumber);

        void updatePreparationResult(@NonNull String s);
    }
}