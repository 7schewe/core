package io.schewe.core.theme;

import android.content.Context;

import io.schewe.core.preferences.PreferenceManager;


public class ThemeManager {

    private static ThemeManager INSTANCE;
    private PreferenceManager preferenceManager;
    private ThemeNode actualTheme = null;
    private ThemeNode firstTheme = null;
    private ThemeNode lastTheme = null;

    public static ThemeManager getThemeManager(Context context) {
        if (INSTANCE == null) INSTANCE = new ThemeManager(context);
        return INSTANCE;
    }

    private ThemeManager(Context context) {
        preferenceManager = PreferenceManager.getPreferenceManager(context);
        int themeID = preferenceManager.getThemeID();
        boolean isThemeDark = preferenceManager.isThemeDark();
        if(themeID>=0) this.addTheme(themeID, isThemeDark);
    }

    private boolean containsTheme(int themeResourceId){
        if(firstTheme==null) return false;
        if(firstTheme.getTheme()==themeResourceId) return true;
        ThemeNode node = firstTheme;
        do {
            if(node.getTheme()==themeResourceId) return true;
            node = node.getNextTheme();
        }while (node.getNextTheme().getTheme()!=firstTheme.getTheme());
        return false;
    }

    public void addTheme(int themeResourceId, boolean isDarkTheme) {
        if(this.containsTheme(themeResourceId)) return;
        ThemeNode newTheme = new ThemeNode(themeResourceId, isDarkTheme);

        if (this.actualTheme == null) this.actualTheme = newTheme;
        if (this.firstTheme == null) this.firstTheme = newTheme;
        if (this.lastTheme == null) this.lastTheme = newTheme;
        this.lastTheme.setNextTheme(newTheme);
        this.lastTheme = newTheme;
        this.lastTheme.setNextTheme(this.firstTheme);
    }

    public int getThemeID() { return actualTheme.getTheme();}

    public boolean isDarkTheme() { return actualTheme.isDarkTheme();}

    public void switchTheme() {
        actualTheme = actualTheme.getNextTheme();
        preferenceManager.setThemeID(actualTheme.getTheme(), actualTheme.isDarkTheme());
    }

    class ThemeNode {
        private int themeResourceId;
        private boolean isDarkTheme;
        private ThemeNode nextTheme = null;

        ThemeNode(int themeResourceId, boolean isDarkTheme) { this.themeResourceId = themeResourceId; this.isDarkTheme = isDarkTheme; }

        int getTheme() { return this.themeResourceId;}

        void setNextTheme(ThemeNode nextTheme) { this.nextTheme = nextTheme;}

        ThemeNode getNextTheme() { return this.nextTheme;}

        boolean isDarkTheme() { return this.isDarkTheme;}
    }
}
