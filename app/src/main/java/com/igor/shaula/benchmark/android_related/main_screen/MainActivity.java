package com.igor.shaula.benchmark.android_related.main_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.igor.shaula.benchmark.R;
import com.igor.shaula.benchmark.android_related.TestingIntentService;
import com.igor.shaula.benchmark.android_related.additional_screens.SettingsActivity;
import com.igor.shaula.benchmark.logic_engine.DataTransport;
import com.igor.shaula.benchmark.logic_engine.JobHolder;
import com.igor.shaula.benchmark.utils.C;
import com.igor.shaula.benchmark.utils.L;
import com.igor.shaula.benchmark.utils.U;

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

//        setContentView(R.layout.main_activity_root_with_top_toolbar);
        setContentView(R.layout.main_activity_root);

        logicLink = new MainLogic(this,
                new MainUi(findViewById(R.id.mainActivityRootView)),
                getDataTransport());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_toggleLoadPreparationBlock) {
            logicLink.onLoadPreparationBlockAction();
            // as it's hard to avoid linking to Android classes in logic - toggling the icon is here \\
            item.setIcon(logicLink.isPreparationBlockShown() ?
                    R.drawable.ic_close_preparation_block : R.drawable.ic_open_preparation_block);
            return true;
        } else if (id == R.id.action_showSettingsActivity) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_toggleExplanations) {
            logicLink.onToggleAllExplanationsAction();
        } else if (id == R.id.action_about) {
            logicLink.onShowDialogWithBuildInfoAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // FROM SystemLink =============================================================================

    @NonNull
    @Override
    public DataTransport getDataTransport() {
        return (DataTransport) getApplication();
    }

    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    @NonNull
    @Override
    public String getLoad() {
        // longStringForTest may be null - but it's normally processed by all our logging variants \\
        final String longStringForTest = getDataTransport().getLongStringForTest();
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
        final String[] unitsOfMeasurements = new String[]{
                getString(R.string.nanos),
                getString(R.string.micros),
                getString(R.string.millis),
                getString(R.string.seconds)
        };
        return U.adaptForUser(unitsOfMeasurements, resultNanoTime);
    }

    @NonNull
    @Override
    public String findStringById(int stringId) {
        return getString(stringId);
    }

/*
    @Override
    public void toggleAppBarExpansionIcon(boolean shouldCollapseAbbBar) {
        if (menu == null) {
            return;
        }
        final MenuItem toggleAppBarExpansionMenuItem = menu.findItem(R.id.action_toggleLoadPreparationBlock);
        if (shouldCollapseAbbBar) {
//            toggleAppBarExpansionMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_close_preparation_block));
            toggleAppBarExpansionMenuItem.setIcon(R.drawable.ic_close_preparation_block);
        } else {
//            toggleAppBarExpansionMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_open_preparation_block));
            toggleAppBarExpansionMenuItem.setIcon(R.drawable.ic_open_preparation_block);
        }
        // TODO: 14.06.2018 protect after W/View: requestLayout() improperly called by android.support.design.widget.CollapsingToolbarLayout
    }
*/

    @Override
    public void launchPreparation(@NonNull String basicString, int basicStringsCount) {
        TestingIntentService.prepareTheLoadForTest(this, basicString, basicStringsCount);
    }

    @Override
    public void launchAllMeasurements(int testRepetitionsCount) {
        JobHolder.getInstance(0).launchAllMeasurements(this, testRepetitionsCount);
    }

    @Override
    public void forbidIterationsJob(boolean isForbiddenToRunIterations) {
        getDataTransport().forbidIterationsJob(isForbiddenToRunIterations);
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
}