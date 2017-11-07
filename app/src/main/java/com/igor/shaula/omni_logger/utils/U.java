package com.igor.shaula.omni_logger.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.igor.shaula.omni_logger.R;
import com.igor.shaula.omni_logger.annotations.MeDoc;
import com.igor.shaula.omni_logger.annotations.TypeDoc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
@TypeDoc(createdBy = "Igor Shaula", createdOn = "31-08-2017", modifiedOn = "06.11.2017",
        purpose = "unified utility shared methods container",
        comment = "former PSUtils = Public Static Utility methods")

public final class U {

    private static final String CN = "U";

    @Nullable
    private static Toast toast; // needed to enable cancellation for the previous toast \

    private U() {
        // should not create any instances of this class \
    }

    public static boolean isTablet(@NonNull Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @MeDoc("crazy simple magic method - it finds my already launched service among others")
    public static boolean isMyServiceRunning(@NonNull Context context, @NonNull Class<?> serviceClass) {
        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        final List<ActivityManager.RunningServiceInfo> runningServices =
                activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermissions(@Nullable Context context, @Nullable String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // may be <= !!!
            return true;
        }
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
//                    return false;
                }
            }
        }
        return false;
//        return true;
    }

    public static boolean isAnyOneNullOrEmpty(@Nullable String... strings) {
        if (strings == null) {
            return true;
        }
        boolean isNullOrEmpty = false;
        for (String s : strings) {
            if (s == null || s.isEmpty()) {
                isNullOrEmpty = true;
            }
        }
        return isNullOrEmpty;
    }

    @MeDoc("safe conversion - without crashes")
    public static int convertIntoInt(@Nullable String string) {
        if (string == null || string.isEmpty()) {
            return 0;
        } else {
            int result = 0;
            try {
                result = Integer.parseInt(string);
            } catch (NumberFormatException nfe) {
                Log.e(CN, nfe.getLocalizedMessage());
            }
            return result;
        }
    }

    @MeDoc("provides with device info")
    @NonNull
    public static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + C.SPACE + model;
        }
    }

    @NonNull
    private static String capitalize(@Nullable String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        final char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @NonNull
    public static String getCommandFromIntent(@Nullable Intent intent) {
        String command = null;
        try {
            if (intent != null && intent.getExtras() != null) {
                command = intent.getExtras().getString(C.Intent.TYPE);
//                command = intent.getStringExtra(C.TYPE);
            }
        } catch (Exception e) {
            Log.e(CN, "getCommandFromIntent ` e = " + e.getMessage());
        }
        if (command != null) {
            return command;
        } else {
            return C.Intent.NO_COMMAND;
        }
    }

    @NonNull
    public static String makeNonNullStringFrom(@Nullable String input) {
        return input == null ? "" : input;
    }

    @NonNull
    public static Collection<String> parseForSeparator(@NonNull String rawString, char separator) {

        final List<String> resultList = new LinkedList<>();
        if (rawString.isEmpty()) {
            return resultList;
        }
        String fragment = rawString;
        String added;
        int separatorIndex = 0;
        for (int i = 0, l = rawString.length(); i < l; i++) {
            if (rawString.charAt(i) == separator) {
                added = rawString.substring(separatorIndex, i); // excluding separator length
//                L.d(CN, "parseForSeparator ` added in cycle = " + added);
                resultList.add(added);
                separatorIndex = i + 1; // excluding separator length for next iteration
                fragment = rawString.substring(separatorIndex);
            }
        }
        if (!fragment.isEmpty()) {
//            L.d(CN, "parseForSeparator ` added rest = " + fragment);
            resultList.add(fragment);
        }
        return resultList;
    }

    public static void printLogFor(@Nullable Intent intent) {
        if (intent == null) {
            Log.i(CN, "printLogFor ` intent == null");
            return;
        }
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.i(CN, "printLogFor ` bundle is not null");
            for (String key : bundle.keySet()) {
//                String value = bundle.getString(key);
                Log.i(CN, "printLogFor ` Key: " + key + " Value: " + bundle.get(key));
            }
        } else {
            Log.i(CN, "printLogFor ` bundle == null");
        }
    }

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

    public static void showToast(@NonNull Context context, @NonNull String string, int duration) {
//        L.w(CN, "showToast ` message = " + message);
        if (duration == 0) {
            duration = Toast.LENGTH_SHORT;
        } else {
            duration = Toast.LENGTH_LONG;
        }
        if (toast != null) {
            toast.cancel();
            toast = null;
//            L.l(CN, "showToast ` toast cancelled");
//        } else {
//            L.l(CN, "showToast ` toast was null");
        }
        toast = Toast.makeText(context.getApplicationContext(), string, duration);
        toast.show();
    }

    @NonNull
    public static String adaptForUser(@NonNull Context context, long nanoTimeValue) {
        String result = "";

        if (nanoTimeValue < 1000) {
            result += nanoTimeValue + C.SPACE + context.getString(R.string.nanos);

        } else if (nanoTimeValue >= 1000 && nanoTimeValue < 1000_000) {
            result += nanoTimeValue / 1000 + C.DOT + nanoTimeValue % 1000 +
                    C.SPACE + context.getString(R.string.micros);

        } else if (nanoTimeValue >= 1000_000 && nanoTimeValue < 1000_000_000) {
            result += nanoTimeValue / 1000_000 + C.DOT + nanoTimeValue % 1000 +
                    C.SPACE + context.getString(R.string.millis);

        } else {
            result += nanoTimeValue / 1000_000_000 + C.DOT + nanoTimeValue % 1000 +
                    C.SPACE + context.getString(R.string.seconds);
        }
        return result;
    }
}