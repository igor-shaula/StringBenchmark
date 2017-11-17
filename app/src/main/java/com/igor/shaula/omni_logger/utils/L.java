package com.igor.shaula.omni_logger.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@SuppressWarnings({"WeakerAccess", "unused"})
@TypeDoc(createdBy = "Igor Shaula", createdOn = "08-2017", modifiedOn = "12-11-2017", purpose = "" +
        "the most minimalistic & useful wrapper for local logging," +
        "helps to eliminate the 23-symbol in original TAG restriction", comment = "" +
        "every method here takes only two arguments," +
        "the best name for this class consists of only one letter - L - for briefness in code")

public final class L {

    private static final String TAG_23 = "DoubleArgsLogTag";
    private static final String DIVIDER = " ` ";
    // global constant switcher to be touched from this class only \\
    private static final boolean USE_LOGGING = true;
    // dynamic local switcher - can be helpful to toggle logging from other classes \\
    private static boolean isLogAllowed = true;

    private L() {
        // should not create any instances of this class \\
    }

    public static void silence() {
        isLogAllowed = false;
    }

    public static void restore() {
        isLogAllowed = true;
    }

    // very nice & fast to write in case of using LL-like templates \\
    public static void l(@NonNull String className, @Nullable String message) {
        v(className, message);
    }

    public static void v(@NonNull String className, @Nullable String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.v(TAG_23, className + DIVIDER + message);
        }
    }

    public static void d(@NonNull String className, @Nullable String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.d(TAG_23, className + DIVIDER + message);
        }
    }

    public static void i(@NonNull String className, @Nullable String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.i(TAG_23, className + DIVIDER + message);
        }
    }

    public static void w(@NonNull String className, @Nullable String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.w(TAG_23, className + DIVIDER + message);
        }
    }

    public static void e(@NonNull String className, @Nullable String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.e(TAG_23, className + DIVIDER + message);
        }
    }

    public static void a(@NonNull String className, @Nullable String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.wtf(TAG_23, className + DIVIDER + message);
        }
    }

    // simplest and fastest - even without TAG_23 - may be used to measure speed of doing job \
    public static void f(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            System.out.println(message);
        }
    }
}