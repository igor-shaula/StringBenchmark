package com.igor.shaula.string_benchmark.logic_engine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.double_args_logger.DAL;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.superior_logger.SLInt;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.superior_logger.SLVoid;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.var_args_logger_1_safe.VAL1;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.var_args_logger_2_configurable.VAL2;
import com.igor.shaula.string_benchmark.tested_payload.log_wrappers.var_args_logger_3_objects.VAL3;
import com.igor.shaula.string_benchmark.utils.C;
import com.igor.shaula.string_benchmark.utils.L;
import com.igor.shaula.string_benchmark.utils.annotations.MeDoc;
import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;

import java.util.LinkedHashMap;
import java.util.Map;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "abstract job - not in Service only")
public final class IterationsMeasurement {

    private static final String CN = "IterationsMeasurement";

    @MeDoc("should return default values if howManyIterations = 0 due to usage of single keys here")
    public static void measurePerformanceInLoop(@NonNull final DataTransport dataTransport,
                                                final int howManyIterations) {

        // longStringForTest may be null - but it's normally processed by all our logging variants \\
        final String longStringForTest = dataTransport.getLongStringForTest();
        final Map<String, Long> oneIterationResults = new LinkedHashMap<>();

        // this loop is global - all measurements run inside it \\
        for (int i = 0; i <= howManyIterations; i++) {
            // at first checking if it has been marked for stop \\
            if (dataTransport.isForbiddenToRunIterations()) {
                L.i(CN, "measurePerformanceInLoop ` isMarkedForStop worked -> stopping now");
                dataTransport.stopIterations();
                break;
            }
            oneIterationResults.clear(); // it's always better to be sure in clean container \\
            /*
            previously used to try reducing of garbage collector impact =
            trying to exclude strange numbers for the first test method by pre-heating it \
            pre-heating all other methods to avoid their slowing down for the first time invoked \
            */
            oneIterationResults.put(C.Key.KEY_SOUT, i == 0 ? 0L : runSoutMethod(longStringForTest));

            oneIterationResults.put(C.Key.KEY_LOG, i == 0 ? 0L : runLogMethod(longStringForTest));

            oneIterationResults.put(C.Key.KEY_DAL, i == 0 ? 0L : runDalMethod(longStringForTest));

            oneIterationResults.put(C.Key.KEY_VAL_1, i == 0 ? 0L : runVal1Method(longStringForTest));

            oneIterationResults.put(C.Key.KEY_VAL_2, i == 0 ? 0L : runVal2Method(longStringForTest));

            oneIterationResults.put(C.Key.KEY_VAL_3, i == 0 ? 0L : runVal3Method(longStringForTest));

            oneIterationResults.put(C.Key.KEY_SL_VOID, i == 0 ? 0L : runSLVoidMethod(longStringForTest));

            oneIterationResults.put(C.Key.KEY_SL_INT, i == 0 ? 0L : runSLIntMethod(longStringForTest));
/*
            // as this part of code is hot - no need of debug logging here during normal usage \\
            L.w("measurePerformanceInLoop", "i = " + i +
                    " oneIterationResults = " + oneIterationResults);
*/
            dataTransport.transportOneIterationsResult(oneIterationResults, i);
        }
    }

    private static long runSoutMethod(@Nullable String longStringForTest) {
        long soutNanoTime = System.nanoTime();
        System.out.println(longStringForTest);
        return System.nanoTime() - soutNanoTime;
    }

    private static long runLogMethod(@Nullable String longStringForTest) {
        long logNanoTime = System.nanoTime();
        Log.v(CN, longStringForTest);
        return System.nanoTime() - logNanoTime;
    }

    private static long runDalMethod(@Nullable String longStringForTest) {
        long dalNanoTime = System.nanoTime();
        DAL.v(CN, longStringForTest);
        return System.nanoTime() - dalNanoTime;
    }

    private static long runVal1Method(@Nullable String longStringForTest) {
        long val1NanoTime = System.nanoTime();
        VAL1.v(longStringForTest);
        return System.nanoTime() - val1NanoTime;
    }

    private static long runVal2Method(@Nullable String longStringForTest) {
        long val2NanoTime = System.nanoTime();
        VAL2.v(longStringForTest);
        return System.nanoTime() - val2NanoTime;
    }

    private static long runVal3Method(@Nullable String longStringForTest) {
        long val3NanoTime = System.nanoTime();
        VAL3.v(longStringForTest);
        return System.nanoTime() - val3NanoTime;
    }

    private static long runSLVoidMethod(@Nullable String longStringForTest) {
        long salNanoTime = System.nanoTime();
        SLVoid.v(longStringForTest);
        return System.nanoTime() - salNanoTime;
    }

    private static long runSLIntMethod(@Nullable String longStringForTest) {
        long val0NanoTime = System.nanoTime();
        SLInt.v(longStringForTest);
        return System.nanoTime() - val0NanoTime;
    }
}