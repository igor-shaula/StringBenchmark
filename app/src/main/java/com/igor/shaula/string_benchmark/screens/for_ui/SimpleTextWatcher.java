package com.igor.shaula.string_benchmark.screens.for_ui;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author igor shaula \
 */
public abstract class SimpleTextWatcher implements TextWatcher {

    public abstract void onTextChanged();

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // not needed \
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        L.restore();
//        L.l("onTextChanged", "s = " + s);
//        L.silence();
        onTextChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // not needed \
    }
}