package io.schewe.core.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import io.schewe.core.preferences.PreferenceManager;

public class AppRater {

    private final static String MARKETPLACE_URL = "market://details?id=io.schewe.seelenfaenger";
    private final static int DAYS_TO_MILLIS = 24 * 60 * 60 * 1000;
    private final static int DAYS_UNTIL_PROMPT      = 7 * DAYS_TO_MILLIS;
    private final static int LAUNCHES_UNTIL_PROMPT  = 7;
    private String title;
    private String message;
    private String rateButtonText;
    private String laterButtonText;
    private String declineButtonText;
    private final Activity activity;
    private int days;
    private int launches;

    private AppRater(Activity activity) {
        this.activity = activity;
        this.title = "";
        this.message = "";
        this.rateButtonText = "";
        this.laterButtonText = "";
        this.declineButtonText = "";
        this.days = AppRater.DAYS_UNTIL_PROMPT;
        this.launches = AppRater.LAUNCHES_UNTIL_PROMPT;
    }

    public static AppRater getInstance(Activity activity) { return new AppRater(activity); }

    public AppRater setTitle(String title) { this.title = title; return this; }

    public AppRater setMessage(String message) { this.message = message; return this; }

    public AppRater setRateButtonText(String rateButtonText) { this.rateButtonText = rateButtonText; return this; }

    public AppRater setLaterButtonText(String laterButtonText) { this.laterButtonText = laterButtonText; return this; }

    public AppRater setDeclineButtonText(String declineButtonText) { this.declineButtonText = declineButtonText; return this; }

    public AppRater setDaysUntilPrompt(int days){ this.days = days * AppRater.DAYS_TO_MILLIS; return this; }

    public AppRater setLaunchesUntilPrompt(int launches){ this.launches = launches; return this;}

    public void show() {
        PreferenceManager preferenceManager = PreferenceManager.getPreferenceManager(this.activity);
        if (preferenceManager.showRateDialogAgain()) {
            int launchCount = preferenceManager.getLaunchCount();
            long dateOfFirstLaunch = preferenceManager.getFirstLaunch();
            if (dateOfFirstLaunch == 0) preferenceManager.setFirstLaunch(System.currentTimeMillis());
            if (launchCount >= this.launches && System.currentTimeMillis() >= dateOfFirstLaunch + (this.days)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                AlertDialog dialog = builder.setMessage(this.message)
                        .setTitle(this.title)
                        .setPositiveButton(this.rateButtonText, (d, id) -> {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKETPLACE_URL)));
                            preferenceManager.setShowRateDialogAgain(false);
                        })
                        .setNegativeButton(this.declineButtonText, (d, id) -> preferenceManager.setShowRateDialogAgain(false))
                        .setNeutralButton(this.laterButtonText, (d, id) -> {
                            preferenceManager.setFirstLaunch(System.currentTimeMillis());
                            preferenceManager.setLaunchCount(0);
                        })
                        .create();
                dialog.show();
            }
        }
    }
}