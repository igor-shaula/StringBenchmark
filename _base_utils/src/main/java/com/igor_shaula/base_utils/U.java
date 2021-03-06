package com.igor_shaula.base_utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.igor_shaula.base_utils.annotations.MeDoc;
import com.igor_shaula.base_utils.annotations.TypeDoc;

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
    
    @NonNull
    public static long[] convertIntoArray(@NonNull List<Long> list) {
        final int size = list.size();
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        return array;
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
    
    public static void showToast(@NonNull Context context, @NonNull String string, int duration) {
//        L.w(CN, "showToast ` message = " + message);
        if (duration != 0) { // anything except Toast.LENGTH_SHORT
            duration = Toast.LENGTH_LONG;
        }
        immediatelyDisableToast();
        toast = Toast.makeText(context.getApplicationContext(), string, duration);
        toast.show();
    }
    
    public static void immediatelyDisableToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
    
    @NonNull
    public static String adaptForUser(@NonNull String[] unitsOfMeasurement, long nanoTimeValue) {
//    public static String adaptForUser(@NonNull Context context, long nanoTimeValue) {
        final StringBuilder stringBuilder = new StringBuilder();
        
        if (nanoTimeValue <= 0) {
            stringBuilder.append(C.STAR);
            
        } else if (nanoTimeValue < 1_000) {
            stringBuilder
                    .append(nanoTimeValue)
                    .append(C.SPACE)
                    .append(unitsOfMeasurement[0]);
            
        } else if (nanoTimeValue < 1_000_000) {
            stringBuilder
                    .append(createReadableStringForTime(nanoTimeValue))
                    .append(C.SPACE)
                    .append(unitsOfMeasurement[1]);
            
        } else if (nanoTimeValue < 1_000_000_000) {
            stringBuilder
                    .append(createReadableStringForTime(nanoTimeValue))
                    .append(C.SPACE)
                    .append(unitsOfMeasurement[2]);
            
        } else {
            stringBuilder
                    .append(createReadableStringForTime(nanoTimeValue))
                    .append(C.SPACE)
                    .append(unitsOfMeasurement[3]);
        }
        return stringBuilder.toString();
    }
    
    // TODO: 03.12.2017 check all numeric-related methods with using negative values \\
    
    @MeDoc("converts long number into the specially formatted string for showing test results")
    @NonNull
    private static String createReadableStringForTime(long l) {
        return replaceFirstDotWithComma(createReadableStringForLong(l));
    }
    
    @MeDoc("replaces only the first met dot with comma - of course if this dot exists in the string")
    @NonNull
    private static String replaceFirstDotWithComma(@NonNull String s) {
        return s.replaceFirst(C.REGEX_NOT_DIGIT, String.valueOf(C.COMMA));
    }
    
    @MeDoc("converts positive integer number into formatted string for showing quantities")
    public static String createReadableStringForLong(long l) {
        // as we need special format with dots & zeroes - standard Java formatters won't fit here \\
        final int howManySeparators = defineSeparatorsCount(l);
        final StringBuilder stringBuilder = new StringBuilder();
        // defining the changing-per-iteration working number \\
        long meltingByThousand = l;
        for (int i = 0; i <= howManySeparators; i++) {
            // creating the string from the number's tail \\
            final long divisionsModulo = meltingByThousand % 1000;
            // filling any possible gaps with zeroes \\
            if (divisionsModulo == 0) {
                stringBuilder.insert(0, C.THREE_ZEROES);
            } else if (0 < divisionsModulo && divisionsModulo < 10) {
                stringBuilder.insert(0, C.TWO_ZEROES + String.valueOf(divisionsModulo));
            } else if (10 <= divisionsModulo && divisionsModulo < 100) {
                stringBuilder.insert(0, C.ZERO + String.valueOf(divisionsModulo));
            } else if (100 <= divisionsModulo) { // cannot be > 1000 because of division by 1000 beforehand \\
                stringBuilder.insert(0, String.valueOf(divisionsModulo));
            }
            // updating the number's tail for the next possible iteration \\
            meltingByThousand = meltingByThousand / 1000;
            // now inserting the dot - only when current iteration is completed \\
            if (i < howManySeparators) {
                // dot is not needed when l < 1000 - nothing to separate here \\
                stringBuilder.insert(0, C.DOT);
            }
        }
        return reduceStartingZeroes(stringBuilder.toString());
    }
    
    @MeDoc("counts the number of needed separators between digits divided by three per group")
    private static int defineSeparatorsCount(long l) {
        int separatorsCount = 0;
        long howManyThousands = l; // we just need to start from something \\
        do {
            howManyThousands = howManyThousands / 1000;
            if (howManyThousands > 0) {
                separatorsCount++;
            }
        } while (howManyThousands > 999);
        return separatorsCount;
    }
    
    @MeDoc("eliminates all possible excess zeroes at the beginning of the string to a single zero")
    @NonNull
    private static String reduceStartingZeroes(@NonNull String s) {
        if (s.startsWith(C.TWO_ZEROES)) {
            s = s.replaceFirst(C.TWO_ZEROES, "");
        } else if (s.startsWith(String.valueOf(C.ZERO))) {
            s = s.replaceFirst(String.valueOf(C.ZERO), "");
        }
        return s;
    }
    
    public static boolean hideKeyboard(@NonNull View view) {
        final InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        } else {
            return false;
        }
    }
}