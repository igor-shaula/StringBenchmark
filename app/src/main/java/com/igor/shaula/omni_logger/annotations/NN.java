package com.igor.shaula.omni_logger.annotations;

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
}

/*
...
@NN int anInstance = new AnInstance(); // cannot be null
@NN anInstance = instances.getByKey(key); // cannot be null
@NN anInstance = null; // should underline with red mistake
 */