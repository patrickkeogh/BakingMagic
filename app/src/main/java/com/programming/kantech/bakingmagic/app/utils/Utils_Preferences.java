package com.programming.kantech.bakingmagic.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.programming.kantech.bakingmagic.app.R;

/**
 * Created by patrick keogh on 2017-06-23.
 */

public class Utils_Preferences {

    public static void saveFavId(Context context, int recipe_id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(context.getString(R.string.pref_fav_recipe_id), recipe_id);
        spe.apply();
    }

    public static int getFavId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.pref_fav_recipe_id), 1);
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_Preferences() {
        throw new AssertionError();
    }
}
