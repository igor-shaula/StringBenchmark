package com.igor.shaula.string_benchmark.screens;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public final class MainLogic implements MainHub.LogicLink {

    private static final String CN = "MainLogic";

    private static final char[] CHARS = {'-', '\\', '|', '/', '-'};

    private boolean isJobRunning;

    private int counter;
    @NonNull
    private List<long[]> totalResultList = new LinkedList<>();

    @NonNull
    private String pendingPreparationResult = "";

    @Nullable
    private Timer twisterTimer;

}