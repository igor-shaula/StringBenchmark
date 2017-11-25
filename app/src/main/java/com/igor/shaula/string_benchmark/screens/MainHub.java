package com.igor.shaula.string_benchmark.screens;

import android.support.annotation.NonNull;

public interface MainHub {

    interface SystemLink {

    }

    interface UiLink {

        void setLogicLink(@NonNull LogicLink logicLink);
    }

    interface LogicLink {

        void unLinkDataTransport();
    }
}