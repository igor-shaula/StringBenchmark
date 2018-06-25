package com.igor.shaula.string_benchmark.payload_jobs;

import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.app_components.main_screen.MainHub;
import com.igor.shaula.string_benchmark.app_components.main_screen.for_ui.OneIterationResultModel;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "iterations result processor")
public class IterationResultConsumer implements DataTransport.IterationResultConsumer {

    private static final String CN = "IterationResultConsumer";

    @NonNull
    private MainHub.LogicLink logicLink;

    @NonNull
    private List<Map<String, Long>> totalResultList = new LinkedList<>();

    public IterationResultConsumer(@NonNull MainHub.LogicLink logicLink) {
        this.logicLink = logicLink;
    }

    @Override
    public void onNewIterationResult(@NonNull Map<String, Long> oneIterationsResult, int currentIterationNumber) {
        L.w("onNewIterationResult",
                " oneIterationsResult = " + oneIterationsResult);
        totalResultList.add(oneIterationsResult);
        final Map<String, Long> results = calculateMedianResult();
        final List<OneIterationResultModel> resultModelList = new ArrayList<>(results.size());
        for (String key : results.keySet()) {
            resultModelList.add(new OneIterationResultModel(key, results.get(key)));
        }
        logicLink.transportIterationsResult(resultModelList, currentIterationNumber);
    }

    @Override
    public void prepareForNewJob() {
        totalResultList.clear();
    }

    @NonNull
    private Map<String, Long> calculateMedianResult() {
        final int listSize = totalResultList.size();
        L.w(CN, "calculateMedianResult ` listSize = " + listSize);
        final Map<String, Long> medianMap = new HashMap<>();
        if (totalResultList.isEmpty()) { // anyway we should not fall inside this check \\
            // avoiding division by zero in the loop just after this check \\
            return medianMap; // empty here \\
        }
        long sumForSout = 0;
        long sumForLog = 0;
        long sumForDAL = 0;
        long sumForVAL1 = 0;
        long sumForVAL2 = 0;
        long sumForVAL3 = 0;
        long sumForSLVoid = 0;
        long sumForSLInt = 0;
        for (Map<String, Long> oneIterationResult : totalResultList) {
            L.w(CN, "calculateMedianResult: " + oneIterationResult);
            // i hope we'll avoid exceeding the max value for type long \\
            sumForSout += oneIterationResult.get(C.Key.KEY_SOUT);
            sumForLog += oneIterationResult.get(C.Key.KEY_LOG);
            sumForDAL += oneIterationResult.get(C.Key.KEY_DAL);
            sumForVAL1 += oneIterationResult.get(C.Key.KEY_VAL_1);
            sumForVAL2 += oneIterationResult.get(C.Key.KEY_VAL_2);
            sumForVAL3 += oneIterationResult.get(C.Key.KEY_VAL_3);
            sumForSLVoid += oneIterationResult.get(C.Key.KEY_SL_VOID);
            sumForSLInt += oneIterationResult.get(C.Key.KEY_SL_INT);
        }
        medianMap.put(C.Key.KEY_SOUT, sumForSout / listSize);
        medianMap.put(C.Key.KEY_LOG, sumForLog / listSize);
        medianMap.put(C.Key.KEY_DAL, sumForDAL / listSize);
        medianMap.put(C.Key.KEY_VAL_1, sumForVAL1 / listSize);
        medianMap.put(C.Key.KEY_VAL_2, sumForVAL2 / listSize);
        medianMap.put(C.Key.KEY_VAL_3, sumForVAL3 / listSize);
        medianMap.put(C.Key.KEY_SL_VOID, sumForSLVoid / listSize);
        medianMap.put(C.Key.KEY_SL_INT, sumForSLInt / listSize);

        return medianMap;
    }
}