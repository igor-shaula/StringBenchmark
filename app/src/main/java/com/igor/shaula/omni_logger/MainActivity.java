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

    private FloatingActionButton fab;
    @NonNull
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopCurrentJob();
            showPreparationsResult(intent.getLongExtra(C.Intent.NAME_PREPARATION_TIME, 0));
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
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_PREPARATION_RESULT));
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
        isJobRunning = true;
        toggleJobActiveUiState(true);
    }

    private void stopCurrentJob() {
        interruptPerformanceTest();
        isJobRunning = false;
        toggleJobActiveUiState(false);
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
            startService(new Intent(this, TestingIntentService.class)
                    .setAction(C.Intent.ACTION_START_BURDEN_PREPARATION)
                    .putExtra(C.Intent.NAME_COUNT, count));
            pendingPreparationResult = "";
            showTextyTwister();
        }
        Log.d(CN, "runPerformanceAppraisal() finished");
        // TODO: 07.11.2017 realize variant with using Handler to get the results back from service \\
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

    private void showPreparationsResult(long nanoTimeOfPreparation) {
        stopTwisterTimer();
        pendingPreparationResult = U.adaptForUser(this, nanoTimeOfPreparation);
        updateResultOnMainThread("");
        Log.d("receiver", "Got message: " + nanoTimeOfPreparation);
    }

    private void stopTwisterTimer() {
        if (twisterTimer != null) {
            twisterTimer.cancel(); // purge() behaves very strangely - so i decided to avoid it
        }
        twisterTimer = null;
        counter = 0;
    }
}