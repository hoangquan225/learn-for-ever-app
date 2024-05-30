package com.example.mylap.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    public static void saveStringToSharedPreferences(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void removeStringToSharedPreferences(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getStringToSharedPreferences(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        String savedValue = preferences.getString(key, "");
        return savedValue;
    }
}
