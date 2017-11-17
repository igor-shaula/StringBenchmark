package com.igor.shaula.omni_logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.igor.shaula.omni_logger.utils.C;
import com.igor.shaula.omni_logger.utils.L;
import com.igor.shaula.omni_logger.utils.U;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements App.Callback {

    private static final String CN = "MainActivity";

    private static final char[] CHARS = {'-', '\\', '|', '/', '-'};

    private boolean isJobRunning;

    private int counter;
    @NonNull
    private List<long[]> totalResultList = new LinkedList<>();

    @NonNull
    private String pendingPreparationResult = "";

    @Nullable
    private Timer twisterTimer;

    private EditText etStringsQuantity;
    private EditText etIterationsQuantity;
    private TextView tvResultOfPreparation;
    private TextView tvExplanationForTheFAB;
    private TextView tvResultForStandardLog;
    private TextView tvResultForSAL;
    private TextView tvResultForDAL;
    private TextView tvResultForVAL;
    private TextView tvResultForSystemOutPrintln;
    private FloatingActionButton fab;

    // TODO: 07.11.2017 realize variant with using Handler to get the results back from service \\
    @NonNull
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // we assume that intent here cannot be null by default \\
            selectInfoToShow(intent);
        }
    };

    // LIFECYCLE ===================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).setCallback(this);

        etStringsQuantity = findViewById(R.id.tiedNumberOfStrings);
        etIterationsQuantity = findViewById(R.id.tiedNumberOfLoopIterations);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvResultOfPreparation = findViewById(R.id.tvResultOfPreparation);
        tvExplanationForTheFAB = findViewById(R.id.tvExplanationForTheFAB);
        tvResultForStandardLog = findViewById(R.id.tvResultForStandardLog);
        tvResultForSAL = findViewById(R.id.tvResultForSAL);
        tvResultForDAL = findViewById(R.id.tvResultForDAL);
        tvResultForVAL = findViewById(R.id.tvResultForVAL);
        tvResultForSystemOutPrintln = findViewById(R.id.tvResultForSystemOutPrintln);

        fab = findViewById(R.id.fab);
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

    @Override
    protected void onStart() {
        super.onStart();
        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_PREPARATION_RESULT));
//        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_SYSTEM_LOG_TEST_RESULT));
//        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_SAL_TEST_RESULT));
//        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_DAL_TEST_RESULT));
//        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_VAL_TEST_RESULT));
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_ONE_ITERATION_RESULTS));
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_ON_SERVICE_STOPPED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    // MENU ========================================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // PAYLOAD =====================================================================================

    private void stopCurrentJob() {
        interruptPerformanceTest();
        toggleJobState(false);
    }

    private void interruptPerformanceTest() {
        stopService(new Intent(this, TestingIntentService.class));
    }

    private void toggleJobState(boolean isRunning) {
        isJobRunning = isRunning;
        toggleJobActiveUiState(isRunning);
    }

    private void toggleJobActiveUiState(boolean isJobRunning) {
        etStringsQuantity.setEnabled(!isJobRunning);
        etIterationsQuantity.setEnabled(!isJobRunning);
        tvExplanationForTheFAB.setText(isJobRunning ?
                R.string.explanationForBusyFAB : R.string.explanationForReadyFAB);
        fab.setImageResource(isJobRunning ?
                android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }

    private void startNewJob() {
        runTestBurdenPreparation();
        toggleJobState(true);
        restoreResultViewStates();
    }

    private void runTestBurdenPreparation() {
        int count = 0;
        try {
            count = Integer.parseInt(etStringsQuantity.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if (count > 0) {
            TestingIntentService.prepareTheBurdenForTest(this, count);
            pendingPreparationResult = "";
            showTextyTwister();
        }
        L.d(CN, "runTestBurdenPreparation() finished");
/*
                    VAL.v("" + getString(R.string.vero_test).length());
                    VAL.v("", "");
                    VAL.v("", "", "");
                    VAL.v();
                    VAL.v((String[]) null);
                    VAL.v(null, null);
                    VAL.v(null, null, null);
                    VAL.v(this.toString(), null, null);
                    VAL.v("1");
                    VAL.v("1", "2");
                    VAL.v("1", "2", "3");
                    VAL.v("", "", "", "");
*/
    }

    private void showTextyTwister() {
        final int[] index = new int[1];
        final String[] textForUI = new String[1];
        final TimerTask twisterTask = new TimerTask() {
            @Override
            public void run() {
                // 0 - 1 - 2 - 3 - 0 - 1 - 2 - 3 - 0 - ...
                index[0] = counter % CHARS.length;
                textForUI[0] = String.valueOf(CHARS[index[0]]);
                updateResultOnMainThread(textForUI[0]);
            }
        };
        twisterTimer = new Timer(true);
        twisterTimer.schedule(twisterTask, 0, 80);
    }

    private void restoreResultViewStates() {
        tvResultForStandardLog.setText(getString(R.string.star));
        tvResultForSAL.setText(getString(R.string.star));
        tvResultForDAL.setText(getString(R.string.star));
        tvResultForVAL.setText(getString(R.string.star));
        tvResultForSystemOutPrintln.setText(getString(R.string.star));
    }

    private void updateResultOnMainThread(@NonNull final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pendingPreparationResult.isEmpty()) {
                    tvResultOfPreparation.setText(result);
                    counter++;
                } else {
                    tvResultOfPreparation.setText(pendingPreparationResult);
                }
            }
        }); // runOnUiThread \\
    }

    private void selectInfoToShow(@NonNull Intent intent) {
        final String intentAction = intent.getAction();
        if (intentAction == null) {
            return;
        }
        final int whatInfoToShow;
        final long resultNanoTime;
        switch (intentAction) {
            case C.Intent.ACTION_GET_PREPARATION_RESULT:
                whatInfoToShow = C.Choice.PREPARATION;
                resultNanoTime = intent.getLongExtra(C.Intent.NAME_PREPARATION_TIME, 0);
                // immediately launching the next job - the main job of testing speed of variants \\
                prepareMainJob();
                break;
            case C.Intent.ACTION_GET_ONE_ITERATION_RESULTS:
//                long[] oneIterationResults = intent.getLongArrayExtra(C.Intent.NAME_ALL_TIME);
//                L.restore();
//                L.w("selectInfoToShow",
//                        " oneIterationResults = " + Arrays.toString(oneIterationResults));
//                L.silence();
//                storeToIntegralResult(oneIterationResults);
//                showPreparationsResult(calculateMedianResult());
                return;
            case C.Intent.ACTION_ON_SERVICE_STOPPED:
                toggleJobState(false);
                return;
            default:
                L.w(CN, "selectInfoToShow ` unknown intentAction = " + intentAction);
                return;
        }
        showPreparationsResult(whatInfoToShow, resultNanoTime);
    }

    private void prepareMainJob() {
        int count = 0;
        try {
            count = Integer.parseInt(etIterationsQuantity.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if (count > 0) {
            totalResultList.clear();
            TestingIntentService.launchAllMeasurements(this, count);
        }
        L.d(CN, "prepareMainJob() finished");
    }

    @Override
    public void transportOneIterationsResult(@NonNull long[] oneIterationsResult) {
        L.w("transportOneIterationsResult",
                " oneIterationsResult = " + Arrays.toString(oneIterationsResult));
        storeToIntegralResult(oneIterationsResult);
        final long[] results = calculateMedianResult();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showPreparationsResult(results);
            }
        });
    }

    private void storeToIntegralResult(@NonNull long[] oneIterationResults) {
        totalResultList.add(oneIterationResults);
    }

    @NonNull
    private long[] calculateMedianResult() {
        L.w("calculateMedianResult", "totalResultList.size = " + totalResultList.size());
        long[] medianArray = new long[5];
        if (totalResultList.isEmpty()) { // anyway we should not fall inside this check \\
            // avoiding division by zero in the loop just after this check \\
            return medianArray;
        }
        long sumForLog = 0;
        long sumForSAL = 0;
        long sumForDAL = 0;
        long sumForVAL = 0;
        long sumForSout = 0;
        for (long[] array : totalResultList) {
            L.w("calculateMedianResult", "" + Arrays.toString(array));
            // i hope we'll avoid exceeding the max value for type long \\
            sumForLog += array[0];
            sumForSAL += array[1];
            sumForDAL += array[2];
            sumForVAL += array[3];
            sumForSout += array[4];
        }
        medianArray[0] = sumForLog / totalResultList.size();
        medianArray[1] = sumForSAL / totalResultList.size();
        medianArray[2] = sumForDAL / totalResultList.size();
        medianArray[3] = sumForVAL / totalResultList.size();
        medianArray[4] = sumForSout / totalResultList.size();

        return medianArray;
    }

    private void showPreparationsResult(@Nullable long[] oneIterationResults) {
        if (oneIterationResults == null || oneIterationResults.length != 5) {
            return;
        }
        tvResultForStandardLog.setText(U.adaptForUser(this, oneIterationResults[0]));
        tvResultForSAL.setText(U.adaptForUser(this, oneIterationResults[1]));
        tvResultForDAL.setText(U.adaptForUser(this, oneIterationResults[2]));
        tvResultForVAL.setText(U.adaptForUser(this, oneIterationResults[3]));
        tvResultForSystemOutPrintln.setText(U.adaptForUser(this, oneIterationResults[4]));
    }

    private void showPreparationsResult(int whatInfoToShow, long resultNanoTime) {
        L.d("showPreparationsResult", "whatInfoToShow = " + whatInfoToShow);
        L.d("showPreparationsResult", "resultNanoTime = " + resultNanoTime);
        switch (whatInfoToShow) {
            case C.Choice.PREPARATION:
                stopTwisterTimer();
                pendingPreparationResult = U.adaptForUser(this, resultNanoTime);
                updateResultOnMainThread("");
                break;
            case C.Choice.TEST_SYSTEM_LOG:
                tvResultForStandardLog.setText(U.adaptForUser(this, resultNanoTime));
                break;
            case C.Choice.TEST_SAL:
                tvResultForSAL.setText(U.adaptForUser(this, resultNanoTime));
                break;
            case C.Choice.TEST_DAL:
                tvResultForDAL.setText(U.adaptForUser(this, resultNanoTime));
                break;
            case C.Choice.TEST_VAL:
                tvResultForVAL.setText(U.adaptForUser(this, resultNanoTime));
                break;
            default:
                L.w(CN, "selectInfoToShow ` unknown whatInfoToShow = " + whatInfoToShow);
        }
    }

    private void stopTwisterTimer() {
        if (twisterTimer != null) {
            twisterTimer.cancel(); // purge() behaves very strangely - so i decided to avoid it
        }
        twisterTimer = null;
        counter = 0;
    }
}