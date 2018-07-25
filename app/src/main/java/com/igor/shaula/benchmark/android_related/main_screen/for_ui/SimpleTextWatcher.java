package com.igor.shaula.benchmark.android_related.main_screen.for_ui;

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
        onTextChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // not needed \
    }
}