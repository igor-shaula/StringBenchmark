package com.igor.shaula.omni_logger.log_wrappers.single_arg_logger;

import android.support.annotation.Nullable;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@SuppressWarnings({"WeakerAccess", "unused"})
@TypeDoc(createdBy = "Igor Shaula", createdOn = "21-10-2017", modifiedOn = "12-11-2017", purpose = "" +
        "the most minimalistic & useful wrapper for local logging," +
        "helps to eliminate the 23-symbol in original TAG restriction", comment = "" +
        "every method here takes only one argument," +
        "the best name for this class consists of only one letter - L - for briefness in code")

public final class SAL {

    private static final String TAG_23 = "SingleArgsLogTag";
    // global constant switcher to be touched from this class only \\
    private static final boolean USE_LOGGING = true;
    // dynamic local switcher - can be helpful to toggle logging from other classes \\
    private static boolean isLogAllowed = true;

    private SAL() {
        // should not create any instances of this class \\
    }

    public static void silence() {
        isLogAllowed = false;
    }

    public static void restore() {
        isLogAllowed = true;
    }

    // very nice & fast to write in case of using LL-like templates \\
    public static void l(@Nullable final String message) {
        v(message);
    }

    public static void v(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.v(TAG_23, message);
        }
    }

    public static void d(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.d(TAG_23, message);
        }
    }

    public static void i(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.i(TAG_23, message);
        }
    }

    public static void w(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.w(TAG_23, message);
        }
    }

    public static void e(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.e(TAG_23, message);
        }
    }

    public static void a(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.wtf(TAG_23, message);
        }
    }

    // simplest and fastest - even without TAG_23 - may be used to measure speed of doing job \
    public static void f(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            System.out.println(message);
        }
    }
}