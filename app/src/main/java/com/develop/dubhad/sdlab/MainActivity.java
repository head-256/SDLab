package com.develop.dubhad.sdlab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AbstractPermissionActivity {

    private static final String[] PERMS = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected String[] getDesiredPermissions() {
        return PERMS;
    }

    @Override
    protected void onPermissionDenied() {
        Toast.makeText(this, "No permissions", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onReady(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        TextView appVersionView = findViewById(R.id.appVersionView);
        TextView imeiView = findViewById(R.id.imeiView);

        appVersionView.setText(getAppVersionName());
        imeiView.setText(getImei());
    }

    private String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getImei() {
        String imei = null;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }
        }
        return imei;
    }
}
