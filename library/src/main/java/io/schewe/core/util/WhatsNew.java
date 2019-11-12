package io.schewe.core.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import androidx.appcompat.app.AlertDialog;

import io.schewe.core.BuildConfig;
import io.schewe.core.R;
import io.schewe.core.preferences.PreferenceManager;

public class WhatsNew {

    private static final SparseArray<String> changeLogs = new SparseArray<>();
    private final Activity activity;

    private WhatsNew(Activity activity){ this.activity = activity; }

    public static WhatsNew getInstance(Activity activity){ return new WhatsNew(activity); }

    public WhatsNew addChangeLog(String changeLog, int version){ WhatsNew.changeLogs.put(version, changeLog); return this;}

    public void show(){
        PreferenceManager preferenceManager = PreferenceManager.getPreferenceManager(activity);
        int currentVersion  = preferenceManager.getCurrentVersion();
        int newVersion      = currentVersion;
        try { newVersion = activity.getPackageManager().getPackageInfo(activity.getPackageName(),0).versionCode; }
        catch (PackageManager.NameNotFoundException ignored) { }
        if(newVersion>currentVersion || BuildConfig.DEBUG){
            StringBuilder changelog = new StringBuilder();
            for(int i=currentVersion; i<=newVersion; i++) if (WhatsNew.changeLogs.get(i) != null) changelog.append(System.lineSeparator()).append(WhatsNew.changeLogs.get(i));

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            AlertDialog dialog = builder
                    .setTitle(activity.getString(R.string.changelog))
                    .setMessage(changelog.toString())
                    .setNeutralButton(activity.getString(R.string.ok), (dialogInterface, i) ->{} )
                    .create();
            dialog.show();

            preferenceManager.setCurrentVersion(newVersion);
        }
    }
}
