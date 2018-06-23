package com.igor.shaula.string_benchmark.app_components.main_screen;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
class OneIterationResultModel {

    private String testedMethodName;

    private long testedMethodTime;

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