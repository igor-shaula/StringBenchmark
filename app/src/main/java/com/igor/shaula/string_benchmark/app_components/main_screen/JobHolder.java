package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.app_components.TestingIntentService;

@TypeDoc(createdBy = "shaula", createdOn = "10.07.2018", purpose = "" +
        "abstraction for IntentService, background thread, handler or any other working mechanism")
class JobHolder {

    private int implementation;
    @Nullable
    private static JobHolder thisInstance;

    private JobHolder(int implementation) {
        this.implementation = implementation;
    }

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
                TestingIntentService.getInstance()
                        .launchAllMeasurements(systemLink.getContext(), testRepetitionsCount);
        }
//        TestingIntentService.launchAllMeasurements(systemLink.getContext(), testRepetitionsCount);
    }
}