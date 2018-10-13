package com.develop.dubhad.sdlab.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.develop.dubhad.sdlab.R;

public class PermissionUtil {

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity context, String permission, int requestCode, String rationale) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            showPermissionRationale(context, new String[]{permission},
                    requestCode, rationale);
        }
        else {
            ActivityCompat.requestPermissions(context, new String[]{permission},
                    requestCode);
        }
    }

    private static void showPermissionRationale(final Activity context, final String[] permissions, final int requestCode,
                                         String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(context.getString(R.string.permission_necessary));
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(context, permissions, requestCode);}});
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
