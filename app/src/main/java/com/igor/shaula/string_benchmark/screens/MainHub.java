package com.igor.shaula.string_benchmark.screens;

import android.support.annotation.NonNull;

public interface MainHub {

    interface SystemLink {

        @NonNull
        String getBurden();

        @NonNull
        String getAdaptedString(long resultNanoTime);

        @NonNull
        String findStringById(int stringId);

        void launchPreparation(@NonNull String basicString, int basicStringsCount);

        void launchAllMeasurements(int testRepetitionsCount);

        void stopTestingService();

        void finishItself();
    }

    interface UiLink {

        @NonNull
        String getBasicStringText();

        @NonNull
        String getStringsAmountText();

        @NonNull
        String getIterationsAmountText();

        void setLogicLink(@NonNull LogicLink logicLink);

        void setInitialInputFieldsValues();

        void setBusy(boolean isBusy);

        void toggleViewBurdenBusyStateOnMainThread(boolean isBusy);

        void toggleJobActiveUiState(boolean isRunning);

        void resetResultViewStates();

        void resetResultOfPreparation();

        void updatePreparationsResultOnMainThread(@NonNull long[] results);

        void updateBasicStringHint(@NonNull String s);

        void updateStringsAmountHint(@NonNull String s);

        void updateIterationAmountHint(@NonNull String s);

        void updatePreparationResultOnMainThread(@NonNull String result);

        void updateBurdenLengthOnMainThread(int length);

        void updateResultForLog(long resultNanoTime);

        void updateResultForSAL(long resultNanoTime);

        void updateResultForDAL(long resultNanoTime);

        void updateResultForVAL(long resultNanoTime);

        void informUser(int whichWay, int stringId, int duration);

        void showBuildInfoDialog();

        void showBurdenInDialog(@NonNull String burden);

        void init();
    }

    interface LogicLink {

        boolean isBurdenReady();

        void toggleBurdenPreparationJobState(boolean isRunning);

        void toggleIterationsJobState(boolean isRunning);

        void onBackPressed();

        void onBasicStringChanged();

        void onStringsAmountChanged();

        void onIterationsAmountChanged();

        void onPrepareBurdenClick();

        void onViewBurdenClick();

        void onToggleIterationsClick();

        void showDialogWithBuildInfo();

        void showPreparationsResult(int whatInfoToShow, long resultNanoTime);

        void unLinkDataTransport();

        void interruptPerformanceTest();
    }
}