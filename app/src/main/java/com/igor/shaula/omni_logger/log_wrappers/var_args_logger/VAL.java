package com.igor.shaula.omni_logger.log_wrappers.var_args_logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@TypeDoc(createdBy = "Igor Shaula", createdOn = "09-2017",
        modifiedBy = "Igor Shaula", modifiedOn = "21-10-2017", purpose = "" +
        "the most minimalistic & useful wrapper for local logging," +
        "helps to eliminate the 23-symbol in original TAG restriction", comment = "" +
        "every method here takes any number of arguments," +
        "the best name for this class consists of only one letter - L - for briefness in code")

public final class VAL {

    private static final boolean USE_LOGGING = true;

    private static final String CONTAINER_IS_NULL = "<NULL>";
    private static final String CONTAINER_IS_EMPTY = "<EMPTY>";
    private static final String L_NULL = "<null>";
    private static final String L_EMPTY = "<empty>";
    private static final String TAG_23 = "VariableArgsLogTag";
    private static final String DIVIDER = " ` ";

    private VAL() {
        // should not create any instances of this class \
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void v(@Nullable String... strings) {
        if (USE_LOGGING) {
            l(Log.VERBOSE, processAllStrings(strings));
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void d(@Nullable String... strings) {
        if (USE_LOGGING) {
            l(Log.DEBUG, processAllStrings(strings));
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void i(@Nullable String... strings) {
        if (USE_LOGGING) {
            l(Log.INFO, processAllStrings(strings));
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void w(@Nullable String... strings) {
        if (USE_LOGGING) {
            l(Log.WARN, processAllStrings(strings));
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void e(@Nullable String... strings) {
        if (USE_LOGGING) {
            l(Log.ERROR, processAllStrings(strings));
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static void a(@Nullable String... strings) {
        if (USE_LOGGING) {
            l(Log.ASSERT, processAllStrings(strings));
        }
    }

/*
    @MeDoc("simplest and fastest - even without TAG_23 - may be used to measure speed of doing job")
    public static void f(@NonNull String message) {
        System.out.println(message);
    }
*/

    @NonNull
    private static String processAllStrings(@Nullable String... strings) {
        String logResult; // logResult = VarArgsResult
        if (strings == null) {
            logResult = CONTAINER_IS_NULL;
        } else if (strings.length == 0) {
            logResult = CONTAINER_IS_EMPTY;
        } else if (strings.length == 1) {
            logResult = processOneString(strings[0]);
        } else {
            // saving here the first possible iteration with proper initialization from the start \
            logResult = processOneString(strings[0]) + DIVIDER;
            // this saves a bit of processor speed - anyway it's better than no optimization \
            final int length = strings.length;
/*
            starting from 1 (not from 0) - to properly initialize logResult during the first iteration \
            ending at the penultimate iteration - to exclude useless DIVIDER in the end of logResult \
*/
            final StringBuilder logResultBuilder = new StringBuilder(logResult);
            for (int i = 1; i < length - 1; i++) {
                logResultBuilder.append(processOneString(strings[i])).append(DIVIDER);
            }
            logResult = logResultBuilder.toString();
            logResult += processOneString(strings[length - 1]);
        }
        return logResult;
    }

    @NonNull
    private static String processOneString(@Nullable String theString) {
        String result;
        if (theString == null) {
            result = L_NULL;
        } else if (theString.isEmpty()) {
            result = L_EMPTY;
        } else {
            result = theString;
        }
        return result;
    }

    private static void l(int level, @NonNull String logResult) {
        if (level == Log.VERBOSE) {
            Log.v(TAG_23, logResult);
        } else if (level == Log.DEBUG) {
            Log.v(TAG_23, logResult);
        } else if (level == Log.INFO) {
            Log.v(TAG_23, logResult);
        } else if (level == Log.WARN) {
            Log.v(TAG_23, logResult);
        } else if (level == Log.ERROR) {
            Log.v(TAG_23, logResult);
        } else if (level == Log.ASSERT) {
            Log.wtf(TAG_23, logResult);
        } else {
            System.out.println(logResult);
        }
    }
}