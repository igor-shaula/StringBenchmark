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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.igor.shaula.omni_logger.utils.C;
import com.igor.shaula.omni_logger.utils.U;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String CN = "MainActivity";

    private static final char[] CHARS = {'-', '\\', '|', '/', '-'};

    private boolean isJobRunning;

    private int counter;

    @NonNull
    private String pendingPreparationResult = "";

    @Nullable
    private Timer twisterTimer;

    private EditText etIterationsNumber;
    private TextView tvResultOfPreparation;
    private TextView tvExplanationForTheFAB;
    private TextView tvResultForStandardLog;
    private TextView tvResultForSAL;
    private TextView tvResultForDAL;
    private TextView tvResultForVAL;
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

        etIterationsNumber = findViewById(R.id.tiedNumberOfIterations);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvResultOfPreparation = findViewById(R.id.tvResultOfPreparation);
        tvExplanationForTheFAB = findViewById(R.id.tvExplanationForTheFAB);
        tvResultForStandardLog = findViewById(R.id.tvResultForStandardLog);
        tvResultForSAL = findViewById(R.id.tvResultForSAL);
        tvResultForDAL = findViewById(R.id.tvResultForDAL);
        tvResultForVAL = findViewById(R.id.tvResultForVAL);

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
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_SYSTEM_LOG_TEST_RESULT));
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_SAL_TEST_RESULT));
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_DAL_TEST_RESULT));
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_VAL_TEST_RESULT));
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

    private void startNewJob() {
        runPerformanceAppraisal();
        toggleJobState(true);
        restoreResultViewStates();
    }

    private void restoreResultViewStates() {
        tvResultForStandardLog.setText(getString(R.string.star));
        tvResultForSAL.setText(getString(R.string.star));
        tvResultForDAL.setText(getString(R.string.star));
        tvResultForVAL.setText(getString(R.string.star));
    }

    private void stopCurrentJob() {
        interruptPerformanceTest();
        toggleJobState(false);
    }

    private void toggleJobState(boolean isRunning) {
        isJobRunning = isRunning;
        toggleJobActiveUiState(isRunning);
    }

    private void toggleJobActiveUiState(boolean isJobRunning) {
        etIterationsNumber.setEnabled(!isJobRunning);
        tvExplanationForTheFAB.setText(isJobRunning ?
                R.string.explanationForBusyFAB : R.string.explanationForReadyFAB);
        fab.setImageResource(isJobRunning ?
                android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }

    private void interruptPerformanceTest() {
        stopService(new Intent(this, TestingIntentService.class));
    }

    private void runPerformanceAppraisal() {
        int count = 0;
        try {
            count = Integer.parseInt(etIterationsNumber.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if (count > 0) {
            TestingIntentService.prepareTheBurdenForTest(this, count);
            pendingPreparationResult = "";
            showTextyTwister();
        }
        Log.d(CN, "runPerformanceAppraisal() finished");
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
                TestingIntentService.launchAllMeasurements(this);
                break;
            case C.Intent.ACTION_GET_SYSTEM_LOG_TEST_RESULT:
                whatInfoToShow = C.Choice.TEST_SYSTEM_LOG;
                resultNanoTime = intent.getLongExtra(C.Intent.NAME_SYSTEM_LOG_TIME, 0);
                break;
            case C.Intent.ACTION_GET_SAL_TEST_RESULT:
                whatInfoToShow = C.Choice.TEST_SAL;
                resultNanoTime = intent.getLongExtra(C.Intent.NAME_SAL_TIME, 0);
                break;
            case C.Intent.ACTION_GET_DAL_TEST_RESULT:
                whatInfoToShow = C.Choice.TEST_DAL;
                resultNanoTime = intent.getLongExtra(C.Intent.NAME_DAL_TIME, 0);
                break;
            case C.Intent.ACTION_GET_VAL_TEST_RESULT:
                whatInfoToShow = C.Choice.TEST_VAL;
                resultNanoTime = intent.getLongExtra(C.Intent.NAME_VAL_TIME, 0);
                break;
            case C.Intent.ACTION_ON_SERVICE_STOPPED:
                toggleJobState(false);
                return;
            default:
                Log.w(CN, "selectInfoToShow ` unknown intentAction = " + intentAction);
                return;
        }
        Log.d("selectInfoToShow", "whatInfoToShow = " + whatInfoToShow);
        Log.d("selectInfoToShow", "resultNanoTime = " + resultNanoTime);
        showPreparationsResult(whatInfoToShow, resultNanoTime);
    }

    private void showPreparationsResult(int whatInfoToShow, long resultNanoTime) {
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
                Log.w(CN, "selectInfoToShow ` unknown whatInfoToShow = " + whatInfoToShow);
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