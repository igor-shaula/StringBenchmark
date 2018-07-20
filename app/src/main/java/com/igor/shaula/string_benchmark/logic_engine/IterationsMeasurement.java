package com.igor.shaula.string_benchmark.logic_engine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.double_args_logger.DAL;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.superior_logger.SLInt;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.superior_logger.SLVoid;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.var_args_logger_1_safe.VAL1;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.var_args_logger_2_configurable.VAL2;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.var_args_logger_3_objects.VAL3;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;

import java.util.Map;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "abstract job - not in Service only")
public final class IterationsMeasurement {

    private static final String CN = "IterationsMeasurement";

    public void measurePerformanceInLoop(@NonNull final DataTransport dataTransport,
                                         final int howManyIterations) {

        // longStringForTest may be null - but it's normally processed by all our logging variants \\
        final String longStringForTest = dataTransport.getLongStringForTest();
        final Map<String, Long> oneIterationResults = dataTransport.getInitialEmptyMap();

        // this loop is global - all measurements run inside it \\
        for (int i = 0; i < howManyIterations; i++) {
            // at first checking if it has been marked for stop \\
            if (!dataTransport.isAllowedToRunIterations()) {
                L.i(CN, "measurePerformanceInLoop ` isMarkedForStop worked -> stopping now");
                dataTransport.stopIterations();
                break;
            }
            oneIterationResults.clear();

            // trying to exclude strange numbers for the first test method by pre-heating it \\
            runSoutMethod(longStringForTest);
            oneIterationResults.put(C.Key.KEY_SOUT, runSoutMethod(longStringForTest));
            // pre-heating all other methods to avoid their slowing down for the first time invoked \\

            runLogMethod(longStringForTest);
            oneIterationResults.put(C.Key.KEY_LOG, runLogMethod(longStringForTest));

            runDalMethod(longStringForTest);
            oneIterationResults.put(C.Key.KEY_DAL, runDalMethod(longStringForTest));

            runVal1Method(longStringForTest);
            oneIterationResults.put(C.Key.KEY_VAL_1, runVal1Method(longStringForTest));

            runVal2Method(longStringForTest);
            oneIterationResults.put(C.Key.KEY_VAL_2, runVal2Method(longStringForTest));

            runVal3Method(longStringForTest);
            oneIterationResults.put(C.Key.KEY_VAL_3, runVal3Method(longStringForTest));

            runSLVoidMethod(longStringForTest);
            oneIterationResults.put(C.Key.KEY_SL_VOID, runSLVoidMethod(longStringForTest));

            runSLIntMethod(longStringForTest);
            oneIterationResults.put(C.Key.KEY_SL_INT, runSLIntMethod(longStringForTest));
/*
            // as this part of code is hot - no need of debug logging here during normal usage \\
            L.w("measurePerformanceInLoop", "i = " + i +
                    " oneIterationResults = " + oneIterationResults);
*/
            dataTransport.transportOneIterationsResult(oneIterationResults, i);
        }
    }

    private long runSoutMethod(@Nullable String longStringForTest) {
        long soutNanoTime = System.nanoTime();
        System.out.println(longStringForTest);
        return System.nanoTime() - soutNanoTime;
    }

    private long runLogMethod(@Nullable String longStringForTest) {
        long logNanoTime = System.nanoTime();
        Log.v(CN, longStringForTest);
        return System.nanoTime() - logNanoTime;
    }

    private long runDalMethod(@Nullable String longStringForTest) {
        long dalNanoTime = System.nanoTime();
        DAL.v(CN, longStringForTest);
        return System.nanoTime() - dalNanoTime;
    }

    private long runVal1Method(@Nullable String longStringForTest) {
        long val1NanoTime = System.nanoTime();
        VAL1.v(longStringForTest);
        return System.nanoTime() - val1NanoTime;
    }

    private long runVal2Method(@Nullable String longStringForTest) {
        long val2NanoTime = System.nanoTime();
        VAL2.v(longStringForTest);
        return System.nanoTime() - val2NanoTime;
    }

    private long runVal3Method(@Nullable String longStringForTest) {
        long val3NanoTime = System.nanoTime();
        VAL3.v(longStringForTest);
        return System.nanoTime() - val3NanoTime;
    }

    private long runSLVoidMethod(@Nullable String longStringForTest) {
        long salNanoTime = System.nanoTime();
        SLVoid.v(longStringForTest);
        return System.nanoTime() - salNanoTime;
    }

    private long runSLIntMethod(@Nullable String longStringForTest) {
        long val0NanoTime = System.nanoTime();
        SLInt.v(longStringForTest);
        return System.nanoTime() - val0NanoTime;
    }
}