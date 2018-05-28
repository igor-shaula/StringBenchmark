package com.igor.shaula.string_benchmark.log_wrappers.var_args_logger_2_configurable;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igor.shaula.string_benchmark.BuildConfig;
import com.igor.shaula.string_benchmark.annotations.MeDoc;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;

@SuppressWarnings({"WeakerAccess", "unused"})
@TypeDoc(createdBy = "Igor Shaula", createdOn = "28-05-2018", purpose = "" +
        "the most minimalistic & useful wrapper for local logging," +
        "helps to eliminate the 23-symbol in original TAG restriction", comment = "" +
        "every method here takes any number of arguments," +
        "the best name for this class consists of only one letter - L - for briefness in code")

public final class VAL2 {

    private static final String TAG_SHORTENED_TO_23_SYMBOLS =
            "given TAG was shortened to first 23 symbols to avoid IllegalArgumentException";
    private static final String DIVIDER_SHORTENED_TO_10_SYMBOLS =
            "given divider was shortened to first 10 symbols to keep separation reasonable";
    private static final String CONTAINER_IS_NULL = "{LogVarArgs:NULL}";
    private static final String CONTAINER_IS_EMPTY = "{LogVarArgs:EMPTY}";
    private static final String L_NULL = "{null}";
    private static final String L_EMPTY = "{empty}";

    // global constant switcher to be touched from this class only \\
    private static final boolean USE_LOGGING = BuildConfig.DEBUG;

    // dynamic local switcher - can be helpful to toggle logging from other classes \\
    private static boolean isLogAllowed = true;
    @NonNull
    private static String tag23 = "VariableArgsLogTag2";
    @NonNull
    private static String divider = " ` "; // should be restricted in length somehow to keep it usable \\

    private VAL2() {
        // should not create any instances of this class \\
    }

    // CONFIGURATION ===============================================================================

    public static void silence() {
        isLogAllowed = false;
    }

    public static void restore() {
        isLogAllowed = true;
    }

    @NonNull
    public static String getTag23() {
        return tag23;
    }

    public static void setTag23(@NonNull String tag23) {
        /*
        the following is taken from developers.android.com -> Log-related documentation \\
        IllegalArgumentException is thrown if the tag.length() > 23 for Nougat (7.0) releases (API <= 23) and prior,
        there is no tag limit of concern after this API level.
        */
        if (Build.VERSION.SDK_INT <= 23) {
            // to avoid IllegalArgumentException i decided to take only first 23 of given characters \\
            VAL2.tag23 = tag23.substring(0, 22);
            // i think in this case user should be notified about such a change \\
            Log.i(VAL2.tag23, TAG_SHORTENED_TO_23_SYMBOLS);
        } else {
            VAL2.tag23 = tag23;
        }
    }

    @NonNull
    public static String getDivider() {
        return divider;
    }

    public static void setDivider(@NonNull String divider) {
        // i decided to restrict divider's length to reasonable limit (to avoid too long inner separators) \\
        if (divider.length() > 10) {
            VAL2.divider = divider.substring(0, 9);
            Log.i(tag23, DIVIDER_SHORTENED_TO_10_SYMBOLS);
        }
        VAL2.divider = divider;
    }

    // =============================================================================================

    public static void v(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.VERBOSE, processAllStrings(strings));
        }
    }

    public static void d(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.DEBUG, processAllStrings(strings));
        }
    }

    public static void i(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.INFO, processAllStrings(strings));
        }
    }

    public static void w(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.WARN, processAllStrings(strings));
        }
    }

    public static void e(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.ERROR, processAllStrings(strings));
        }
    }

    public static void a(@Nullable final String... strings) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.ASSERT, processAllStrings(strings));
        }
    }

    // simplest and fastest - even without tag23 - may be used to measure speed of doing job \
    public static void p(@NonNull final String message) {
        if (USE_LOGGING && isLogAllowed) {
            System.out.println(message);
        }
    }

    @MeDoc("only setting accordance between custom & standard logging levels")
    private static void passToStandardLogger(final int logLevel, @NonNull final String logResult) {
        if (logLevel == Log.VERBOSE) { // 2 \\
            Log.v(tag23, logResult);
        } else if (logLevel == Log.DEBUG) { // 3 \\
            Log.d(tag23, logResult);
        } else if (logLevel == Log.INFO) { // 4 \\
            Log.i(tag23, logResult);
        } else if (logLevel == Log.WARN) { // 5 \\
            Log.w(tag23, logResult);
        } else if (logLevel == Log.ERROR) { // 6 \\
            Log.e(tag23, logResult);
        } else if (logLevel == Log.ASSERT) { // 7 \\
            Log.wtf(tag23, logResult);
        } else { // in fact this else will never be invoked \\
            System.out.println(logResult);
        }
    }

    @NonNull
    private static String processAllStrings(@Nullable final String... strings) {
        final String logResult; // logResult = VarArgsResult \\
        if (strings == null) {
            logResult = CONTAINER_IS_NULL;
        } else if (strings.length == 0) {
            logResult = CONTAINER_IS_EMPTY;
        } else if (strings.length == 1) { // saving time by avoiding StringBuilder creation \\
            logResult = processOneString(strings[0]);
        } else { // as [strings.length cannot be < 0] -> in this case [strings.length >= 2] \\
            final int minimumCapacity = getStringLength(strings[0]) + divider.length() + getStringLength(strings[1]);
            // as minimum number of args here is 2 -> we're preparing StringBuilder just for it \\
            final StringBuilder logResultBuilder = new StringBuilder(minimumCapacity);
            // the starting string doesn't need the divider before it - so it's taken out of loop \\
            logResultBuilder.append(processOneString(strings[0]));
            // this loop is expected to do at least one iteration & starts from the second string \\
            for (int i = 1, argsLength = strings.length; i < argsLength; i++) {
                // as we have already wrote initial string - here we should start with divider \\
                logResultBuilder.append(divider).append(processOneString(strings[i]));
            }
            logResult = logResultBuilder.toString();
        }
        return logResult;
    }

    @NonNull
    private static String processOneString(@Nullable final String theString) {
        final String result;
        if (theString == null) {
            result = L_NULL;
        } else if (theString.isEmpty()) {
            result = L_EMPTY;
        } else {
            result = theString;
        }
        return result;
    }

    private static int getStringLength(@Nullable String string) {
        return string != null ? string.length() : 0;
    }
}