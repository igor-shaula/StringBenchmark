package com.igor.shaula.string_benchmark.logic_engine;

import android.support.annotation.NonNull;

import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
public final class OneIterationResultModel {

    @NonNull
    private String testedMethodName;

    private long testedMethodTime;

    public OneIterationResultModel(@NonNull String testedMethodName, long testedMethodTime) {
        this.testedMethodName = testedMethodName;
        this.testedMethodTime = testedMethodTime;
    }

    @NonNull
    public String getTestedMethodName() {
        return testedMethodName;
    }

    public long getTestedMethodTime() {
        return testedMethodTime;
    }
}