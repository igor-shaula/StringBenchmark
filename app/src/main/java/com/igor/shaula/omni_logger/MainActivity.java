package com.igor.shaula.omni_logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private static final String CN = "MainActivity";

    //    private static Handler UI_HANDLER = new Handler(Looper.getMainLooper());

    private boolean isJobRunning;

    private EditText etIterationsNumber;
    private TextView tvResultOfPreparation;
    private TextView tvExplanationForTheFAB;
    private FloatingActionButton fab;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nanoTimeOfPreparation = intent.getIntExtra(C.Intent.NAME_PREPARATION_TIME, 0);
            final String existingText = tvResultOfPreparation.getText().toString();
            final String textForUI = existingText + C.SPACE + nanoTimeOfPreparation;
            tvResultOfPreparation.setText(textForUI);
            Log.d("receiver", "Got message: " + nanoTimeOfPreparation);
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
                .registerReceiver(mMessageReceiver, new IntentFilter(C.Intent.ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private void stopCurrentJob() {
        interruptPerformanceTest();
        isJobRunning = false;
        etIterationsNumber.setEnabled(true);
        tvExplanationForTheFAB.setText(R.string.explanationForReadyFAB);
        fab.setImageResource(android.R.drawable.ic_media_play);
    }

    private void startNewJob() {
        runPerformanceAppraisal();
        isJobRunning = true;
        etIterationsNumber.setEnabled(false);
        tvExplanationForTheFAB.setText(R.string.explanationForBusyFAB);
        fab.setImageResource(android.R.drawable.ic_media_pause);
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
                    .putExtra(C.Intent.NAME_COUNT, count));
        }
        Log.d(CN, "runPerformanceAppraisal() finished");
        // preparing to catch the result of preparing the burden - via Handler on the main thread \\
//        new Handler() {
//            //        new Handler(new Handler.Callback() {
//            @Override
//            public void handleMessage(Message message) {
//                Log.d(CN, "handleMessage: nanoTime of creation = " + message.arg2);
//                if (message.arg1 == C.Choice.PREPARATION) {
//                    final String existingText = tvResultOfPreparation.getText().toString();
//                    final String textForUI = existingText + C.SPACE + message.arg2;
//                    tvResultOfPreparation.setText(textForUI);
//                }
//            }
//        };
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
}