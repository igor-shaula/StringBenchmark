package com.igor.shaula.string_benchmark.screens;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.App;
import com.igor.shaula.string_benchmark.DataTransport;
import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.utils.L;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "25-11-2017", purpose = "",
        comment = "logic should not have any Android-specific imports & dependencies - only pure Java")

public final class MainLogic implements MainHub.LogicLink, App.Callback {

    private static final String CN = "MainLogic";

    private static final char[] CHARS = {'-', '\\', '|', '/', '-'};

    @NonNull
    private final MainHub.SystemLink mainActivity;
    @NonNull
    private final MainHub.UiLink uiLink;
    @NonNull
    private final DataTransport dataTransport;

    private boolean isJobRunning;

    private int counter;
    @NonNull
    private List<long[]> totalResultList = new LinkedList<>();

    @NonNull
    private String pendingPreparationResult = "";

    @Nullable
    private Timer twisterTimer;

    MainLogic(@NonNull MainHub.SystemLink mainActivity,
              @NonNull MainHub.UiLink uiLink,
              @NonNull DataTransport dataTransport) {
        this.mainActivity = mainActivity;
        this.uiLink = uiLink;
        this.dataTransport = dataTransport;
        uiLink.setLogicLink(this);
        dataTransport.setDataConsumer(this); // register for receiving portions of result \\
    }

    @MeDoc("invoked in activity's onStop")
    @Override
    public void unLinkDataTransport() {
        dataTransport.setDataConsumer(null); // preventing possible memory leak here \\
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
}