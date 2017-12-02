package com.igor.shaula.string_benchmark.screens;

import android.support.annotation.NonNull;

public interface MainHub {

    interface SystemLink {

        @NonNull
        String getAdaptedString(long resultNanoTime);

        void launchPreparation(@NonNull String basicString, int basicStringsCount);

        void launchAllMeasurements(int testRepetitionsCount);

        void stopTestingService();

        void finishItself();
    }

    interface UiLink {

        @NonNull
        String getBasicString();

        @NonNull
        String getRepetitionsCount();

        @NonNull
        String getIterationsAmount();

        void setLogicLink(@NonNull LogicLink logicLink);

        void toggleJobActiveUiState(boolean isRunning);

        void restoreResultViewStates();

        void showPreparationsResultOnMainThread(@NonNull long[] results);

        void updatePreparationResultOnMainThread(@NonNull String result);

        void updateResultForLog(long resultNanoTime);

        void updateResultForSAL(long resultNanoTime);

        void updateResultForDAL(long resultNanoTime);

        void updateResultForVAL(long resultNanoTime);

        void init();

        void informUser(int whichWay, int stringId, int duration);
    }

    interface LogicLink {

        void toggleJobState(boolean isRunning);

        void onBackPressed();

        void onFabClick();

        void prepareMainJob();

        void showPreparationsResult(int whatInfoToShow, long resultNanoTime);

        void unLinkDataTransport();

        void interruptPerformanceTest();
    }
}