package com.develop.dubhad.sdlab.rss;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.List;

public class FeedCacheManager {
    
    public static void saveCache(Activity activity, List<Article> data, int maxNumber) {
        List<Article> cachedData = data.subList(0, maxNumber);

        Gson gson = new Gson();
        String jsonData = gson.toJson(cachedData);

        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("feedCache", jsonData);
        editor.apply();
    }
    
    public static List<Article> getCache(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        String jsonData = sharedPreferences.getString("feedCache", null);
        
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<ArrayList<Article>>(){}.getType());
    }
    
    public static void clearCache(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("feedCache");
        editor.apply();
    }
    
    public static boolean isCacheExist(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.contains("feedCache");
    }
}
