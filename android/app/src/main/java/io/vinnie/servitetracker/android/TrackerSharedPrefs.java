package io.vinnie.servitetracker.android;

import android.content.Context;
import android.content.SharedPreferences;

public class TrackerSharedPrefs {

    private static Context context;

    private static SharedPreferences prefs;

    public static void init(Context context) {
        TrackerSharedPrefs.context = context;
        prefs = context.getSharedPreferences("tracker", Context.MODE_PRIVATE);
    }

    public static String getUserCard() {
        checkInited();
        return prefs.getString("userCard", null);
    }

    public static void setUserCard(String card) {
        checkInited();
        prefs.edit().putString("userCard", card).commit();
    }

    private static void checkInited() {
        if (context == null || prefs == null)
            throw new IllegalStateException("Must call init(Context) on TrackerSharedPrefs before using");
    }

}
