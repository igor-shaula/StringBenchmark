package com.igor.shaula.string_benchmark.payload_jobs;

import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.app_components.main_screen.MainHub;
import com.igor.shaula.string_benchmark.app_components.main_screen.for_ui.OneIterationResultModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "iterations result processor")
public final class IterationResultConsumer implements DataTransport.IterationResultConsumer {

//    private static final String CN = "IterationResultConsumer";

    @NonNull
    private MainHub.LogicLink logicLink;

    @NonNull
    private List<Map<String, Long>> totalResultList = new LinkedList<>();
    @NonNull
    private Map<String, Long> summarizedResultsMap = new LinkedHashMap<>();
    private Map<String, Long> medianResultMap = new LinkedHashMap<>();

    public IterationResultConsumer(@NonNull MainHub.LogicLink logicLink) {
        this.logicLink = logicLink;
    }

    @NonNull
    @Override
    public List<OneIterationResultModel> getOneIterationResultList() {
        final List<OneIterationResultModel> mockResultList = new ArrayList<>();
//        mockResultList.add();
        return mockResultList;
    }

    @NonNull
    @Override
    public Map<String, Long> getOneIterationResultMap() {
        return medianResultMap;
    }

    @MeDoc("decision made inside here is the reason to be proud about myself for now")
    @Override
    public void onNewIterationResult(@NonNull Map<String, Long> oneIterationsResult, // LinkedHashMap in fact \\
                                     int currentIterationIndex) {

        totalResultList.add(oneIterationsResult);

        if (currentIterationIndex == 0) {
            long oneIterationValue;
            // creating & preparing maps for summarized & median results per iteration \\
            for (final String key : oneIterationsResult.keySet()) {
                oneIterationValue = oneIterationsResult.get(key);
                summarizedResultsMap.put(key, oneIterationValue);
                medianResultMap.put(key, oneIterationValue);
            }
        } else {
            long summarizedValue;
            // the idea here is to avoid increasing number of actions for getting median result \\
            for (final String key : oneIterationsResult.keySet()) {
                summarizedValue = summarizedResultsMap.get(key);
                summarizedResultsMap.put(key, summarizedValue + oneIterationsResult.get(key));
            }
            for (final String key : summarizedResultsMap.keySet()) {
                summarizedValue = summarizedResultsMap.get(key);
                medianResultMap.put(key, summarizedValue / (currentIterationIndex + 1));
            }
        }
        logicLink.transportIterationsResult(medianResultMap, currentIterationIndex);
//        logicLink.transportIterationsResult(U.convertIntoList(medianResultMap), currentIterationIndex);
    }

    @Override
    public void prepareForNewJob() {
        totalResultList.clear();
        summarizedResultsMap.clear();
        medianResultMap.clear();
    }
}