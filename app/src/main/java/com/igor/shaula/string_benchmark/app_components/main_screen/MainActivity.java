package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.app_components.TestingIntentService;
import com.igor.shaula.string_benchmark.app_components.additional_screens.PrefsFragment0;
import com.igor.shaula.string_benchmark.app_components.additional_screens.SettingsActivity;
import com.igor.shaula.string_benchmark.payload_jobs.DataTransport;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.U;

public final class MainActivity extends AppCompatActivity implements MainHub.SystemLink {

    private static final String CN = "MainActivity";
    @SuppressWarnings("NullableProblems") // initialized in OnCreate & lasts all the lifetime \\
    @NonNull
    private MainHub.LogicLink logicLink;
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

//        setContentView(R.layout.activity_main_linear);
        setContentView(R.layout.root_coordinator_layout);

        logicLink = new MainLogic(this,
                new MainUi(findViewById(R.id.mainActivityRootView)),
                (DataTransport) getApplication()
        );
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // only setting the basement for more abstract interface communication used later \\
        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        // for now it's decided to use local receiver, but any other variant can ve used \\
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_PREPARATION_RESULT));
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_GET_ONE_ITERATION_RESULTS));
        localBroadcastManager.registerReceiver(messageReceiver, new IntentFilter(C.Intent.ACTION_ON_SERVICE_STOPPED));
        logicLink.linkDataTransport();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        logicLink.onBackPressed();
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
        logicLink.unLinkDataTransport();
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
//        if (id == R.id.action_showPrefsFragmentHere) {
//            logicLink.togglePrefsFragmentHere();
//             as it's hard to avoid linking to Android classes in logic - toggling the icon is here \\
//            item.setIcon(logicLink.isPrefsFragmentShownHere() ?
//                    android.R.drawable.ic_dialog_alert : android.R.drawable.ic_dialog_info);
//            return true;
//        } else if (id == R.id.action_showSettingsActivity) {
        if (id == R.id.action_showSettingsActivity) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_about) {
            logicLink.showDialogWithBuildInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // FROM SystemLink =============================================================================

    @NonNull
    @Override
    public String getLoad() {
        final DataTransport appLink = (DataTransport) getApplication();
        // longStringForTest may be null - but it's normally processed by all our logging variants \\
        final String longStringForTest = appLink.getLongStringForTest();
        if (U.isAnyOneNullOrEmpty(longStringForTest)) {
            return "";
        } else {
            //noinspection ConstantConditions
            return longStringForTest;
        }
    }

    @NonNull
    @Override
    public String getAdaptedString(long resultNanoTime) {
        return U.adaptForUser(this, resultNanoTime);
    }

    @NonNull
    @Override
    public String findStringById(int stringId) {
        return getString(stringId);
    }

    @Override
    public void togglePrefsFragment(boolean shouldShow) {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (shouldShow) {
            fragmentTransaction.replace(R.id.flPrefsContainer, PrefsFragment0.getInstance(), "");
        } else {
            fragmentTransaction.remove(PrefsFragment0.getInstance());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void launchPreparation(@NonNull String basicString, int basicStringsCount) {
        TestingIntentService.prepareTheLoadForTest(this, basicString, basicStringsCount);
    }

    @Override
    public void launchAllMeasurements(int testRepetitionsCount) {
        TestingIntentService.launchAllMeasurements(this, testRepetitionsCount);
    }

    @Override
    public void allowIterationsJob(boolean isAllowedToRunIterations) {
        ((DataTransport) getApplication()).allowIterationsJob(isAllowedToRunIterations);
    }

    @Override
    public void stopTestingService() {
        stopService(new Intent(this, TestingIntentService.class));
    }

    @Override
    public void finishItself() {
        L.w(CN, "finishItself");
        finish();
    }

    // PRIVATE =====================================================================================

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
                logicLink.showPreparationsResult(whatInfoToShow, resultNanoTime);
                logicLink.toggleLoadPreparationJobState(false);
                break;
            case C.Intent.ACTION_ON_SERVICE_STOPPED:
                logicLink.toggleLoadPreparationJobState(false);
                logicLink.toggleIterationsJobState(false);
                break;
            case C.Intent.ACTION_JOB_STOPPED:
                logicLink.toggleIterationsJobState(false);
                break;
            default:
                L.w(CN, "selectInfoToShow ` unknown intentAction = " + intentAction);
        }
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