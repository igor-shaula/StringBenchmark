package com.igor.shaula.string_benchmark.android_related.main_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ToggleButton;

import com.igor.shaula.string_benchmark.BuildConfig;
import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.android_related.main_screen.for_ui.IterationResultsAdapterWithMap;
import com.igor.shaula.string_benchmark.android_related.main_screen.for_ui.SimpleTextWatcher;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

import java.util.Map;

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
    private ImageView ivToggleAppBar;
    private ToggleButton tbLoadPreparationBlock;
    // preparation of the load \\
    private TextView tvPrepareLoadExplanation;
    private View vPrepareLoadExplanation;
    private TextInputLayout tilBasicString;
    private EditText etBasicString;
    private TextInputLayout tilStringsAmount;
    private EditText etStringsAmount;
    private Button bPrepareLoad;
    private CheckBox cbMakeLoadEmpty;
    private TextView tvResultOfPreparation;
    private TextView tvResultForCreatingLoad;

    // iterations \\
    private TextView tvIterationsExplanation;
    private View vIterationsExplanation;
    private TextInputLayout tilIterationsAmount;
    private EditText etIterationsAmount;
    private ToggleButton bToggleAdjustedIterations;
    private CheckBox cbEndlessIterations;
    private TextView tvPreparedLoadInfo;
    private TextView tvCurrentIterationNumber;
    private TextView tvIterationsTotalNumber;
    private TextView tvIterationsResultHeader;
    private View vIterationsResultHeader;
    private IterationResultsAdapterWithMap rvAdapter;
//    private IterationResultsAdapterWithListX rvAdapter;

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

//    @Override
//    public void updateIterationsResultOnMainThread(@NonNull final long[] oneIterationResults,
//                                                   final int currentIterationNumber) {
//        rootView.post(new Runnable() {
//            @Override
//            public void run() {
//                final String currentIterationCounterString =
//                        "" + C.SPACE + U.createReadableStringForLong(currentIterationNumber);
//                tvCurrentIterationNumber.setText(currentIterationCounterString);
//                tvResultForSout.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SOUT]));
//                tvResultForLog.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_LOG]));
//                tvResultForDAL.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_DAL]));
//                tvResultForVAL1.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_VAL_1]));
//                tvResultForVAL2.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_VAL_2]));
//                tvResultForVAL3.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_VAL_3]));
//                tvResultForSLVoid.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SL_VOID]));
//                tvResultForSLInt.setText(U.adaptForUser(rootContext, oneIterationResults[C.Order.INDEX_OF_SL_INT]));
//            }
//        });
//    }

    @Override
    public void toggleJobActiveUiState(boolean isJobRunning) {
        etBasicString.setEnabled(!isJobRunning);
        etStringsAmount.setEnabled(!isJobRunning);
        bPrepareLoad.setEnabled(!isJobRunning);
        cbMakeLoadEmpty.setEnabled(!isJobRunning);
        bToggleAdjustedIterations.setText(isJobRunning ? R.string.stopIterations : R.string.startIterations);
        bToggleAdjustedIterations.setChecked(isJobRunning);
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
            vIterationsResultHeader.setVisibility(View.VISIBLE);
        } else {
            tvPrepareLoadExplanation.setVisibility(View.GONE);
            vPrepareLoadExplanation.setVisibility(View.GONE);
            tvIterationsExplanation.setVisibility(View.GONE);
            vIterationsExplanation.setVisibility(View.GONE);
            tvIterationsResultHeader.setVisibility(View.GONE);
            vIterationsResultHeader.setVisibility(View.GONE);
        }
        final CollapsingToolbarLayout.LayoutParams parallaxLayoutParams
                = (CollapsingToolbarLayout.LayoutParams) ivToggleAppBar.getLayoutParams();
        parallaxLayoutParams.setParallaxMultiplier(shouldShowExplanations ? 0.84F : 0.81F);
        ivToggleAppBar.setLayoutParams(parallaxLayoutParams);
    }

    @Override
    public void resetResultViewStates() {
        rvAdapter.updateIterationsResult(logicLink.getInitialEmptyMap());
        rvAdapter.notifyDataSetChanged();
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
        L.v(CN, "showLoadInDialog ` load = " + load);
        // TODO: 05.12.2017 move to the Dialog from support library \\
        final String title = rootContext.getString(R.string.summaryLoadLength) + C.SPACE + U.createReadableStringForLong(load.length());
        final AlertDialog alertDialog = new AlertDialog.Builder(rootContext)
                .setTitle(title)
                .setMessage(load)
                .create();
        alertDialog
                .show();
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
        appBarLayout.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.v(CN, "init ` verticalOffset = " + verticalOffset);
                logicLink.setAppBarLayoutFullyExpanded(verticalOffset == 0);
//                logicLink.setAppBarLayoutFullyExpanded(appBarLayout.getHeight() == appBarLayout.getBottom());
            }
        });
        final Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        ivToggleAppBar = rootView.findViewById(R.id.ivToggleAppBar);
        ivToggleAppBar.setOnClickListener(this);
        tbLoadPreparationBlock = rootView.findViewById(R.id.tbLoadPreparationBlock);
        tbLoadPreparationBlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                logicLink.onLoadPreparationBlockAction();
            }
        });
        final ToggleButton tbExplanations = rootView.findViewById(R.id.tbExplanations);
        tbExplanations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                logicLink.onToggleAllExplanationsAction();
            }
        });
        final Button bAboutTheApp = rootView.findViewById(R.id.bAboutTheApp);
        bAboutTheApp.setOnClickListener(this);

        // burden preparation block \\
        tvPrepareLoadExplanation = rootView.findViewById(R.id.tvPrepareLoadExplanation);
        vPrepareLoadExplanation = rootView.findViewById(R.id.vPrepareLoadExplanation);
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
        cbMakeLoadEmpty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                logicLink.onLoadEmptyChecked(isChecked);
            }
        });
        bPrepareLoad = rootView.findViewById(R.id.bPrepareLoad);
        bPrepareLoad.setOnClickListener(this);
        final Button bViewLoad = rootView.findViewById(R.id.bViewLoad);
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
        vIterationsResultHeader = rootView.findViewById(R.id.vIterationsResultHeader);

        final RecyclerView rvIterationResults = rootView.findViewById(R.id.rvIterationResults);
        // as inner changes in content should not affect the RecyclerView size \\
        rvIterationResults.setHasFixedSize(true);
        rvIterationResults.setLayoutManager(new LinearLayoutManager(rootContext));
        // special link to adapter is needed for upcoming update-kind method \\
        rvAdapter = new IterationResultsAdapterWithMap();
//        rvAdapter = new IterationResultsAdapterWithMap(logicLink.getInitialEmptyMap());
//        rvAdapter = new IterationResultsAdapterWithListX(logicLink.getIterationResultList());
        rvIterationResults.setAdapter(rvAdapter);

    } // init \\

    @Override
    public void updateIterationsResultOnMainThread(@NonNull final Map<String, Long> resultModelMap,
//    public void updateIterationsResultOnMainThread(@NonNull final List<OneIterationResultModel> resultModelList,
                                                   final int currentIterationIndex) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                rvAdapter.updateIterationsResult(resultModelMap, currentIterationIndex);
                rvAdapter.notifyDataSetChanged();
                final String currentIterationCounterString =
                        "" + C.SPACE + U.createReadableStringForLong(currentIterationIndex + 1);
                tvCurrentIterationNumber.setText(currentIterationCounterString);
            }
        });
    }

    @Override
    public void clearFocusFromAllInputFields() {
        etBasicString.clearFocus();
        etStringsAmount.clearFocus();
        etIterationsAmount.clearFocus();
//        tvWorkaroundForKeepingFocus.requestFocus(); // this action is not obvious but needed in fact \\
    }

    @Override
    public void toggleAppBarExpansionIcon(boolean isFullyExpanded) {
        ivToggleAppBar.setImageResource(isFullyExpanded ?
                R.drawable.ic_close_preparation_block : R.drawable.ic_open_preparation_block);
    }

//    @Override
//    public void togglePreparationTButton(boolean isFullyExpanded) {
//        tbLoadPreparationBlock.setChecked(!isFullyExpanded);
//    }

    @Override
    public void hideKeyboard() {
        U.hideKeyboard(rootView);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.bPrepareLoad:
                logicLink.onPrepareLoadClick();
                break;
            case R.id.bViewLoad:
                logicLink.onViewLoadClick();
                break;
            case R.id.bToggleAdjustedIterations:
                logicLink.onToggleIterationsClick();
                break;
            case R.id.toolbar: // not a mistake here - specially set behavior \\
            case R.id.ivToggleAppBar:
                logicLink.onLoadPreparationBlockAction();
                break;
            case R.id.bAboutTheApp:
                logicLink.onShowDialogWithBuildInfoAction();
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