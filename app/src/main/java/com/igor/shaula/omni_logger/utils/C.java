package com.igor.shaula.omni_logger.utils;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@SuppressWarnings({"unused", "WeakerAccess"})
@TypeDoc(createdBy = "Igor Shaula", createdOn = "31-08-2017",
        purpose = "unified all constants keeper")

public final class C {

    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String SPACE = " ";
    public static final String SLASH = "/";
    public static final String DOT = ".";
    public static final String UNDERSCORE = "_";
    public static final String DASH = "-";

    public static final int ALL_PERMISSIONS_REQUEST = 3000;

    private C() {
        // should not create any instances of this class \
    }

    public static final class Choice {

        public static final int TOAST = 1000;
        public static final int SNACKBAR = 1001;
        public static final int PREPARATION = 0x1;
        public static final int TEST_SYSTEM_LOG = 0x10;
        public static final int TEST_SAL = 0x11;
        public static final int TEST_DAL = 0x12;
        public static final int TEST_VAL = 0x13;
        public static final int DESTROYED = 0x100;
    }

    public static final class Prefs {

        public static final String KEY_PREFERENCES = "preferences for com.autoxloo.simulcast";
        public static final String KEY_DOMAIN = "domain to work with";
        public static final String KEY_LOGIN = "login for webView's content";
        public static final String KEY_PASSWORD = "password for webView's content";
    }

    public static final class Intent {

        public static final String ACTION_START_BURDEN_PREPARATION = "from activity to intent-service";
        public static final String ACTION_GET_PREPARATION_RESULT = "from intent-service to activity";
        public static final String ACTION_START_ALL_TESTS = "start all tests moving on one-by-one";
        public static final String ACTION_GET_SYSTEM_LOG_TEST_RESULT = "standard system log test";
        public static final String ACTION_GET_SAL_TEST_RESULT = "single argument log wrapper test";
        public static final String ACTION_GET_DAL_TEST_RESULT = "double argument log wrapper test";
        public static final String ACTION_GET_VAL_TEST_RESULT = "var-args number log wrapper test";
        public static final String ACTION_ON_SERVICE_STOPPED = "invoked after service's destruction";
        public static final String ACTION_GET_ONE_ITERATION_RESULTS = "results after one iteration";
        public static final String NAME_COUNT = "domain for passing through intent";
        public static final String NAME_PREPARATION_TIME = "time of preparing the burden";
        public static final String NAME_SYSTEM_LOG_TIME = "time of system log work";
        public static final String NAME_SAL_TIME = "time of single-arg log wrapper work";
        public static final String NAME_DAL_TIME = "time of double-arg log wrapper work";
        public static final String NAME_VAL_TIME = "time of var-args log wrapper work";
        public static final String NAME_ITERATIONS = "number of iterations for testing every variant";
        public static final String NAME_ALL_TIME = "array with results for every variant per iteration";
        public static final String NAME_ITERATION_NUMBER = "iteration number";
        static final String TYPE = "type";
        static final String NO_COMMAND = "no_command";
    }

    public static final class Delay {

        public static final int EXIT_WITH_BACK_MILLIS = 2_000;
        public static final int SPLASH_ACTIVITY_MILLIS = 2_000;
    }
}