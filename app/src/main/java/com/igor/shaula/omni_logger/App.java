package com.igor.shaula.omni_logger;

import android.app.Application;
import android.support.annotation.Nullable;

public final class App extends Application {

    @Nullable
    private static String staticLongStringForTest = "";
    @Nullable
    private String longStringForTest = "";

    @Nullable
    public static String getStaticLongStringForTest() {
        return staticLongStringForTest;
    }

    public static void setStaticLongStringForTest(@Nullable String staticLongStringForTest) {
        App.staticLongStringForTest = staticLongStringForTest;
    }

    @Nullable
    public String getLongStringForTest() {
        return longStringForTest;
    }

    public void setLongStringForTest(@Nullable String longStringForTest) {
        this.longStringForTest = longStringForTest;
    }
}