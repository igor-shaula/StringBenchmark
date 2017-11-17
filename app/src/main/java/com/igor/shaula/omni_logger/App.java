package com.igor.shaula.omni_logger;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.omni_logger.annotations.TypeDoc;
import com.igor.shaula.omni_logger.utils.L;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "13-11-2017", purpose = "" +
        "fastest & easiest way of preserving the burden from being destroyed with IntentService")

public final class App extends Application {

    @Nullable
    private Callback callback;
    @Nullable
    private String longStringForTest = "";

    @Nullable
    public String getLongStringForTest() {
        return longStringForTest;
    }

    public void setLongStringForTest(@Nullable String longStringForTest) {
        this.longStringForTest = longStringForTest;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.silence();
    }

    public void setCallback(@NonNull Callback mainActivity) {
        this.callback = mainActivity;
    }

    public void transportOneIterationsResult(@NonNull long[] oneIterationsResult) {
        if (callback != null) {
            callback.transportOneIterationsResult(oneIterationsResult);
        }
    }

    interface Callback { // implemented by MainActivity \\

        void transportOneIterationsResult(@NonNull long[] oneIterationsResult);
    }
}