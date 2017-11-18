package com.igor.shaula.omni_logger;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.omni_logger.annotations.TypeDoc;
import com.igor.shaula.omni_logger.utils.L;

import java.util.List;

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

    public void transportOneIterationsResult(@NonNull List<Long> oneIterationsResult) {
        if (callback != null) {
            callback.transportOneIterationsResult(convertIntoArray(oneIterationsResult));
        }
    }

    @NonNull
    private long[] convertIntoArray(@NonNull List<Long> list) {
        final int size = list.size();
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    interface Callback { // implemented by MainActivity \\

        void transportOneIterationsResult(@NonNull long[] oneIterationsResult);

//        void transportOneIterationsResult(@NonNull List<Long> oneIterationsResult);
    }
}