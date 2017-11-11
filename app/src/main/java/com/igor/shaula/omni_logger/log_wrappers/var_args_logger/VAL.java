package com.igor.shaula.omni_logger.log_wrappers.var_args_logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@SuppressWarnings({"WeakerAccess", "unused"})
@TypeDoc(createdBy = "Igor Shaula", createdOn = "09-2017", modifiedOn = "11-11-2017", purpose = "" +
        "the most minimalistic & useful wrapper for local logging," +
        "helps to eliminate the 23-symbol in original TAG restriction", comment = "" +
        "every method here takes any number of arguments," +
        "the best name for this class consists of only one letter - L - for briefness in code")
public final class VAL {

    // global constant switcher to be touched from this class only \\
    private static final boolean USE_LOGGING = true;

    private static final String CONTAINER_IS_NULL = "{LogVarArgs:NULL}";
    private static final String CONTAINER_IS_EMPTY = "{LogVarArgs:EMPTY}";
    private static final String L_NULL = "{null}";
    private static final String L_EMPTY = "{empty}";
    private static final String TAG_23 = "VariableArgsLogTag";
    private static final String DIVIDER = " ` ";

    // dynamic local switcher - can be helpful to toggle logging from other classes \\
    private static boolean isLogAllowed = true;

    private VAL() {
        // should not create any instances of this class \
    }

    public static void silence() {
        isLogAllowed = false;
    }

    public static void restore() {
        isLogAllowed = true;
    }

    public static void v(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            l(Log.VERBOSE, processAllStrings(strings));
        }
    }

    public static void d(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            l(Log.DEBUG, processAllStrings(strings));
        }
    }

    public static void i(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            l(Log.INFO, processAllStrings(strings));
        }
    }

    public static void w(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            l(Log.WARN, processAllStrings(strings));
        }
    }

    public static void e(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            l(Log.ERROR, processAllStrings(strings));
        }
    }

    public static void a(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            l(Log.ASSERT, processAllStrings(strings));
        }
    }

    private static void l(final int level, @NonNull final String logResult) {
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

/*
    @MeDoc("simplest and fastest - even without TAG_23 - may be used to measure speed of doing job")
    public static void f(@NonNull String message) {
        System.out.println(message);
    }
*/
    // TODO: 11.11.2017 try & measure StringBuilder's optimization with {new StringBuilder(2)} \\

    @NonNull
    private static String processAllStrings(@Nullable final String... strings) {
        String logResult; // logResult = VarArgsResult
        if (strings == null) {
            logResult = CONTAINER_IS_NULL;
        } else if (strings.length == 0) {
            logResult = CONTAINER_IS_EMPTY;
        } else if (strings.length == 1) {
            logResult = processOneString(strings[0]);
        } else {
            // preparing the obvious optimization for avoiding String concatenations \\
            final StringBuilder logResultBuilder = new StringBuilder(processOneString(strings[0]));
            // as in this block we assume tha there are more than one String - DIVIDER is needed now \\
            logResultBuilder.append(DIVIDER);
            // this saves a bit of processor speed - anyway it's better than no optimization \
            final int lengthReducedBy1 = strings.length - 1;
/*
            starting from 1 (not from 0) - to properly initialize logResult during the first iteration \
            ending at the penultimate iteration - to exclude useless DIVIDER in the end of logResult \
*/
            for (int i = 1; i < lengthReducedBy1; i++) {
                logResultBuilder.append(processOneString(strings[i])).append(DIVIDER);
            }
            // the last iteration in loop is avoided for proper closing without DIVIDER \\
            logResultBuilder.append(processOneString(strings[lengthReducedBy1]));
            logResult = logResultBuilder.toString();
        }
        return logResult;
    }

    @NonNull
    private static String processOneString(@Nullable final String theString) {
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
}