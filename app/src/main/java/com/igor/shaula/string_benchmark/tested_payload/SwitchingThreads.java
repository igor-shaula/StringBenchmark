package com.igor.shaula.string_benchmark.tested_payload;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;

import java.util.ArrayList;

@TypeDoc(createdBy = "shaula", createdOn = "18.07.2018", purpose = "" +
        "test switching between threads when working with collections")
public class SwitchingThreads {

    @NonNull
    private ArrayList<String> testedArrayList = new ArrayList<>(10);
    @NonNull
    private Callback callback;

    public SwitchingThreads(@NonNull Callback callback) {
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
    public String iterateThroughArrayListInTheSameThread() {
        final StringBuilder resultBuilder = new StringBuilder();
        for (String s : testedArrayList) {
            resultBuilder.append(s);
        }
        return resultBuilder.toString();
    }

    public void iterateThroughArrayListInWorkerThread() {
        final Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                callback.delegateResult(iterateThroughArrayListInTheSameThread());
            }
        });
        workerThread.start();
    }

    public interface Callback {

        void delegateResult(@Nullable String result);
    }
}