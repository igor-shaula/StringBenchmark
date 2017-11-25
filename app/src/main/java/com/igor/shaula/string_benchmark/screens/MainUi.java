package com.igor.shaula.string_benchmark.screens;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.screens.for_ui.SimpleTextWatcher;
import com.igor.shaula.string_benchmark.utils.C;

public final class MainUi implements MainHub.UiLink {

    private static final String CN = "MainUi";

    @NonNull
    private final Context rootContext;
    @NonNull
    private final View rootView;

    @SuppressWarnings("NullableProblems") // invoked in the Logic's constructor \\
    @NonNull
    private MainHub.LogicLink logicLink;

    private TextView tvStartingExplanation;
    private EditText etBasicString;
    private EditText etStringsAmount;
    private EditText etIterationsAmount;
    private TextView tvResultOfPreparation;
    private TextView tvExplanationForTheFAB;
    private TextView tvResultForLog;
    private TextView tvResultForSAL;
    private TextView tvResultForDAL;
    private TextView tvResultForVAL;
    private TextView tvResultForSout;
    private FloatingActionButton fab;

    MainUi(@NonNull View rootView) {
        this.rootView = rootView;
        rootContext = rootView.getContext();
    }

    @Override
    public void setLogicLink(@NonNull MainHub.LogicLink logicLink) {
        this.logicLink = logicLink;
    }

    public void init() {

        tvStartingExplanation = rootView.findViewById(R.id.tvStartingExplanation);

        final TextInputLayout tilBasicString = rootView.findViewById(R.id.tilBasicString);
        final TextInputLayout tilStringsAmount = rootView.findViewById(R.id.tilStringsAmount);
        final TextInputLayout tilIterationsAmount = rootView.findViewById(R.id.tilIterationsAmount);

        etBasicString = rootView.findViewById(R.id.tiedBasicString);
        etStringsAmount = rootView.findViewById(R.id.tiedStringsAmount);
        etIterationsAmount = rootView.findViewById(R.id.tiedIterationsAmount);

        etBasicString.setSelection(etBasicString.getText().length());
        etStringsAmount.setSelection(etStringsAmount.getText().length());
        etIterationsAmount.setSelection(etIterationsAmount.getText().length());

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

        etBasicString.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                final int basicStringLength = etBasicString.getText().length();
                if (basicStringLength != 0) {
                    // 1 \\
                    final String testBasisAltHint = MainActivity.this.getString(R.string.testBasisAltHint)
                            + C.SPACE + basicStringLength;
                    tilBasicString.setHint(testBasisAltHint);
                    // 2 \\
                    final int stringsAmountAltHint = Integer.parseInt(etStringsAmount.getText().toString());
                    final String altStringRepetitionsHint =
                            MainActivity.this.getString(R.string.stringsAmountHint)
                                    + C.SPACE + stringsAmountAltHint * basicStringLength;
                    tilStringsAmount.setHint(altStringRepetitionsHint);
                } else {
                    tilBasicString.setHint(MainActivity.this.getString(R.string.testBasisHint));
                }
                restoreResultViewStates();
                tvResultOfPreparation.setText(C.STAR);
            }
        });
        etStringsAmount.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                // safely parsing here - because inputType is number in layout \\
                final int stringsAmountAltHint = Integer.parseInt(etStringsAmount.getText().toString());
                if (stringsAmountAltHint != 0) {
                    final String altStringRepetitionsHint =
                            MainActivity.this.getString(R.string.stringsAmountHint)
                                    + C.SPACE + stringsAmountAltHint * etBasicString.getText().length();
                    tilStringsAmount.setHint(altStringRepetitionsHint);
                } else {
                    tilStringsAmount.setHint(MainActivity.this.getString(R.string.stringsAmountAltHint));
                }
                restoreResultViewStates();
                tvResultOfPreparation.setText(C.STAR);
            }
        });
        etIterationsAmount.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged() {
                final int iterationsAmount = Integer.parseInt(etIterationsAmount.getText().toString());
                if (iterationsAmount != 0) {
                    tilIterationsAmount.setHint(MainActivity.this.getString(R.string.iterationsAmountHint));
                } else {
                    tilIterationsAmount.setHint(MainActivity.this.getString(R.string.iterationsAmountAltHint));
                }
                restoreResultViewStates();
/*
                no need to reset shown value of tvResultOfPreparation here because
                testing loop iterations number has no effect on burden creation time \\
*/
            }
        });

        tvResultOfPreparation = rootView.findViewById(R.id.tvResultOfPreparation);
        tvExplanationForTheFAB = rootView.findViewById(R.id.tvExplanationForTheFAB);
        tvResultForLog = rootView.findViewById(R.id.tvResultForStandardLog);
        tvResultForSAL = rootView.findViewById(R.id.tvResultForSAL);
        tvResultForDAL = rootView.findViewById(R.id.tvResultForDAL);
        tvResultForVAL = rootView.findViewById(R.id.tvResultForVAL);
        tvResultForSout = rootView.findViewById(R.id.tvResultForSystemOutPrintln);

        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isJobRunning) {
                    stopCurrentJob();
                } else {
                    startNewJob();
                }
            }
        });
    }
}