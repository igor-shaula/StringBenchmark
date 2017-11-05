package com.igor.shaula.omni_logger;

import com.igor.shaula.omni_logger.annotations.TypeDoc;

@SuppressWarnings("unused")
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
        public static final int AUDIO = 0x0;
        public static final int VIDEO = 0x1;
    }

    public static final class Prefs {

        public static final String KEY_PREFERENCES = "preferences for com.autoxloo.simulcast";
        public static final String KEY_DOMAIN = "domain to work with";
        public static final String KEY_LOGIN = "login for webView's content";
        public static final String KEY_PASSWORD = "password for webView's content";
    }

    public static final class Intent {

        public static final String NAME_COUNT = "domain for passing through intent";

        static final String TYPE = "type";
        static final String NO_COMMAND = "no_command";
    }

    public static final class Delay {

        public static final int EXIT_WITH_BACK_MILLIS = 2_000;
        public static final int SPLASH_ACTIVITY_MILLIS = 2_000;
    }
}