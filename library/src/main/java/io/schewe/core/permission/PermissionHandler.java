package io.schewe.core.permission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

import io.schewe.core.util.Callback;

public class PermissionHandler {

    private final Map<Integer, Callback.SimpleCallback> callbacks;
    private final Activity activity;

    @SuppressLint("UseSparseArrays")
    public PermissionHandler(Activity activity){
        this.activity = activity;
        this.callbacks = new HashMap<>();
    }

    private void addOnRequestCallback(int permission, Callback.SimpleCallback callback){ this.callbacks.put(permission, callback); }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for(Integer permissionCode : this.callbacks.keySet()) if (requestCode == permissionCode) if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            //noinspection ConstantConditions
            this.callbacks.get(permissionCode).execute();
    }

    public void requestPermission(int permissionCode, String permission, String[] dialogTexts, Callback.SimpleCallback callback) {
        this.addOnRequestCallback(permissionCode, callback);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, permission))
            new AlertDialog.Builder(this.activity)
                .setCancelable(true)
                .setTitle(dialogTexts[0])
                .setMessage(dialogTexts[1])
                .setPositiveButton(dialogTexts[2], (dialog, which) -> ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionCode))
                .create()
                .show();
        else ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionCode);
    }



}
