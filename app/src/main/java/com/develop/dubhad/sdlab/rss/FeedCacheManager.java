package com.develop.dubhad.sdlab.rss;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.develop.dubhad.sdlab.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class FeedCacheManager {

    public static void saveCache(@NonNull Activity activity, @NonNull List<Article> data, int maxNumber) {
        List<Article> cachedData;
        if (data.size() < maxNumber) {
            cachedData = new ArrayList<>(data);
        } else {
            cachedData = data.subList(0, maxNumber);
        }

        Gson gson = new Gson();
        String jsonData = gson.toJson(cachedData);

        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(activity.getString(R.string.feed_cache_key), jsonData);
        editor.apply();
    }

    public static List<Article> getCache(@NonNull Activity activity) {
        if (!isCacheExist(activity)) {
            return new ArrayList<>();
        }

        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        String jsonData = sharedPreferences.getString(activity.getString(R.string.feed_cache_key), null);

        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<ArrayList<Article>>() {}.getType());
    }

    public static void clearCache(@NonNull Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(activity.getString(R.string.feed_cache_key));
        editor.apply();
    }

    public static boolean isCacheExist(@NonNull Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.contains(activity.getString(R.string.feed_cache_key));
    }
}
