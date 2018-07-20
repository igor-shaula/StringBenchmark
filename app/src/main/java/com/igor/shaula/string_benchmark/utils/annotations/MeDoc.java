package com.igor.shaula.string_benchmark.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@TypeDoc(createdBy = "igor", createdOn = "22.08.2017",
        purpose = "minimalistic way to put in a single line documenting comment for a method")
@Documented
//@Inherited - commented to force separate documenting for every possible overridden method \
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})

public @interface MeDoc {

    String value();
}