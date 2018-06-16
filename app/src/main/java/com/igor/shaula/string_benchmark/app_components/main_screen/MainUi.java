package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.BuildConfig;
import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.app_components.main_screen.for_ui.SimpleTextWatcher;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

public final class MainUi implements MainHub.UiLink, View.OnClickListener, View.OnKeyListener {

    private static final String CN = "MainUi";
    @NonNull
    private final Context rootContext;
    @NonNull
    private final View rootView;

    @SuppressWarnings("NullableProblems") // invoked in the Logic's constructor \\
    @NonNull
    private MainHub.LogicLink logicLink;

    private AppBarLayout appBarLayout;
    //    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivToggleAppBar;

    // preparation of the burden \\
    private TextView tvPrepareLoadExplanation;
    private View vPrepareLoadExplanation;
    //    private TextView tvWorkaroundForKeepingFocus;
    private TextInputLayout tilBasicString;
    private EditText etBasicString;
    private TextInputLayout tilStringsAmount;
    private EditText etStringsAmount;
    private Button bPrepareLoad;
    //    private Button bResetLoad;
    private CheckBox cbMakeLoadEmpty;
    private Button bViewLoad;
    private TextView tvResultOfPreparation;
    private TextView tvResultForCreatingLoad;

    // iterations \\
    private TextView tvIterationsExplanation;
    private View vIterationsExplanation;
    private TextInputLayout tilIterationsAmount;
    private EditText etIterationsAmount;
    private Button bToggleAdjustedIterations;
    private CheckBox cbEndlessIterations;
    private TextView tvPreparedLoadInfo;
    private TextView tvCurrentIterationNumber;
    private TextView tvIterationsTotalNumber;
    private TextView tvIterationsResultHeader;
    private TextView tvResultForSout;
    private TextView tvResultForLog;
    private TextView tvResultForDAL;
    private TextView tvResultForVAL1;
    private TextView tvResultForVAL2;
    private TextView tvResultForVAL3;
    private TextView tvResultForSLVoid;
    private TextView tvResultForSLInt;

    MainUi(@NonNull View rootView) {
        this.rootView = rootView;
        rootContext = rootView.getContext();
    }

    @Override
    public boolean isEndless() {
        return cbEndlessIterations.isChecked();
    }

    @NonNull
    @Override
    public String getBasicStringText() {
        return etBasicString.getText().toString();
    }

    @NonNull
    @Override
    public String getStringsAmountText() {
        return etStringsAmount.getText().toString();
    }

    @Override
    public void setStringsAmountText(int howManyTimesToRepeatBasicStringInLoad) {
        etStringsAmount.setText(String.valueOf(howManyTimesToRepeatBasicStringInLoad));
    }

    @NonNull
    @Override
    public String getIterationsAmountText() {
        return etIterationsAmount.getText().toString();
    }

    @Override
    public void setLogicLink(@NonNull MainHub.LogicLink logicLink) {
        this.logicLink = logicLink;
    }

    @Override
    public void setInitialInputFieldsValues() {
        etBasicString.setText(C.INITIAL_BASIC_STRING);
        etBasicString.setSelection(C.INITIAL_BASIC_STRING.length());
        etStringsAmount.setText(C.INITIAL_STRING_REPETITIONS);
        etStringsAmount.setSelection(C.INITIAL_STRING_REPETITIONS.length());
        etIterationsAmount.setText(C.INITIAL_TEST_ITERATIONS);
        etIterationsAmount.setSelection(C.INITIAL_TEST_ITERATIONS.length());
    }

/*
    // currently not used \\
    @Override
    public void setBusy(boolean isBusy) {
        L.i(CN, "setBusy = " + isBusy);
        if (isBusy) {
            pdWait.show();
        } else if (!((Activity) rootContext).isFinishing()
                && pdWait != null
                && pdWait.isShowing()) {
            try {
                pdWait.dismiss();
            } catch (IllegalArgumentException iae) {
                L.e(CN, iae.getMessage());
            }
        }
    }
*/

    @Override
    public void toggleLoadPreparationBlock(boolean shouldBeExpanded) {
        appBarLayout.setExpanded(shouldBeExpanded);
        // in fact animation works every time here \\
    }

    @Override
    public void toggleViewLoadBusyStateOnMainThread(final boolean isBusy) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                // maybe later i'll use it \\
            }
        });
    }

    @Override
    public void updateIterationsResultOnMainThread(@NonNull final long[] oneIterationResults,
                                                   final int currentIterationNumber) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                final String currentIterationCounterString =
                        "" + C.SPACE + U.createReadableStringForLong(currentIterationNumber);
                tvCurrentIterationNumber.setText(currentIterationCounterString);
                tvResultForSout.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SOUT]));
                tvResultForLog.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_LOG]));
                tvResultForDAL.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_DAL]));
                tvResultForVAL1.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_VAL_1]));
                tvResultForVAL2.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_VAL_2]));
                tvResultForVAL3.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_VAL_3]));
                tvResultForSLVoid.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SL_VOID]));
                tvResultForSLInt.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SL_INT]));
            }
        });
    }

    @Override
    public void toggleJobActiveUiState(boolean isJobRunning) {
        etBasicString.setEnabled(!isJobRunning);
        etStringsAmount.setEnabled(!isJobRunning);
        bPrepareLoad.setEnabled(!isJobRunning);
//        bResetLoad.setEnabled(!isJobRunning);
        cbMakeLoadEmpty.setEnabled(!isJobRunning);
        bViewLoad.setEnabled(!isJobRunning && logicLink.isLoadReady());
        toggleViewLoadBusyStateOnMainThread(bViewLoad.isEnabled());
        bToggleAdjustedIterations.setText(isJobRunning ? R.string.stopIterations : R.string.startIterations);
        cbEndlessIterations.setEnabled(!isJobRunning);
    }

    @Override
    public void toggleAllExplanations(boolean shouldShowExplanations) {
        if (shouldShowExplanations) {
            tvPrepareLoadExplanation.setVisibility(View.VISIBLE);
            vPrepareLoadExplanation.setVisibility(View.VISIBLE);
            tvIterationsExplanation.setVisibility(View.VISIBLE);
            vIterationsExplanation.setVisibility(View.VISIBLE);
            tvIterationsResultHeader.setVisibility(View.VISIBLE);
        } else {
            tvPrepareLoadExplanation.setVisibility(View.GONE);
            vPrepareLoadExplanation.setVisibility(View.GONE);
            tvIterationsExplanation.setVisibility(View.GONE);
            vIterationsExplanation.setVisibility(View.GONE);
            tvIterationsResultHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public void resetResultViewStates() {
        final CharSequence oneTemplateForAll = String.valueOf(C.STAR);
        tvResultForSout.setText(oneTemplateForAll);
        tvResultForLog.setText(oneTemplateForAll);
        tvResultForDAL.setText(oneTemplateForAll);
        tvResultForVAL1.setText(oneTemplateForAll);
        tvResultForVAL2.setText(oneTemplateForAll);
        tvResultForVAL3.setText(oneTemplateForAll);
        tvResultForSLVoid.setText(oneTemplateForAll);
        tvResultForSLInt.setText(oneTemplateForAll);
    }

    @Override
    public void resetResultOfPreparation() {
        tvResultOfPreparation.setText(rootContext.getString(R.string.resultOfPreparation));
    }

    @Override
    public void updateBasicStringHint(@NonNull String s) {
        tilBasicString.setHint(s);
    }

    @Override
    public void updateStringsAmountHint(@NonNull String s) {
        tilStringsAmount.setHint(s);
    }

    @Override
    public void updateIterationAmountHint(@NonNull String s) {
        tilIterationsAmount.setHint(s);
    }

    @Override
    public void updatePreparationResultOnMainThread(final @NonNull String result) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                tvResultForCreatingLoad.setText(String.valueOf(result));
            }
        });
    }

    @Override
    public void updateLoadLengthOnMainThread(final int length) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                final String text = rootContext.getString(R.string.summaryLoadInfo)
                        + C.SPACE + U.createReadableStringForLong(length);
                tvPreparedLoadInfo.setText(text);
            }
        });
    }

    @Override
    public void updateResultForLog(long resultNanoTime) {
        tvResultForLog.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForDAL(long resultNanoTime) {
        tvResultForDAL.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForVAL1(long resultNanoTime) {
        tvResultForVAL1.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForVAL2(long resultNanoTime) {
        tvResultForVAL2.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForVAL3(long resultNanoTime) {
        tvResultForVAL3.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForSLVoid(long resultNanoTime) {
        tvResultForSLVoid.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForSLInt(long resultNanoTime) {
        tvResultForSLInt.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void informUser(int typeOfNotification, int stringId, int duration) {
        final String message = rootContext.getString(stringId);
        if (C.Choice.TOAST == typeOfNotification) {
            U.showToast(rootContext, message, duration);
        } else if (C.Choice.SNACKBAR == typeOfNotification) {
            U.showSnackbar(rootView, message, duration);
        } else {
            L.w(CN, message);
        }
    }

    @Override
    public void showBuildInfoDialog() {
        final String message = rootContext.getString(R.string.application) + C.SPACE
                + rootContext.getString(R.string.app_name) + C.N
                + rootContext.getString(R.string.versionName) + C.SPACE + BuildConfig.VERSION_NAME + C.N
                + rootContext.getString(R.string.versionCode) + C.SPACE + BuildConfig.VERSION_CODE + C.N
                + rootContext.getString(R.string.author) + C.SPACE + rootContext.getString(R.string.me);
        new AlertDialog.Builder(rootContext)
                .setTitle(rootContext.getString(R.string.aboutThisBuild))
                .setMessage(message)
                .create()
                .show();
    }

    @Override
    public void showLoadInDialog(@NonNull String load) {
        // TODO: 05.12.2017 move to the Dialog from support library \\
        final String title = rootContext.getString(R.string.summaryLoadLength) + C.SPACE + U.createReadableStringForLong(load.length());
        final AlertDialog alertDialog = new AlertDialog.Builder(rootContext)
                .setTitle(title)
                .setMessage(load)
                .create();
        alertDialog
                .show();
        toggleViewLoadBusyStateOnMainThread(!alertDialog.isShowing());
    }

    @Override
    public void showTotalIterationsNumber(final int totalIterationsCount) {
        tvIterationsTotalNumber.post(new Runnable() {
            @Override
            public void run() {
                final String iterationsTotalCountString =
                        "" + C.SPACE + C.SLASH + C.SPACE + U.createReadableStringForLong(totalIterationsCount);
                tvIterationsTotalNumber.setText(iterationsTotalCountString);
            }
        });
    }

    @Override
    public void init() {

        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.v(CN, "init ` verticalOffset = " + verticalOffset);
                logicLink.setAppBarLayoutFullyExpanded(verticalOffset == 0);
//                logicLink.setAppBarLayoutFullyExpanded(appBarLayout.getHeight() == appBarLayout.getBottom());
            }
        });
//        collapsingToolbarLayout = rootView.findViewById(R.id.collapsingToolBar);
        ivToggleAppBar = rootView.findViewById(R.id.ivToggleAppBar);
        ivToggleAppBar.setOnClickListener(this);

        // burden preparation block \\
        tvPrepareLoadExplanation = rootView.findViewById(R.id.tvPrepareLoadExplanation);
        vPrepareLoadExplanation = rootView.findViewById(R.id.vPrepareLoadExplanation);
//        tvWorkaroundForKeepingFocus = rootView.findViewById(R.id.tvWorkaroundForKeepingFocus);
        tilBasicString = rootView.findViewById(R.id.tilBasicString);
        etBasicString = rootView.findViewById(R.id.tiedBasicString);
        etBasicString.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                logicLink.onBasicStringChanged();
            }
        });
        etBasicString.setOnKeyListener(this);
        tilStringsAmount = rootView.findViewById(R.id.tilStringsAmount);
        etStringsAmount = rootView.findViewById(R.id.tiedStringsAmount);
        etStringsAmount.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                logicLink.onStringsAmountChanged();
            }
        });
        etStringsAmount.setOnKeyListener(this);
        cbMakeLoadEmpty = rootView.findViewById(R.id.cbMakeLoadEmpty);
        bPrepareLoad = rootView.findViewById(R.id.bPrepareLoad);
        bPrepareLoad.setOnClickListener(this);
//        bResetLoad = rootView.findViewById(R.id.bResetLoad);
//        bResetLoad.setOnClickListener(this);
        bViewLoad = rootView.findViewById(R.id.bViewLoad);
        bViewLoad.setOnClickListener(this);
        tvResultOfPreparation = rootView.findViewById(R.id.tvResultOfPreparation);
        tvResultForCreatingLoad = rootView.findViewById(R.id.tvResultForCreatingLoad);

        // iterations block \\
        tvIterationsExplanation = rootView.findViewById(R.id.tvIterationsExplanation);
        vIterationsExplanation = rootView.findViewById(R.id.vIterationsExplanation);
        tilIterationsAmount = rootView.findViewById(R.id.tilIterationsAmount);
        etIterationsAmount = rootView.findViewById(R.id.tiedIterationsAmount);
        etIterationsAmount.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                logicLink.onIterationsAmountChanged();
            }
        });
        etIterationsAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // disabling this view's focused state somehow - we have to pass it somewhere \\
                    clearFocusFromAllInputFields();
                    // also should close keyboard right now \\
                    final InputMethodManager imm = (InputMethodManager)
                            rootContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(etIterationsAmount.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        etIterationsAmount.setOnKeyListener(this);
        bToggleAdjustedIterations = rootView.findViewById(R.id.bToggleAdjustedIterations);
        bToggleAdjustedIterations.setOnClickListener(this);
        cbEndlessIterations = rootView.findViewById(R.id.cbEndlessIterations);
        cbEndlessIterations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tilIterationsAmount.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });
        tvPreparedLoadInfo = rootView.findViewById(R.id.tvPreparedLoadInfo);
        final String startingLoadInfo = rootContext.getString(R.string.summaryLoadInfo) + C.SPACE + C.ZERO;
        tvPreparedLoadInfo.setText(startingLoadInfo);
        tvCurrentIterationNumber = rootView.findViewById(R.id.tvCurrentIterationNumber);
        tvIterationsTotalNumber = rootView.findViewById(R.id.tvIterationsTotalNumber);
        tvIterationsResultHeader = rootView.findViewById(R.id.tvIterationsResultHeader);
        tvResultForSout = rootView.findViewById(R.id.tvResultForSystemOutPrintln);
        tvResultForLog = rootView.findViewById(R.id.tvResultForStandardLog);
        tvResultForDAL = rootView.findViewById(R.id.tvResultForDAL);
        tvResultForVAL1 = rootView.findViewById(R.id.tvResultForVAL1);
        tvResultForVAL2 = rootView.findViewById(R.id.tvResultForVAL2);
        tvResultForVAL3 = rootView.findViewById(R.id.tvResultForVAL3);
        tvResultForSLVoid = rootView.findViewById(R.id.tvResultForSLVoid);
        tvResultForSLInt = rootView.findViewById(R.id.tvResultForSLInt);

    } // init \\

    @Override
    public void clearFocusFromAllInputFields() {
        etBasicString.clearFocus();
        etStringsAmount.clearFocus();
        etIterationsAmount.clearFocus();
//        tvWorkaroundForKeepingFocus.requestFocus(); // this action is not obvious but needed in fact \\
    }

    @Override
    public void invalidateAppBar() {
        appBarLayout.invalidate();
    }

    @Override
    public void toggleAppBarExpansionIcon(boolean isFullyExpanded) {
        ivToggleAppBar.setImageResource(isFullyExpanded ?
                R.drawable.ic_close_preparation_block : R.drawable.ic_open_preparation_block);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.bPrepareLoad:
                logicLink.onPrepareLoadClick();
                break;
//            case R.id.bResetLoad:
//                logicLink.onResetLoadClick();
//                break;
            case R.id.bViewLoad:
                logicLink.onViewLoadClick();
                break;
            case R.id.bToggleAdjustedIterations:
                logicLink.onToggleIterationsClick();
                break;
            case R.id.ivToggleAppBar:
                logicLink.onLoadPreparationBlockAction();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        L.v(CN, "onKey ` keyCode = " + keyCode);
        // TODO: 10.06.2018 make sure that it works and if not - just make it work \\
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            L.v(CN, "onKey == KEYCODE_BACK");
            clearFocusFromAllInputFields();
//            return true;
            return U.hideKeyboard(v);
        }
        return false;
    }
}