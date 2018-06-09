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

        void togglePrefsFragment(boolean shouldShow);

        void launchPreparation(@NonNull String basicString, int basicStringsCount);

        void launchAllMeasurements(int testRepetitionsCount);

        void allowIterationsJob(boolean isAllowedToRunIterations);

        void stopTestingService();

        void finishItself();
    }

    interface UiLink {

        @NonNull
        String getBasicStringText();

        void setEmptyBasicStringText();

        @NonNull
        String getStringsAmountText();

        @NonNull
        String getIterationsAmountText();

        void setLogicLink(@NonNull LogicLink logicLink);

        void setInitialInputFieldsValues();

        void toggleViewLoadBusyStateOnMainThread(boolean isBusy);

        void toggleJobActiveUiState(boolean isRunning);

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
    }

    interface LogicLink {

        boolean isLoadReady();

        boolean isPrefsFragmentShownHere();

        void toggleLoadPreparationJobState(boolean isRunning);

        void toggleIterationsJobState(boolean isRunning);

        void onBackPressed();

        void onBasicStringChanged();

        void onStringsAmountChanged();

        void onIterationsAmountChanged();

        void onPrepareLoadClick();

        void onResetLoadClick();

        void onViewLoadClick();

        void onToggleIterationsClick(boolean isEndless);

        void showDialogWithBuildInfo();

        void togglePrefsFragmentHere();

        void showPreparationsResult(int whatInfoToShow, long resultNanoTime);

        void linkDataTransport();

        void unLinkDataTransport();

        void interruptPerformanceTest();

        void transportIterationsResult(@NonNull long[] results, int currentIterationNumber);

        void updatePreparationResult(@NonNull String s);
    }
}