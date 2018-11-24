package com.develop.dubhad.sdlab;

import android.app.Application;

public class App extends Application {
    
    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        
        app = this;
    }
    
    public static Application getApp() {
        return app;
    }
}
