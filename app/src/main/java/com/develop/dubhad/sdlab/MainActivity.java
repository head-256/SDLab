package com.develop.dubhad.sdlab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import static com.develop.dubhad.sdlab.Util.PermissionUtil.hasPermission;
import static com.develop.dubhad.sdlab.Util.PermissionUtil.requestPermission;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 111;

    private TextView imeiView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView appVersionView = findViewById(R.id.appVersionView);
        appVersionView.setText(getAppVersionName());

        imeiView = findViewById(R.id.imeiView);

        if (!hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            requestPermission(this, Manifest.permission.READ_PHONE_STATE, REQUEST_READ_PHONE_STATE,
                    getString(R.string.read_phone_state_rationale));
        } else {
            imeiView.setText(getImei());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                    imeiView.setText(getImei());
                }
            }
        }
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
