package com.tuliodev.cameratest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by eduar on 17/11/2016.
 */

class SaveSharePreference {
    private static final String CAMERAID = "cameraID";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    static void setCameraID(Context ctx, int cameraID){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(CAMERAID, cameraID);
        editor.apply();
    }

    static int getCameraID(Context ctx){
        return getSharedPreferences(ctx).getInt(CAMERAID,-1);
    }
}
