package com.example.sinhvienapplication.savedata;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sinhvienapplication.constant.Constant;

public class PrefManager {
    private static final String MY_PREFERENCES = "Pref";
    private static final String OPEN_FIRST_APP = "open_first_app";
    private static final String TYPE_USER = "type_user";


    public static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void clearData(Context context) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.clear();
        editor.commit();
    }

    public static void saveOpenFirstApp(Context context, boolean value) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putBoolean(OPEN_FIRST_APP, value);
        editor.apply();
    }

    public static boolean getOpenFirstApp(Context context){
        return getPreference(context).getBoolean(OPEN_FIRST_APP, true);
    }

    public static void saveTypeUser(Context context, String value) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(TYPE_USER, value);
        editor.apply();
    }

    public static String getTypeUser(Context context){
        return getPreference(context).getString(TYPE_USER, "");
    }
}
