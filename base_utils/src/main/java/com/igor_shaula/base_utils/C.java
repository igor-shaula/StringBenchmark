package com.igor_shaula.base_utils;

import com.igor_shaula.base_utils.annotations.TypeDoc;

@SuppressWarnings({"unused", "WeakerAccess"})
@TypeDoc(createdBy = "Igor Shaula", createdOn = "31-08-2017",
        purpose = "unified all constants keeper")

public final class C {

    public static final char SPACE = ' ';
    public static final char SLASH = '/';
    public static final char DOT = '.';
    public static final char COMMA = ',';
    public static final char STAR = '*';
    public static final char DASH = '-';
    public static final char UNDERSCORE = '_';
    public static final char ZERO = '0';
    public static final char N = '\n';
    public static final String TWO_ZEROES = "00";
    public static final String THREE_ZEROES = "000";
    public static final String REGEX_NOT_DIGIT = "\\D";
    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String INITIAL_BASIC_STRING = "1234567890";
    public static final String INITIAL_STRING_REPETITIONS = "100";
    public static final String INITIAL_TEST_ITERATIONS = "1000";

    private C() {
        // should not create any instances of this class \
    }

    public static class Key {

        public static final String READ_ARRAY_LIST_IN_THIS_THREAD = "read 10 strings ArrayList in current thread";
        public static final String READ_ARRAY_LIST_IN_NEW_THREAD = "read 10 strings ArrayList - new thread each time";
        public static final String GOOD_OLD_SYSTEM_OUT_PRINTLN = "Good old System.out.println(…):";
        public static final String STANDARD_ANDROID_LOG = "Standard Android Log:";
        public static final String MY_DOUBLE_ARGS_LOG_WRAPPER = "My double-args Log wrapper:";
        public static final String MY_VARIABLE_ARGS_LOG_WRAPPER_1 = "My variable-args Log wrapper 1:";
        public static final String MY_VARIABLE_ARGS_LOG_WRAPPER_2 = "My variable-args Log wrapper 2:";
        public static final String MY_VARIABLE_ARGS_LOG_WRAPPER_3 = "My variable-args Log wrapper 3:";
        public static final String MY_SUPERIOR_VOID_LOG_WRAPPER = "My superior-void Log wrapper:";
        public static final String MY_SUPERIOR_INT_LOG_WRAPPER = "My superior-int Log wrapper:";
    }

    public static final class Choice {

        public static final int TOAST = 1000;
        public static final int SNACKBAR = 1001;
        public static final int PREPARATION = 1;
        public static final int STOPPED = 2;
    }

    public static final class Prefs {

        public static final String KEY_PREFERENCES = "preferences for com.autoxloo.simulcast";
        public static final String KEY_DOMAIN = "domain to work with";
        public static final String KEY_LOGIN = "login for webView's content";
        public static final String KEY_PASSWORD = "password for webView's content";
    }

    public static final class Intent {

        public static final String ACTION_START_LOAD_PREPARATION = "from activity to intent-service";
        public static final String ACTION_GET_PREPARATION_RESULT = "from intent-service to activity";
        public static final String ACTION_START_ALL_TESTS = "start all tests moving on one-by-one";
        public static final String ACTION_GET_SYSTEM_LOG_TEST_RESULT = "standard system log test";
        public static final String ACTION_GET_DAL_TEST_RESULT = "double argument log wrapper test";
        public static final String ACTION_GET_VAL_1_TEST_RESULT = "var-args-1 number log wrapper test";
        public static final String ACTION_GET_VAL_2_TEST_RESULT = "var-args-2 number log wrapper test";
        public static final String ACTION_GET_VAL_3_TEST_RESULT = "var-args-3 number log wrapper test";
        public static final String ACTION_GET_SL_VOID_TEST_RESULT = "superior-void log wrapper test";
        public static final String ACTION_GET_SL_INT_TEST_RESULT = "superior-int log wrapper test";
        public static final String ACTION_ON_SERVICE_STOPPED = "invoked after service's destruction";
        public static final String ACTION_GET_ONE_ITERATION_RESULTS = "results after one iteration";
        public static final String NAME_BASIC_STRING = "string used as a basis for creating load";
        public static final String NAME_COUNT = "domain for passing through intent";
        public static final String NAME_PREPARATION_TIME = "time of preparing the load";
        public static final String NAME_ITERATIONS = "number of iterations for testing every variant";
        public static final String NAME_ALL_TIME = "array with results for every variant per iteration";
        public static final String NAME_ITERATION_NUMBER = "iteration number";
        static final String TYPE = "type";
        static final String NO_COMMAND = "no_command";
        public static final String ACTION_JOB_STOPPED = "iterations job was interrupted";
    }

    public static final class Delay {

        public static final int EXIT_WITH_BACK_MILLIS = 2_000;
        public static final int SPLASH_ACTIVITY_MILLIS = 2_000;
    }
}