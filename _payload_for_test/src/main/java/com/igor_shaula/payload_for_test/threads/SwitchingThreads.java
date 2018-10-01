package com.igor_shaula.payload_for_test.threads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor_shaula.base_utils.annotations.TypeDoc;

import java.util.ArrayList;

@TypeDoc(createdBy = "shaula", createdOn = "18.07.2018", purpose = "" +
        "test switching between threads when working with collections")
public class SwitchingThreads {
    
    @Nullable
    private static SwitchingThreads instance;
    @NonNull
    private ArrayList<String> testedArrayList = new ArrayList<>(10);
    @NonNull
    private Callback callback;
    
    private SwitchingThreads(@NonNull Callback callback) {
        this.callback = callback;
        
        testedArrayList.add("0");
        testedArrayList.add("1");
        testedArrayList.add("2");
        testedArrayList.add("3");
        testedArrayList.add("4");
        testedArrayList.add("5");
        testedArrayList.add("6");
        testedArrayList.add("7");
        testedArrayList.add("8");
        testedArrayList.add("9");
    }
    
    @NonNull
    public static SwitchingThreads getInstance(@NonNull Callback callback) {
        if (instance == null) {
            instance = new SwitchingThreads(callback);
        }
        return instance;
    }
    
    public void iterateThroughArrayListInThisThread() {
        final StringBuilder resultBuilder = new StringBuilder();
        for (String s : testedArrayList) {
            resultBuilder.append(s);
        }
        callback.delegateResult(resultBuilder.toString());
    }
    
    public void iterateThroughArrayListInNewThread() {
        final Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                iterateThroughArrayListInThisThread();
            }
        });
        workerThread.start();
    }
    
    public interface Callback {
        
        void delegateResult(@Nullable String result);
    }
}