package com.igor.shaula.string_benchmark.screens;

import android.support.annotation.NonNull;

public interface MainHub {

    interface SystemLink {

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

        void setInitialValues();

        void toggleJobActiveUiState(boolean isRunning);

        void resetResultViewStates();

        void resetResultOfPreparation();

        void showPreparationsResultOnMainThread(@NonNull long[] results);

        void updateBasicStringHint(@NonNull String s);

        void updateStringsAmountHint(@NonNull String s);

        void updateIterationAmountHint(@NonNull String s);

        void updatePreparationResultOnMainThread(@NonNull String result);

        void updateResultForLog(long resultNanoTime);

        void updateResultForSAL(long resultNanoTime);

        void updateResultForDAL(long resultNanoTime);

        void updateResultForVAL(long resultNanoTime);

        void informUser(int whichWay, int stringId, int duration);

        void init();
    }

    interface LogicLink {

        boolean isBurdenReady();

        void toggleJobState(boolean isRunning);

        void onBackPressed();

        void onBasicStringChanged();

        void onStringsAmountChanged();

        void onIterationsAmountChanged();

        void onPrepareBurdenClick();

        void prepareMainJob();

        void showPreparationsResult(int whatInfoToShow, long resultNanoTime);

        void unLinkDataTransport();

        void interruptPerformanceTest();
    }
}