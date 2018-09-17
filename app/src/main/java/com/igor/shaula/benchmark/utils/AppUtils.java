package com.igor.shaula.benchmark.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.igor_shaula.base_utils.annotations.TypeDoc;

@TypeDoc(createdBy = "shaula", createdOn = "17.09.2018", purpose = "")
public final class AppUtils {
    
    public static void showSnackbar(@NonNull View view, @NonNull String message, int duration) {
//        L.w(CN, "showSnackbar ` message = " + message);
        if (duration == 0) {
            duration = Snackbar.LENGTH_SHORT;
        } else if (duration > 0) {
            duration = Snackbar.LENGTH_LONG;
        } else {
            duration = Snackbar.LENGTH_INDEFINITE;
        }
        final Snackbar snackbar = Snackbar.make(view, message, duration);
//        TextView textView = (TextView) snackbar.getView()
//                .findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }
}