package com.igor.shaula.string_benchmark.screens;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.screens.for_ui.SimpleTextWatcher;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

public final class MainUi implements MainHub.UiLink, View.OnClickListener {

    private static final String CN = "MainUi";
    @NonNull
    private final Context rootContext;
    @NonNull
    private final View rootView;

    @SuppressWarnings("NullableProblems") // invoked in the Logic's constructor \\
    @NonNull
    private MainHub.LogicLink logicLink;

    // preparation of the burden \\
    private TextView tvStartingExplanation;
    private TextInputLayout tilBasicString;
    private EditText etBasicString;
    private TextInputLayout tilStringsAmount;
    private EditText etStringsAmount;
    private Button bPrepareBurden;
    private Button bViewBurden;
    private TextView tvResultOfPreparation;

    // iterations \\
    private TextInputLayout tilIterationsAmount;
    private EditText etIterationsAmount;
    private Button bToggleIterations;
    private Button bViewAllResults;
    private TextView tvResultForLog;
    private TextView tvResultForSAL;
    private TextView tvResultForDAL;
    private TextView tvResultForVAL;
    private TextView tvResultForSout;

    MainUi(@NonNull View rootView) {
        this.rootView = rootView;
        rootContext = rootView.getContext();
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

    @Override
    public void showPreparationsResultOnMainThread(@NonNull final long[] oneIterationResults) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                tvResultForLog.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_LOG]));
                tvResultForSAL.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SAL]));
                tvResultForDAL.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_DAL]));
                tvResultForVAL.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_VAL]));
                tvResultForSout.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SOUT]));
            }
        });
    }

    @Override
    public void toggleJobActiveUiState(boolean isJobRunning) {
        etBasicString.setEnabled(!isJobRunning);
        etStringsAmount.setEnabled(!isJobRunning);
        etIterationsAmount.setEnabled(!isJobRunning);
        bPrepareBurden.setEnabled(!isJobRunning);
        bViewBurden.setEnabled(!isJobRunning && logicLink.isBurdenReady());
    }

    @Override
    public void resetResultViewStates() {
        tvResultForLog.setText(C.STAR);
        tvResultForSAL.setText(C.STAR);
        tvResultForDAL.setText(C.STAR);
        tvResultForVAL.setText(C.STAR);
        tvResultForSout.setText(C.STAR);
    }

    @Override
    public void resetResultOfPreparation() {
        tvResultOfPreparation.setText(C.STAR);
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
                tvResultOfPreparation.setText(result);
            }
        });
    }

    @Override
    public void updateResultForLog(long resultNanoTime) {
        tvResultForLog.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForSAL(long resultNanoTime) {
        tvResultForSAL.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForDAL(long resultNanoTime) {
        tvResultForDAL.setText(U.adaptForUser(rootContext, resultNanoTime));
    }

    @Override
    public void updateResultForVAL(long resultNanoTime) {
        tvResultForVAL.setText(U.adaptForUser(rootContext, resultNanoTime));
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
    public void init() {

        tvStartingExplanation = rootView.findViewById(R.id.tvStartingExplanation);

        // burden preparation block \\
        tilBasicString = rootView.findViewById(R.id.tilBasicString);
        etBasicString = rootView.findViewById(R.id.tiedBasicString);
        etBasicString.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                logicLink.onBasicStringChanged();
            }
        });
        tilStringsAmount = rootView.findViewById(R.id.tilStringsAmount);
        etStringsAmount = rootView.findViewById(R.id.tiedStringsAmount);
        etStringsAmount.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                logicLink.onStringsAmountChanged();
            }
        });
        bPrepareBurden = rootView.findViewById(R.id.bPrepareBurden);
        bPrepareBurden.setOnClickListener(this);
        bViewBurden = rootView.findViewById(R.id.bViewBurden);
        bViewBurden.setOnClickListener(this);
        tvResultOfPreparation = rootView.findViewById(R.id.tvResultOfPreparation);

        // iterations block \\
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
                    etBasicString.clearFocus();
                    etStringsAmount.clearFocus();
                    etIterationsAmount.clearFocus();
                    tvStartingExplanation.requestFocus();
                    // also should close keyboard right now \\
                    final InputMethodManager imm = (InputMethodManager)
                            rootContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(tvStartingExplanation.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        bToggleIterations = rootView.findViewById(R.id.bToggleIterations);
        bToggleIterations.setOnClickListener(this);
        bViewAllResults = rootView.findViewById(R.id.bViewAllResults);
        bViewAllResults.setOnClickListener(this);
        tvResultForLog = rootView.findViewById(R.id.tvResultForStandardLog);
        tvResultForSAL = rootView.findViewById(R.id.tvResultForSAL);
        tvResultForDAL = rootView.findViewById(R.id.tvResultForDAL);
        tvResultForVAL = rootView.findViewById(R.id.tvResultForVAL);
        tvResultForSout = rootView.findViewById(R.id.tvResultForSystemOutPrintln);

    } // init \\

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bPrepareBurden:
                logicLink.onPrepareBurdenClick();
                break;
            case R.id.bViewBurden:

                break;
            case R.id.bToggleIterations:
                logicLink.onToggleIterationsClick();
                break;
            case R.id.bViewAllResults:

                break;
        }
    }
}