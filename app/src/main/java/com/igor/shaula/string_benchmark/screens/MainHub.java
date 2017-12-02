package com.igor.shaula.string_benchmark.screens;

import android.support.annotation.NonNull;

public interface MainHub {

    interface SystemLink {

        void stopTestingService();

        void launchPreparation(@NonNull String basicString, int count);

        void launchAllMeasurements(int count);

        @NonNull
        String getAdaptedString(long resultNanoTime);
    }

    interface UiLink {

        void setLogicLink(@NonNull LogicLink logicLink);

        void showPreparationsResultOnMainThread(long[] results);

        void toggleJobActiveUiState(boolean isRunning);

        @NonNull
        String getBasicString();

        @NonNull
        String getRepetitionsCount();

        void restoreResultViewStates();

        String getIterationsAmount();

        void updateResultForLog(long resultNanoTime);

        void updateResultForSAL(long resultNanoTime);

        void updateResultForDAL(long resultNanoTime);

        void updateResultForVAL(long resultNanoTime);

        void updatePreparationResultOnMainThread(String result);

        void init();
    }

    interface LogicLink {

        void unLinkDataTransport();

        void interruptPerformanceTest();

        void onFabClick();

        void prepareMainJob();

        void toggleJobState(boolean isRunning);

        void showPreparationsResult(int whatInfoToShow, long resultNanoTime);
    }
}