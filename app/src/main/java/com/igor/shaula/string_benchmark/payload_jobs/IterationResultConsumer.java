package com.igor.shaula.string_benchmark.payload_jobs;

import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.app_components.main_screen.MainHub;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "iterations result processor")
public class IterationResultConsumer implements DataTransport.IterationResultConsumer {

    private static final String CN = "IterationResultConsumer";

    @NonNull
    private MainHub.LogicLink logicLink;

    @NonNull
    private List<long[]> totalResultList = new LinkedList<>();

    public IterationResultConsumer(@NonNull MainHub.LogicLink logicLink) {
        this.logicLink = logicLink;
    }

    @Override
    public void onNewIterationResult(@NonNull long[] oneIterationsResult, int currentIterationNumber) {
        L.w("onNewIterationResult",
                " oneIterationsResult = " + Arrays.toString(oneIterationsResult));
        totalResultList.add(oneIterationsResult);
        final long[] results = calculateMedianResult();
        logicLink.transportIterationsResult(results, currentIterationNumber);
    }

    @Override
    public void prepareForNewJob() {
        totalResultList.clear();
    }

    @NonNull
    private long[] calculateMedianResult() {
        final int listSize = totalResultList.size();
        L.w(CN, "calculateMedianResult ` listSize = " + listSize);
        final long[] medianArray = new long[C.Order.VARIANTS_TOTAL];
        if (totalResultList.isEmpty()) { // anyway we should not fall inside this check \\
            // avoiding division by zero in the loop just after this check \\
            return medianArray; // empty here \\
        }
        long sumForSout = 0;
        long sumForLog = 0;
        long sumForDAL = 0;
        long sumForVAL1 = 0;
        long sumForVAL2 = 0;
        long sumForVAL3 = 0;
        long sumForSLVoid = 0;
        long sumForSLInt = 0;
        for (long[] array : totalResultList) {
            L.w(CN, "calculateMedianResult: " + Arrays.toString(array));
            // i hope we'll avoid exceeding the max value for type long \\
            sumForSout += array[C.Order.INDEX_OF_SOUT];
            sumForLog += array[C.Order.INDEX_OF_LOG];
            sumForDAL += array[C.Order.INDEX_OF_DAL];
            sumForVAL1 += array[C.Order.INDEX_OF_VAL_1];
            sumForVAL2 += array[C.Order.INDEX_OF_VAL_2];
            sumForVAL3 += array[C.Order.INDEX_OF_VAL_3];
            sumForSLVoid += array[C.Order.INDEX_OF_SL_VOID];
            sumForSLInt += array[C.Order.INDEX_OF_SL_INT];
        }
        medianArray[C.Order.INDEX_OF_SOUT] = sumForSout / listSize;
        medianArray[C.Order.INDEX_OF_LOG] = sumForLog / listSize;
        medianArray[C.Order.INDEX_OF_DAL] = sumForDAL / listSize;
        medianArray[C.Order.INDEX_OF_VAL_1] = sumForVAL1 / listSize;
        medianArray[C.Order.INDEX_OF_VAL_2] = sumForVAL2 / listSize;
        medianArray[C.Order.INDEX_OF_VAL_3] = sumForVAL3 / listSize;
        medianArray[C.Order.INDEX_OF_SL_VOID] = sumForSLVoid / listSize;
        medianArray[C.Order.INDEX_OF_SL_INT] = sumForSLInt / listSize;

        return medianArray;
    }
}