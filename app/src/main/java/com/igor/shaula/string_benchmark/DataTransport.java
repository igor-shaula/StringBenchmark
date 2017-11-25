package com.igor.shaula.string_benchmark;

import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.screens.MainHub;

public interface DataTransport {

    void setDataConsumer(@Nullable MainHub.LogicLink logicLink);

    void setDataConsumer(@Nullable App.Callback mainActivity);

    @Nullable
    public String getLongStringForTest();
}