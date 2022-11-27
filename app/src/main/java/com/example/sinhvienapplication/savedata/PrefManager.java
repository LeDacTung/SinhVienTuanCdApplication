package com.example.sinhvienapplication.savedata;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String MY_PREFERENCES = "Pref";

    public static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
    }
}
