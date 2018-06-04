package com.igor.shaula.string_benchmark.payload_jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.app_components.main_screen.MainHub;

import java.util.Timer;
import java.util.TimerTask;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "just to lighten MainLogic")
public class TextyTwister {

    private static final char[] CHARS = {'-', '\\', '|', '/', '-'};

    private int twisterCounter;

    @Nullable
    private Timer twisterTimer;

//    @NonNull
//    private final MainHub.LogicLink logicLink;

//    public TextyTwister(@NonNull MainHub.LogicLink logicLink) {
//        this.logicLink = logicLink;
//    }

    public void showTextyTwister(@NonNull final MainHub.LogicLink logicLink) {
        final int[] index = new int[1];
        final String[] textForUI = new String[1];
        final TimerTask twisterTask = new TimerTask() {
            @Override
            public void run() {
                // 0 - 1 - 2 - 3 - 0 - 1 - 2 - 3 - 0 - ...
                index[0] = twisterCounter % CHARS.length;
                textForUI[0] = String.valueOf(CHARS[index[0]]);
                logicLink.updatePreparationResult(textForUI[0]);
                twisterCounter++;
            }
        };
        twisterTimer = new Timer(true);
        twisterTimer.schedule(twisterTask, 0, 80); // every 5 frames = 12 times per second \\
    }

    public void stopTwisterTimer() {
        if (twisterTimer != null) {
            twisterTimer.cancel(); // purge() behaves very strangely - so i decided to avoid it
        }
        twisterTimer = null;
        twisterCounter = 0;
    }
}