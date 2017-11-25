package com.igor.shaula.string_benchmark.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.igor.shaula.string_benchmark.App;
import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.TestingIntentService;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public final class MainActivity extends AppCompatActivity implements App.Callback, MainHub.SystemLink {

    private static final String CN = "MainActivity";

    @NonNull
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
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

        ((App) getApplication()).setLinkToMainActivity(this); // register for receiving portions of result \\

        final MainHub.UiLink uiLink = new MainUi(findViewById(R.id.mainActivityRootView));

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    } // onCreate \\

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
    public void onBackPressed() {
        super.onBackPressed();
        interruptPerformanceTest();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        // possibly launched test is meant to continue running in this case \\
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        ((App) getApplication()).setLinkToMainActivity(null); // preventing possible memory leak here \\
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
        etBasicString.setEnabled(!isJobRunning);
        etStringsAmount.setEnabled(!isJobRunning);
        etIterationsAmount.setEnabled(!isJobRunning);
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
        final String basicString = etBasicString.getText().toString();
        int count = 0;
        try {
            count = Integer.parseInt(etStringsAmount.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if (count > 0) {
            TestingIntentService.prepareTheBurdenForTest(this, basicString, count);
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
        tvResultForLog.setText(C.STAR);
        tvResultForSAL.setText(C.STAR);
        tvResultForDAL.setText(C.STAR);
        tvResultForVAL.setText(C.STAR);
        tvResultForSout.setText(C.STAR);
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
//            case C.Intent.ACTION_GET_ONE_ITERATION_RESULTS:
//                long[] oneIterationResults = intent.getLongArrayExtra(C.Intent.NAME_ALL_TIME);
//                L.restore();
//                L.w("selectInfoToShow",
//                        " oneIterationResults = " + Arrays.toString(oneIterationResults));
//                L.silence();
//                storeToIntegralResult(oneIterationResults);
//                showPreparationsResult(calculateMedianResult());
//                return;
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
        int count;
        try {
            count = Integer.parseInt(etIterationsAmount.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            count = 0;
        }
        // condition in the main loop will work only for count > 0 but any numbers are safe there \\
        totalResultList.clear();
        TestingIntentService.launchAllMeasurements(this, count);
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
        final int listSize = totalResultList.size();
        L.w("calculateMedianResult", "listSize = " + listSize);
        final long[] medianArray = new long[C.Order.VARIANTS_TOTAL];
        if (totalResultList.isEmpty()) { // anyway we should not fall inside this check \\
            // avoiding division by zero in the loop just after this check \\
            return medianArray; // empty here \\
        }
        long sumForLog = 0;
        long sumForSAL = 0;
        long sumForDAL = 0;
        long sumForVAL = 0;
        long sumForSout = 0;
        for (long[] array : totalResultList) {
            L.w("calculateMedianResult", "" + Arrays.toString(array));
            // i hope we'll avoid exceeding the max value for type long \\
            sumForLog += array[C.Order.INDEX_OF_LOG];
            sumForSAL += array[C.Order.INDEX_OF_SAL];
            sumForDAL += array[C.Order.INDEX_OF_DAL];
            sumForVAL += array[C.Order.INDEX_OF_VAL];
            sumForSout += array[C.Order.INDEX_OF_SOUT];
        }
        medianArray[C.Order.INDEX_OF_LOG] = sumForLog / listSize;
        medianArray[C.Order.INDEX_OF_SAL] = sumForSAL / listSize;
        medianArray[C.Order.INDEX_OF_DAL] = sumForDAL / listSize;
        medianArray[C.Order.INDEX_OF_VAL] = sumForVAL / listSize;
        medianArray[C.Order.INDEX_OF_SOUT] = sumForSout / listSize;

        return medianArray;
    }

    private void showPreparationsResult(@Nullable long[] oneIterationResults) {
        if (oneIterationResults == null || oneIterationResults.length != C.Order.VARIANTS_TOTAL) {
            return;
        }
        tvResultForLog.setText(U.adaptForUser(this, oneIterationResults[C.Order.INDEX_OF_LOG]));
        tvResultForSAL.setText(U.adaptForUser(this, oneIterationResults[C.Order.INDEX_OF_SAL]));
        tvResultForDAL.setText(U.adaptForUser(this, oneIterationResults[C.Order.INDEX_OF_DAL]));
        tvResultForVAL.setText(U.adaptForUser(this, oneIterationResults[C.Order.INDEX_OF_VAL]));
        tvResultForSout.setText(U.adaptForUser(this, oneIterationResults[C.Order.INDEX_OF_SOUT]));
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
                tvResultForLog.setText(U.adaptForUser(this, resultNanoTime));
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

    // TODO: 18.11.2017 use android.os.CpuUsageInfo
    // TODO: 18.11.2017 use android.os.Debug.MemoryInfo \\
    // TODO: 18.11.2017 use android.os.Process \\
    // TODO: 18.11.2017 use android.util.DebugUtils \\

    /*
    // add into info screen - from java.lang.System \ getProperties() \\
    Key                             Description of Associated Value
    java.version	                Java Runtime Environment version
    java.vendor	                    Java Runtime Environment vendor
    java.vendor.url	                Java vendor URL
    java.home	                    Java installation directory
    java.vm.specification.version	Java Virtual Machine specification version
    java.vm.specification.vendor	Java Virtual Machine specification vendor
    java.vm.specification.name	    Java Virtual Machine specification name
    java.vm.version	                Java Virtual Machine implementation version
    java.vm.vendor	                Java Virtual Machine implementation vendor
    java.vm.name	                Java Virtual Machine implementation name
    java.specification.version	    Java Runtime Environment specification version
    java.specification.vendor	    Java Runtime Environment specification vendor
    java.specification.name	        Java Runtime Environment specification name
    java.class.version	            Java class format version number
    java.class.path	                Java class path
    java.library.path	            List of paths to search when loading libraries
    java.io.tmpdir	                Default temp file path
    java.compiler	                Name of JIT compiler to use
    java.ext.dirs	                Path of extension directory or directories
    os.name	                        Operating system name
    os.arch	                        Operating system architecture
    os.version	                    Operating system version
    file.separator	                File separator ("/" on UNIX)
    path.separator	                Path separator (":" on UNIX)
    line.separator	                Line separator ("\n" on UNIX)
    user.name	                    User's account name
    user.home	                    User's home directory
    user.dir	                    User's current working directory
    */
}