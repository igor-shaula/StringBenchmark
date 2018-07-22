package com.igor.shaula.string_benchmark.logic_engine;

import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.android_related.main_screen.MainHub;
import com.igor.shaula.string_benchmark.utils.annotations.MeDoc;
import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;

import java.util.LinkedHashMap;
import java.util.Map;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "iterations result processor")
public final class IterationResultConsumer {

//    private static final String CN = "IterationResultConsumer";

    @NonNull
    private MainHub.LogicLink logicLink;

    @NonNull
    private Map<String, Long> summarizedResultsMap = new LinkedHashMap<>();

    public IterationResultConsumer(@NonNull MainHub.LogicLink logicLink) {
        this.logicLink = logicLink;
    }

    @MeDoc("decision made inside here is the reason to be proud about myself for now")
    public void onNewIterationResult(@NonNull Map<String, Long> oneIterationsResult, // LinkedHashMap in fact \\
                                     int currentIterationIndex) {
        if (currentIterationIndex == 0) {
            // creating & preparing maps for summarized & median results per iteration \\
            for (final String key : oneIterationsResult.keySet()) {
                summarizedResultsMap.put(key, oneIterationsResult.get(key));
            }
        } else {
            // the idea here is to avoid increasing number of actions for getting median result \\
            for (final String key : oneIterationsResult.keySet()) {
                summarizedResultsMap.put(key, summarizedResultsMap.get(key) + oneIterationsResult.get(key));
            }
        }
        logicLink.transportIterationsResult(summarizedResultsMap, currentIterationIndex);
//        logicLink.transportIterationsResult(U.convertIntoList(medianResultMap), currentIterationIndex);
    }

    public void prepareForNewJob() {
        summarizedResultsMap.clear();
    }
}