package com.develop.dubhad.sdlab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AbstractPermissionActivity {

    private static final String[] PERMS = {Manifest.permission.READ_PHONE_STATE};

    private TextView appVersionView;
    private TextView imeiView;

    @Override
    protected String[] getDesiredPermissions() {
        return(PERMS);
    }

    @Override
    protected void onPermissionDenied() {
        Toast
                .makeText(this, "No permissions", Toast.LENGTH_LONG)
                .show();
        finish();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onReady(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        appVersionView = findViewById(R.id.appVersionView);
        appVersionView.setText(BuildConfig.VERSION_NAME);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imeiView = findViewById(R.id.imeiView);
        imeiView.setText(telephonyManager.getDeviceId());
    }
}
