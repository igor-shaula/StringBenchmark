package com.igor.shaula.benchmark.logic_engine;

import android.support.annotation.NonNull;
import android.util.Log;

import com.igor_shaula.base_utils.C;
import com.igor_shaula.base_utils.L;
import com.igor_shaula.base_utils.annotations.MeDoc;
import com.igor_shaula.base_utils.annotations.TypeDoc;
import com.igor_shaula.payload_for_test.log_wrappers.double_args_logger.DAL;
import com.igor_shaula.payload_for_test.log_wrappers.superior_logger.SLInt;
import com.igor_shaula.payload_for_test.log_wrappers.superior_logger.SLVoid;
import com.igor_shaula.payload_for_test.log_wrappers.var_args_logger_1_safe.VAL1;
import com.igor_shaula.payload_for_test.log_wrappers.var_args_logger_2_configurable.VAL2;
import com.igor_shaula.payload_for_test.log_wrappers.var_args_logger_3_objects.VAL3;
import com.igor_shaula.payload_for_test.threads.SwitchingThreads;

import java.util.LinkedHashMap;
import java.util.Map;

//import com.igor_shaula.payload_for_test.log_wrappers.double_args_logger.DAL;
//import com.igor_shaula.payload_for_test.log_wrappers.superior_logger.SLInt;
//import com.igor_shaula.payload_for_test.log_wrappers.superior_logger.SLVoid;
//import com.igor_shaula.payload_for_test.log_wrappers.var_args_logger_1_safe.VAL1;
//import com.igor_shaula.payload_for_test.log_wrappers.var_args_logger_2_configurable.VAL2;
//import com.igor_shaula.payload_for_test.log_wrappers.var_args_logger_3_objects.VAL3;
//import com.igor_shaula.payload_for_test.threads.SwitchingThreads;

@TypeDoc(createdBy = "shaula", createdOn = "05.06.2018", purpose = "abstract job - not in Service only")
public final class IterationsMeasurement {
    
    private static final String CN = "IterationsMeasurement";
    
    @MeDoc("should return default values if howManyIterations = 0 due to usage of single keys here")
    public static void measurePerformanceInLoop(@NonNull final DataTransport dataTransport,
                                                final int howManyIterations) {
        
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
            oneIterationResults.put(C.Key.READ_ARRAY_LIST_IN_THIS_THREAD,
                    i == 0 ? 0L : runReadArrayListInThisThread(dataTransport));
            
            oneIterationResults.put(C.Key.READ_ARRAY_LIST_IN_NEW_THREAD,
                    i == 0 ? 0L : runReadArrayListInNewThread(dataTransport));
            
            oneIterationResults.put(C.Key.GOOD_OLD_SYSTEM_OUT_PRINTLN,
                    i == 0 ? 0L : runSoutMethod(dataTransport));
            
            oneIterationResults.put(C.Key.STANDARD_ANDROID_LOG,
                    i == 0 ? 0L : runLogMethod(dataTransport));
            
            oneIterationResults.put(C.Key.MY_DOUBLE_ARGS_LOG_WRAPPER,
                    i == 0 ? 0L : runDalMethod(dataTransport));
            
            oneIterationResults.put(C.Key.MY_VARIABLE_ARGS_LOG_WRAPPER_1,
                    i == 0 ? 0L : runVal1Method(dataTransport));
            
            oneIterationResults.put(C.Key.MY_VARIABLE_ARGS_LOG_WRAPPER_2,
                    i == 0 ? 0L : runVal2Method(dataTransport));
            
            oneIterationResults.put(C.Key.MY_VARIABLE_ARGS_LOG_WRAPPER_3,
                    i == 0 ? 0L : runVal3Method(dataTransport));
            
            oneIterationResults.put(C.Key.MY_SUPERIOR_VOID_LOG_WRAPPER,
                    i == 0 ? 0L : runSLVoidMethod(dataTransport));
            
            oneIterationResults.put(C.Key.MY_SUPERIOR_INT_LOG_WRAPPER,
                    i == 0 ? 0L : runSLIntMethod(dataTransport));
/*
            // as this part of code is hot - no need of debug logging here during normal usage \\
            L.w("measurePerformanceInLoop", "i = " + i +
                    " oneIterationResults = " + oneIterationResults);
*/
            dataTransport.transportOneIterationsResult(oneIterationResults, i);
        }
    }
    
    @NonNull
    private static Long runReadArrayListInThisThread(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        SwitchingThreads.getInstance(dataTransport).iterateThroughArrayListInThisThread();
        return System.nanoTime() - initialNanoTime;
    }
    
    @NonNull
    private static Long runReadArrayListInNewThread(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        SwitchingThreads.getInstance(dataTransport).iterateThroughArrayListInNewThread();
        return System.nanoTime() - initialNanoTime;
    }
    
    private static long runSoutMethod(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        System.out.println(dataTransport.getLongStringForTest());
        return System.nanoTime() - initialNanoTime;
    }
    
    private static long runLogMethod(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        Log.v(CN, dataTransport.getLongStringForTest());
        return System.nanoTime() - initialNanoTime;
    }
    
    private static long runDalMethod(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        DAL.v(CN, dataTransport.getLongStringForTest());
        return System.nanoTime() - initialNanoTime;
    }
    
    private static long runVal1Method(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        VAL1.v(dataTransport.getLongStringForTest());
        return System.nanoTime() - initialNanoTime;
    }
    
    private static long runVal2Method(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        VAL2.v(dataTransport.getLongStringForTest());
        return System.nanoTime() - initialNanoTime;
    }
    
    private static long runVal3Method(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        VAL3.v(dataTransport.getLongStringForTest());
        return System.nanoTime() - initialNanoTime;
    }
    
    private static long runSLVoidMethod(@NonNull DataTransport dataTransport) {
        long salNanoTime = System.nanoTime();
        SLVoid.v(dataTransport.getLongStringForTest());
        return System.nanoTime() - salNanoTime;
    }
    
    private static long runSLIntMethod(@NonNull DataTransport dataTransport) {
        long initialNanoTime = System.nanoTime();
        SLInt.v(dataTransport.getLongStringForTest());
        return System.nanoTime() - initialNanoTime;
    }
}