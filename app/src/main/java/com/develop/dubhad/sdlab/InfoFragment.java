package com.develop.dubhad.sdlab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.develop.dubhad.sdlab.Util.PermissionUtil.hasPermission;
import static com.develop.dubhad.sdlab.Util.PermissionUtil.requestPermission;

public class InfoFragment extends Fragment {

    private static final int REQUEST_READ_PHONE_STATE = 111;

    private TextView imeiView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView appVersionView = view.findViewById(R.id.appVersionView);
        appVersionView.setText(getAppVersionName());

        imeiView = view.findViewById(R.id.imeiView);
        if (!hasPermission(getContext(), Manifest.permission.READ_PHONE_STATE)) {
            requestPermission(getActivity(), Manifest.permission.READ_PHONE_STATE, REQUEST_READ_PHONE_STATE,
                    getString(R.string.read_phone_state_rationale));
        } else {
            imeiView.setText(getImei());
        }
    }

    private String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getImei() {
        String imei = null;
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }
        }
        return imei;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                    imeiView.setText(getImei());
                }
            }
        }
    }
}
