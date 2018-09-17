package com.igor_shaula.payload_for_test.log_wrappers.superior_logger;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igor_shaula.base_utils.annotations.TypeDoc;
import com.igor_shaula.payload_for_test.BuildConfig;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})

@TypeDoc(createdBy = "Igor Shaula", createdOn = "30-05-2018", purpose = "" +
        "the most minimalistic & useful wrapper for local logging," +
        "helps to eliminate the 23-symbol in original TAG restriction", comment = "" +
        "every method here takes any number of arguments," +
        "the best name for this class consists of only one letter - L - for briefness in code")
public final class SLVoid {
    
    private static final String TAG_SHORTENED_TO_23_SYMBOLS =
            "given TAG was shortened to first 23 symbols to avoid IllegalArgumentException";
    private static final String DIVIDER_SHORTENED_TO_10_SYMBOLS =
            "given divider was shortened to first 10 symbols to keep separation reasonable";
    private static final String CONNECTOR_SHORTENED_TO_10_SYMBOLS =
            "given connector was shortened to first 10 symbols to keep separation reasonable";
    
    // global constant switcher to be touched from this class only \\
    private static final boolean USE_LOGGING = BuildConfig.DEBUG;
    
    // dynamic local switcher - can be helpful to toggle logging from other classes \\
    private static boolean isLogAllowed = true;
    
    @NonNull
    private static String tag23 = "SLVoid";
    @NonNull
    private static String divider = " ` "; // should be restricted in length somehow to keep it usable \\
    @NonNull
    private static String connector = " = "; // should be restricted in length somehow to keep it usable \\
    @NonNull
    private static String stubForNullContainer = "{LogVarArgs:NULL}";
    @NonNull
    private static String stubForEmptyContainer = "{LogVarArgs:EMPTY}";
    @NonNull
    private static String stubForNullElement = "{null}";
    @NonNull
    private static String stubForEmptyElement = "{empty}";
    
    private SLVoid() {
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
            SLVoid.tag23 = tag23.substring(0, 22);
            // i think in this case user should be notified about such a change \\
            Log.i(SLVoid.tag23, TAG_SHORTENED_TO_23_SYMBOLS);
        } else {
            SLVoid.tag23 = tag23;
        }
    }
    
    @NonNull
    public static String getDivider() {
        return divider;
    }
    
    public static void setDivider(@NonNull String divider) {
        // i decided to restrict divider's length to reasonable limit (to avoid too long inner separators) \\
        if (divider.length() > 10) {
            SLVoid.divider = divider.substring(0, 9);
            Log.i(tag23, DIVIDER_SHORTENED_TO_10_SYMBOLS);
        }
        SLVoid.divider = divider;
    }
    
    @NonNull
    public static String getConnector() {
        return connector;
    }
    
    public static void setConnector(@NonNull String connector) {
        // i decided to restrict connector's length to reasonable limit (to avoid too long inner connectors) \\
        if (connector.length() > 10) {
            SLVoid.connector = connector.substring(0, 9);
            Log.i(tag23, CONNECTOR_SHORTENED_TO_10_SYMBOLS);
        }
        SLVoid.connector = connector;
    }
    
    @NonNull
    public static String getStubForNullContainer() {
        return stubForNullContainer;
    }
    
    public static void setStubForNullContainer(@NonNull String stubForNullContainer) {
        SLVoid.stubForNullContainer = stubForNullContainer; // currently unprotected concerning length \\
    }
    
    @NonNull
    public static String getStubForEmptyContainer() {
        return stubForEmptyContainer;
    }
    
    public static void setStubForEmptyContainer(@NonNull String stubForEmptyContainer) {
        SLVoid.stubForEmptyContainer = stubForEmptyContainer; // currently unprotected concerning length \\
    }
    
    @NonNull
    public static String getStubForNullElement() {
        return stubForNullElement;
    }
    
    public static void setStubForNullElement(@NonNull String stubForNullElement) {
        SLVoid.stubForNullElement = stubForNullElement; // currently unprotected concerning length \\
    }
    
    @NonNull
    public static String getStubForEmptyElement() {
        return stubForEmptyElement;
    }
    
    public static void setStubForEmptyElement(@NonNull String stubForEmptyElement) {
        SLVoid.stubForEmptyElement = stubForEmptyElement; // currently unprotected concerning length \\
    }
    
    // FASTEST & SIMPLEST ONE_ARGUMENT API =========================================================
    
    public static void v(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.v(tag23, message);
        }
    }
    
    public static void d(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.d(tag23, message);
        }
    }
    
    public static void i(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.i(tag23, message);
        }
    }
    
    public static void w(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.w(tag23, message);
        }
    }
    
    public static void e(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.e(tag23, message);
        }
    }
    
    public static void a(@Nullable final String message) {
        if (USE_LOGGING && isLogAllowed) {
            Log.wtf(tag23, message);
        }
    }
    
    //    @MeDoc("this is suitable addition here - just wrapped log invocation to have control over it")
    public static void o(@Nullable final Object object) { // s - because it is the simplest here \\
        if (USE_LOGGING && isLogAllowed) {
            System.out.println(object);
        }
    }
    
    // INT_RESULT PRINTLN ADDITIONAL PART ==========================================================
    
    public static int pV(@NonNull final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            return Log.println(Log.VERBOSE, tag23, assembleResultString(objects));
        } else {
            return -1;
        }
    }
    
    public static int pD(@NonNull final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            return Log.println(Log.DEBUG, tag23, assembleResultString(objects));
        } else {
            return -1;
        }
    }
    
    public static int pI(@NonNull final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            return Log.println(Log.INFO, tag23, assembleResultString(objects));
        } else {
            return -1;
        }
    }
    
    public static int pW(@NonNull final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            return Log.println(Log.WARN, tag23, assembleResultString(objects));
        } else {
            return -1;
        }
    }
    
    public static int pE(@NonNull final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            return Log.println(Log.ERROR, tag23, assembleResultString(objects));
        } else {
            return -1;
        }
    }
    
    public static int pA(@NonNull final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            return Log.println(Log.ASSERT, tag23, assembleResultString(objects));
        } else {
            return -1;
        }
    }
    
    // HOT METHODS FOR CONSTRUCTING MESSAGES TO BE SHOWN ===========================================
    
    @NonNull
    private static String assembleResultString(@Nullable final Object... objects) {
        final String logResult; // logResult = VarArgsResult \\
        if (objects == null) {
            logResult = stubForNullContainer;
        } else if (objects.length == 0) {
            logResult = stubForEmptyContainer;
        } else if (objects.length == 1) { // saving time by avoiding StringBuilder creation \\
            logResult = processOneObject(objects[0]);
        } else { // as [objects.length cannot be < 0] -> in this case [objects.length >= 2] \\
            final int minimumCapacity = getStringLength(objects[0]) + divider.length() + getStringLength(objects[1]);
            // as minimum number of args here is 2 -> we're preparing StringBuilder just for it \\
            final StringBuilder logResultBuilder = new StringBuilder(minimumCapacity);
            // the starting string doesn't need the divider before it - so it's taken out of loop \\
            logResultBuilder.append(processOneObject(objects[0]));
            // this loop is expected to do at least one iteration & starts from the second string \\
            for (int i = 1, argsLength = objects.length; i < argsLength; i++) {
                // as we have already wrote initial string - here we should start with divider \\
                logResultBuilder.append(divider).append(processOneObject(objects[i]));
            }
            logResult = logResultBuilder.toString();
        }
        return logResult;
    }
    
    @NonNull
    private static String processOneObject(@Nullable final Object theObject) {
        final String result;
        if (theObject == null) {
            result = stubForNullElement;
        } else if (theObject.toString().isEmpty()) {
            result = stubForEmptyElement;
        } else {
            result = theObject.toString();
        }
        return result;
    }
    
    private static int getStringLength(@Nullable Object string) {
        return string != null ? string.toString().length() : 0;
    }
    
    // ADDITIONAL API FOR SHOWING CURRENT VALUES ===================================================
    
    public static void isV(@Nullable Object someInstance, @Nullable Object someValue) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.VERBOSE, createJointMessage(someInstance, someValue));
        }
    }
    
    public static void isD(@Nullable Object someInstance, @Nullable Object someValue) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.DEBUG, createJointMessage(someInstance, someValue));
        }
    }
    
    public static void isI(@Nullable Object someInstance, @Nullable Object someValue) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.INFO, createJointMessage(someInstance, someValue));
        }
    }
    
    public static void isW(@Nullable Object someInstance, @Nullable Object someValue) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.WARN, createJointMessage(someInstance, someValue));
        }
    }
    
    public static void isE(@Nullable Object someInstance, @Nullable Object someValue) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.ERROR, createJointMessage(someInstance, someValue));
        }
    }
    
    public static void isA(@Nullable Object someInstance, @Nullable Object someValue) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.ASSERT, createJointMessage(someInstance, someValue));
        }
    }
    
    public static void isO(@Nullable Object someInstance, @Nullable Object someValue) {
        if (USE_LOGGING && isLogAllowed) {
            System.out.println(createJointMessage(someInstance, someValue));
        }
    }
    
    //    @MeDoc("actually the main method - setting accordance between custom & standard logging levels")
    private static void passToStandardLogger(final int logLevel, @NonNull final String logResult) {
        switch (logLevel) {
            case Log.VERBOSE:  // 2 \\
                Log.v(tag23, logResult);
                break;
            case Log.DEBUG:  // 3 \\
                Log.d(tag23, logResult);
                break;
            case Log.INFO:  // 4 \\
                Log.i(tag23, logResult);
                break;
            case Log.WARN:  // 5 \\
                Log.w(tag23, logResult);
                break;
            case Log.ERROR:  // 6 \\
                Log.e(tag23, logResult);
                break;
            case Log.ASSERT:  // 7 \\
                Log.wtf(tag23, logResult);
                break;
            // default is not needed here - due to all invocations are defined & method is private \\
        }
    }
    
    @NonNull
    private static String createJointMessage(@Nullable Object instanceToLog, @Nullable Object value) {
        String result;
        if (instanceToLog == null) { // just protecting from NPE in that simple way \\
            result = stubForNullElement;
        } else {
            result = instanceToLog.toString();
        }
        return result + connector + value; // no need to use StringBuilder here for only 1 operation \\
    }
    
    // STANDARD API FOR SHOWING HAPPENED FACTS =====================================================
    
    public static void v(@Nullable final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.VERBOSE, assembleResultString(objects));
        }
    }
    
    public static void d(@Nullable final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.DEBUG, assembleResultString(objects));
        }
    }
    
    public static void i(@Nullable final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.INFO, assembleResultString(objects));
        }
    }
    
    public static void w(@Nullable final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.WARN, assembleResultString(objects));
        }
    }
    
    public static void e(@Nullable final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.ERROR, assembleResultString(objects));
        }
    }
    
    public static void a(@Nullable final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            passToStandardLogger(Log.ASSERT, assembleResultString(objects));
        }
    }
    
    // simplest and fastest - even without tag23 - may be used to measure speed of doing job \
    public static void o(@NonNull final Object... objects) {
        if (USE_LOGGING && isLogAllowed) {
            System.out.println(assembleResultString(objects));
        }
    }
    
    // REMAINING STAFF TO BE WRAPPED FOR HAVING COMPLETE FUNCTIONALITY =============================
    
    @NonNull
    public static String getStackTrace(@NonNull final Throwable throwable) {
        return Log.getStackTraceString(throwable);
    }
    
    public static boolean isLoggable(int priority) {
        return Log.isLoggable(tag23, priority);
    }
}