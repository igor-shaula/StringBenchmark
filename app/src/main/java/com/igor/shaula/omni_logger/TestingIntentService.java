package com.igor.shaula.omni_logger;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.MeDoc;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TestingIntentService extends IntentService {

    private static final String CN = "TestingIntentService";

    private static Handler UI_HANDLER = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            Log.d(CN, "handleMessage: nanoTime of creation = " + message.arg2);
            if (message.arg1 == C.Choice.PREPARATION) {
//                final String existingText = tvResultOfPreparation.getText().toString();
//                final String textForUI = existingText + C.SPACE + message.arg2;
//                tvResultOfPreparation.setText(textForUI);
//                return true;
            }
//            super.handleMessage(message);
        }
    };

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
//    private static final String ACTION_FOO = "com.igor.shaula.omni_logger.action.FOO";
//    private static final String ACTION_BAZ = "com.igor.shaula.omni_logger.action.BAZ";

    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "com.igor.shaula.omni_logger.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.igor.shaula.omni_logger.extra.PARAM2";

    public TestingIntentService() {
        super(CN);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionFoo(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, TestingIntentService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, TestingIntentService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final int count = intent.getIntExtra(C.Intent.NAME_COUNT, 0);
            if (count > 0) {
                prepareInitialBurden(count);
            }
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
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
        // 918760833 - for 1_000 iterations - almost 1 second - without using StringBuilder \\
        // 67333459506 - for 10_000 iterations - 67 seconds
        // 67461031537 - for 10_000 iterations - 67 seconds

        final int timeDeltaInt = (int) nanoTimeDelta;
        // we need to check that type conversion has given reasonable result \\
        if (timeDeltaInt > 0 & timeDeltaInt < Integer.MAX_VALUE) {
            notifyUserAboutPreparationFinish(timeDeltaInt);
        } else {
            Log.e(CN, "prepareInitialBurden: timeDeltaInt is wrong: " + timeDeltaInt);
        }
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

    private void notifyUserAboutPreparationFinish(int timeDeltaInt) {
/*
        Log.d(CN, "prepareInitialBurden() finished with: timeDeltaInt = [" + timeDeltaInt + "]");
        // for passing info about burden creation it's decided to use Handler & Message \\
        final Message message = Message.obtain(UI_HANDLER);
        message.arg1 = C.Choice.PREPARATION;
        message.arg2 = timeDeltaInt;
//            UI_HANDLER.sendMessage(message);
        UI_HANDLER.handleMessage(message);

        Log.d(CN, "prepareInitialBurden() obtained message with: timeDeltaInt = [" + UI_HANDLER.obtainMessage().arg2 + "]");
*/

        final Intent intent = new Intent(C.Intent.ACTION);
        // You can also include some extra data.
        intent.putExtra(C.Intent.NAME_PREPARATION_TIME, timeDeltaInt);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
