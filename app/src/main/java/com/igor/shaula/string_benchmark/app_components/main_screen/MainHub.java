package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.app_components.main_screen.for_ui.OneIterationResultModel;

import java.util.List;

public interface MainHub {

    interface SystemLink {

        @NonNull
        String getLoad();

        @NonNull
        String getAdaptedString(long resultNanoTime);

        @NonNull
        String findStringById(int stringId);

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

        void toggleLoadPreparationBlock(boolean shouldExpand);

        void toggleJobActiveUiState(boolean isRunning);

        void toggleAllExplanations(boolean shouldShowExplanations);

        void resetResultViewStates();

        void resetResultOfPreparation();

        void updateBasicStringHint(@NonNull String s);

        void updateStringsAmountHint(@NonNull String s);

        void updateIterationAmountHint(@NonNull String s);

        void updatePreparationResultOnMainThread(@NonNull String result);

        void updateLoadLengthOnMainThread(int length);

        void updateIterationsResultOnMainThread(@NonNull List<OneIterationResultModel> resultModelList,
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

        @NonNull
        List<OneIterationResultModel> getIterationResultList();

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

        void transportIterationsResult(@NonNull List<OneIterationResultModel> resultModelList,
                                       int currentIterationNumber);
    }
}