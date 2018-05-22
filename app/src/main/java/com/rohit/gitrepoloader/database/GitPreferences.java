package com.rohit.gitrepoloader.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.rohit.gitrepoloader.utils.AppController;

/**
 * Created by oust on 5/21/18.
 */

public class GitPreferences {


    public static void save(String Key, String Value) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = AppController.getmContext().getSharedPreferences("GitLoader",
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putString(Key, Value);
        editor.commit();
    }


    public static String get(String Key) {
        String text = "";
        try {
            SharedPreferences preference;
            preference = AppController.getmContext().getSharedPreferences("GitLoader", Context.MODE_PRIVATE);
            text = preference.getString(Key, null);
        } catch (Exception e) {
        }
        return text;
    }

    public static void clear(String Key) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = AppController.getmContext().getSharedPreferences("GitLoader",
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.remove(Key);
        editor.commit();
    }

    public static void saveintVar(String key, int value) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = AppController.getmContext().getSharedPreferences("GitLoader",
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getSavedInt(String key) {
        SharedPreferences preference;
        int val;
        preference = AppController.getmContext().getSharedPreferences("GitLoader",
                Context.MODE_PRIVATE);
        val = preference.getInt(key, 0);
        return val;
    }

    public static void saveBooleanVar(String key, boolean value) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = AppController.getmContext().getSharedPreferences("GitLoader",
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSavedBoolean(String key) {
        SharedPreferences preference;
        boolean val;
        preference = AppController.getmContext().getSharedPreferences("GitLoader",
                Context.MODE_PRIVATE);
        val = preference.getBoolean(key, false);
        return val;
    }
}
