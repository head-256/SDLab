package com.develop.dubhad.sdlab;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView appVersionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appVersionView = findViewById(R.id.appVersionView);
        appVersionView.setText(BuildConfig.VERSION_NAME);
    }
}
