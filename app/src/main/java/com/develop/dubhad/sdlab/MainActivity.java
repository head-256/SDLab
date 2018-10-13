package com.develop.dubhad.sdlab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import static com.develop.dubhad.sdlab.Util.PermissionUtil.hasPermission;
import static com.develop.dubhad.sdlab.Util.PermissionUtil.requestPermission;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 111;
    private static final String PROFILE_FRAGMENT_TAG = "profile_fragment";

    private TextView imeiView;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.profile:
                                if (getSupportFragmentManager().findFragmentById(R.id.content_frame) == null) {
                                    getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new ProfileFragment(),
                                            PROFILE_FRAGMENT_TAG).commit();
                                }
                                return true;
                        }

                        return true;
                    }
                });

        /*TextView appVersionView = findViewById(R.id.appVersionView);
        appVersionView.setText(getAppVersionName());

        imeiView = findViewById(R.id.imeiView);

        if (!hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            requestPermission(this, Manifest.permission.READ_PHONE_STATE, REQUEST_READ_PHONE_STATE,
                    getString(R.string.read_phone_state_rationale));
        } else {
            imeiView.setText(getImei());
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                    //imeiView.setText(getImei());
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
