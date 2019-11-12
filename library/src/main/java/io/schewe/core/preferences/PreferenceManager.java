package io.schewe.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceManager {

    private static PreferenceManager INSTANCE;
    private static final String PREFS_NAME      = "SeelenfaengerPreferences";
    private static final String FIRST_RUN       = "firstRun";
    private static final String THEME_ID        = "themeId";
    private static final String THEME_IS_DARK   = "themeIsDark";
    private static final String USER_ID         = "userId";
    private static final String LATEST_NEWS     = "latestNews";
    private static final String PUBLISHER_MODE  = "publisherMode";
    private static final String SHOW_RATEDIALOG = "showRateDialog";
    private static final String LAUNCH_COUNT    = "launchCount";
    private static final String FIRST_LAUNCH    = "firstLaunch";
    private static final String CURRENT_VERSION = "currentVersion";
    private SharedPreferences preferences;

    private PreferenceManager(Context context){
        preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public static PreferenceManager getPreferenceManager(Context context){
        if(INSTANCE==null) INSTANCE = new PreferenceManager(context);
        return PreferenceManager.INSTANCE;
    }

    public boolean isFirstRun(){
        return preferences.getBoolean(FIRST_RUN,true);
    }

    @SuppressWarnings("EmptyMethod")
    public void startFirstRun(){}

    public void finishFirstRun(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FIRST_RUN, false);
        editor.apply();
    }

    public int getThemeID() { return preferences.getInt(THEME_ID, -1);}

    public boolean isThemeDark(){ return preferences.getBoolean(THEME_IS_DARK, false);}

    public void setThemeID(int themeId, boolean isDarkTheme){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(THEME_ID, themeId);
        editor.putBoolean(THEME_IS_DARK, isDarkTheme);
        editor.apply();
    }

    public boolean showRateDialogAgain(){ return preferences.getBoolean(SHOW_RATEDIALOG, true); }

    public void setShowRateDialogAgain(boolean showAgain){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SHOW_RATEDIALOG, showAgain);
        editor.apply();
    }

    public int getLaunchCount(){ return preferences.getInt(LAUNCH_COUNT, 0); }

    public void setLaunchCount(int count){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAUNCH_COUNT, count);
        editor.apply();
    }

    public long getFirstLaunch(){ return preferences.getLong(FIRST_LAUNCH, 0); }

    public void setFirstLaunch(long time){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(FIRST_LAUNCH, time);
        editor.apply();
    }

    public void setUserId(String id){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, id);
        editor.apply();
    }

    public long getLatestNews() {
        return preferences.getLong(LATEST_NEWS, 0);
    }

    public void setLatestNews(long latestNews){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LATEST_NEWS, latestNews);
        editor.apply();
    }

    public int getCurrentVersion() {
        return preferences.getInt(CURRENT_VERSION, 0);
    }

    public void setCurrentVersion(int currentVersion){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CURRENT_VERSION, currentVersion);
        editor.apply();
    }

//    public String getUserId(){ return preferences.getString(USER_ID, "");}

    public void setPublisherModeEnabled(boolean b) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PUBLISHER_MODE, b);
        editor.apply();
    }

    public boolean isPublisherModeEnabled(){ return preferences.getBoolean(PUBLISHER_MODE, false);}
}
