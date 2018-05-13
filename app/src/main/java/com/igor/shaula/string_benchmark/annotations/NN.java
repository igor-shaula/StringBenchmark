package com.igor.shaula.string_benchmark.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Shaula on 02.11.2017.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface NN {

    boolean isNull();
}

/*
...
@NN AnInstance anInstance = new AnInstance(); // cannot be null
@NN anInstance = instances.getByKey(key); // cannot be null
@NN anInstance = null; // should underline with red mistake
 */

class NNImpl implements NN {

    @Override
    public Class<? extends Annotation> annotationType() {
        return NN.class;
    }

    @Override
    public boolean isNull() {
//        Class<? extends NNImpl> aClass = getClass();
//        aClass.getAnnotation(NN.class);
        return false;
    }
}