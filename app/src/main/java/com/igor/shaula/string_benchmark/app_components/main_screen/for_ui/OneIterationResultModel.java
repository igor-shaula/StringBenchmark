package com.igor.shaula.string_benchmark.app_components.main_screen.for_ui;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
public class OneIterationResultModel {

    private String testedMethodName;

    private long testedMethodTime;

    public OneIterationResultModel(String testedMethodName, long testedMethodTime) {
        this.testedMethodName = testedMethodName;
        this.testedMethodTime = testedMethodTime;
    }

    public String getTestedMethodName() {
        return testedMethodName;
    }

    public void setTestedMethodName(String testedMethodName) {
        this.testedMethodName = testedMethodName;
    }

    public long getTestedMethodTime() {
        return testedMethodTime;
    }

    public void setTestedMethodTime(long testedMethodTime) {
        this.testedMethodTime = testedMethodTime;
    }
}