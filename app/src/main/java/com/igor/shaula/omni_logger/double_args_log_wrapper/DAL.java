package com.igor.shaula.omni_logger.double_args_log_wrapper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "08-2017",
        modifiedBy = "Igor Shaula", modifiedOn = "21-10-2017", purpose = "" +
        "the most minimalistic & useful wrapper for local logging," +
        "helps to eliminate the 23-symbol in original TAG restriction", comment = "" +
        "every method here takes only two arguments," +
        "the best name for this class consists of only one letter - L - for briefness in code")

public final class DAL {

    private static final boolean IS_LOG_SHOWN = true;

    private static final String TAG_23 = "DoubleArgsLogTag";
    private static final String DIVIDER = " ` ";

    private DAL() {
        // should not create any instances of this class \
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void l(@NonNull String className, @NonNull String message) {
        v(className, message);
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void v(@NonNull String className, @NonNull String message) {
        if (IS_LOG_SHOWN) {
            Log.v(TAG_23, className + DIVIDER + message);
        }
    }

    // usually used for tracing method chains - in the very beginning of a method \
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void d(@NonNull String className, @NonNull String message) {
        if (IS_LOG_SHOWN) {
            Log.d(TAG_23, className + DIVIDER + message);
        }
    }

    // SAL.i("message") - this is main & typical call for logger in my code - i just like it \
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void i(@NonNull String className, @NonNull String message) {
        if (IS_LOG_SHOWN) {
            Log.i(TAG_23, className + DIVIDER + message);
        }
    }

    // amount of these logs should be as minimal as possible - italic letter 'w' resembles 'v' \
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void w(@NonNull String className, @NonNull String message) {
        if (IS_LOG_SHOWN) {
            Log.w(TAG_23, className + DIVIDER + message);
        }
    }

    // SAL.e("exception") - for logging all exceptions and errors \
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void e(@NonNull String className, @NonNull String message) {
        if (IS_LOG_SHOWN) {
            Log.e(TAG_23, className + DIVIDER + message);
        }
    }

    // SAL.a("assert") - for things that should not ever happen \
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void a(@NonNull String className, @NonNull String message) {
        if (IS_LOG_SHOWN) {
            Log.wtf(TAG_23, className + DIVIDER + message);
        }
    }

    // simplest and fastest - even without TAG_23 - may be used to measure speed of doing job \
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void f(@NonNull String message) {
        System.out.println(message);
    }
}