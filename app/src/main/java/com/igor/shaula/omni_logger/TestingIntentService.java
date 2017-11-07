package com.igor.shaula.omni_logger;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.MeDoc;
import com.igor.shaula.omni_logger.annotations.TypeDoc;
import com.igor.shaula.omni_logger.utils.C;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "06-11-2017", purpose = "" +
        "simplest way of performing heavy jobs queue on the separate thread")

public class TestingIntentService extends IntentService {

    private static final String CN = "TestingIntentService";

//    private static final String ACTION_FOO = "com.igor.shaula.omni_logger.action.FOO";
//    private static final String EXTRA_PARAM1 = "com.igor.shaula.omni_logger.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.igor.shaula.omni_logger.extra.PARAM2";

    public TestingIntentService() {
        super(CN);
    }

    //    public static void startActionFoo(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, TestingIntentService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        final String intentAction = intent.getAction();
        if (intentAction == null) {
            return;
        }
        if (intentAction.equals(C.Intent.ACTION_START_BURDEN_PREPARATION)) {
            final int loopIterationsCount = intent.getIntExtra(C.Intent.NAME_COUNT, 0);
            if (loopIterationsCount > 0) {
                prepareInitialBurden(loopIterationsCount);
            }
        } else {
            Log.w(CN, "onHandleIntent ` unknown intentAction: " + intentAction);
        }
    }

    @MeDoc("this is launched in the worker thread only")
    private void prepareInitialBurden(int count) {
        // TODO: 06.11.2017 later add the ability to set initial String roaster by the user itself \\
        final String[] initialStringSource = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        // for now it's decided to leave this String array here, not moving it into constants \\

        long nanoTime = System.nanoTime();
        System.out.println("before preparing the string for logger @ " + nanoTime);
        final StringBuilder longStringForTestBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            for (String string : initialStringSource) {
                longStringForTestBuilder.append(string);
            }
        }
        final String longStringForTest = longStringForTestBuilder.toString();
        long nanoTimeDelta = System.nanoTime() - nanoTime;
        System.out.println("after preparing the string for logger @ " + nanoTimeDelta);

        notifyUserAboutPreparationFinish(nanoTimeDelta);

        Log.v(CN, "prepareInitialBurden = " + longStringForTest);

/*        {
            long nanoTime = System.nanoTime();
            System.out.println("starting standard logger @ " + nanoTime);
            Log.v("LOG", longStringForTest);
            System.out.println("ending standard logger @ " + (System.nanoTime() - nanoTime));
            // 186875 ns - for 1000 iterations
            // 602605 ns - for 10_000 iterations
            // 618177 ns - for 10_000 iterations
        }
        {
            long nanoTime = System.nanoTime();
            System.out.println("starting custom logger @ " + nanoTime);
            VAL.v("VAL", longStringForTest);
            System.out.println("ending custom logger @ " + (System.nanoTime() - nanoTime));
            // 347604 ns - for 1000 iterations
            // 1262656 ns - for 10_000 iterations
            // 4720677 ns - for 10_000 iterations
        }*/
    }

    private void notifyUserAboutPreparationFinish(long nanoTimeDelta) {
        final Intent intent = new Intent(C.Intent.ACTION_GET_PREPARATION_RESULT);
        // You can also include some extra data.
        intent.putExtra(C.Intent.NAME_PREPARATION_TIME, nanoTimeDelta);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
