package com.igor.shaula.string_benchmark.android_related.main_screen;

import android.content.Context;
import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.logic_engine.DataTransport;

import java.util.Map;

public interface MainHub {

    interface SystemLink {

        @NonNull
        DataTransport getDataTransport();

        @NonNull
        Context getContext();

        @NonNull
        String getLoad();

        @NonNull
        String getAdaptedString(long resultNanoTime);

        @NonNull
        String findStringById(int stringId);

        void launchPreparation(@NonNull String basicString, int basicStringsCount);

        void launchAllMeasurements(int testRepetitionsCount);

        void forbidIterationsJob(boolean isForbiddenToRunIterations);

        void stopTestingService();

        void finishItself();
    }

    interface UiLink {

        boolean isEndless();

        @NonNull
        String getBasicStringText();

        @NonNull
        String getStringsAmountText();

        @NonNull
        String getIterationsAmountText();

        void setStringsAmountText(int howManyTimesToRepeatBasicStringInLoad);

        void setLogicLink(@NonNull LogicLink logicLink);

        void setInitialInputFieldsValues();

        void toggleLoadPreparationBlock(boolean shouldExpand);

        void toggleJobActiveUiState(boolean isRunning);

        void toggleAllExplanations(boolean shouldShowExplanations);

        void resetResultOfPreparation();

        void updateBasicStringHint(@NonNull String s);

        void updateStringsAmountHint(@NonNull String s);

        void updateIterationAmountHint(@NonNull String s);

        void updatePreparationResultOnMainThread(@NonNull String result);

        void updateLoadLengthOnMainThread(int length);

        void updateIterationsResultOnMainThread(@NonNull Map<String, Long> resultModelList,
                                                int currentIterationNumber);

        void informUser(int whichWay, int stringId, int duration);

        void showBuildInfoDialog();

        void showLoadInDialog(@NonNull String load);

        void showTotalIterationsNumber(int totalIterationsCount);

        void init();

        void clearFocusFromAllInputFields();

        void toggleAppBarExpansionIcon(boolean isFullyExpanded);

//        void togglePreparationTButton(boolean isFullyExpanded);

        void hideKeyboard();
    }

    interface LogicLink {

        boolean isPreparationBlockShown();

        void setAppBarLayoutFullyExpanded(boolean isFullyExpanded);

        void toggleLoadPreparationJobState(boolean isRunning);

        void toggleIterationsJobState(boolean isRunning);

        void onLoadPreparationBlockAction();

        void onToggleAllExplanationsAction();

        void onShowDialogWithBuildInfoAction();

        void onBackPressed();

        void onBasicStringChanged();

        void onStringsAmountChanged();

        void onIterationsAmountChanged();

        void onLoadEmptyChecked(boolean isChecked);

        void onPrepareLoadClick();

        void onViewLoadClick();

        void onToggleIterationsClick();

        void showPreparationsResult(int whatInfoToShow, long resultNanoTime);

        void linkDataTransport();

        void unLinkDataTransport();

        void interruptPerformanceTest();

        void updatePreparationResult(@NonNull String s);

        void transportIterationsResult(@NonNull Map<String, Long> resultModelList,
                                       int currentIterationNumber);
    }
}