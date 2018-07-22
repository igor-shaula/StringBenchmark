package com.igor.shaula.string_benchmark.logic_engine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.android_related.TestingIntentService;
import com.igor.shaula.string_benchmark.android_related.main_screen.MainHub;
import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;

@TypeDoc(createdBy = "shaula", createdOn = "10.07.2018", purpose = "" +
        "abstraction for IntentService, background thread, handler or any other working mechanism")
public final class JobHolder {

    private int implementation;
    @Nullable
    private static JobHolder thisInstance;

    private JobHolder(int implementation) {
        this.implementation = implementation;
    }

    @NonNull
    public static JobHolder getInstance(int implementation) {
        if (thisInstance == null) {
            thisInstance = new JobHolder(implementation);
        } else {
            thisInstance.implementation = implementation; // dynamically updating the way \\
        }
        return thisInstance;
    }

    public void launchAllMeasurements(@NonNull MainHub.SystemLink systemLink, int testRepetitionsCount) {
        // TODO: 10.07.2018 later add other variants here \\
        switch (implementation) {
            case 1:
                break;
            case 2:
                break;
            default:
                TestingIntentService.launchAllMeasurements(systemLink.getContext(), testRepetitionsCount);
        }
    }
}