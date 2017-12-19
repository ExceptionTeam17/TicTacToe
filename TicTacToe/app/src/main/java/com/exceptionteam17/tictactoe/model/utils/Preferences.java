package com.exceptionteam17.tictactoe.model.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public abstract class Preferences {

    public static final String EMPTY = "errNO";

    public static String getStringFromPreferences(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String out = preferences.getString(key, EMPTY);
        if(out.isEmpty()){
            out = EMPTY;
        }
        return out;
    }

    public static void addStringToPreferences(Context context, String key, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
