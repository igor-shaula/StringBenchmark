package com.igor.shaula.omni_logger;

import android.app.Application;
import android.support.annotation.Nullable;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "13-11-2017", purpose = "" +
        "fastest & easiest way of preserving the burden from being destroyed with IntentService")

public final class App extends Application {

    @Nullable
    private String longStringForTest = "";

    @Nullable
    public String getLongStringForTest() {
        return longStringForTest;
    }

    public void setLongStringForTest(@Nullable String longStringForTest) {
        this.longStringForTest = longStringForTest;
    }
}