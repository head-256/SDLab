package com.develop.dubhad.sdlab.rss;

import android.content.Context;
import android.content.SharedPreferences;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.authentication.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedUrlManager {

    public static void saveFeedUrl(@NonNull Context context, String feedUrl) {
        String login = Authentication.getCurrentUser().getLogin();
        SharedPreferences sharedPreferences = context.getSharedPreferences(login, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.feed_url_key), feedUrl);
        editor.apply();
    }

    @Nullable
    public static String getCurrentUrl(@NonNull Context context) {
        String login = Authentication.getCurrentUser().getLogin();
        SharedPreferences sharedPreferences = context.getSharedPreferences(login, Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.feed_url_key), null);
    }

    public static boolean isCurrentUrlExist(@NonNull Context context) {
        String login = Authentication.getCurrentUser().getLogin();
        SharedPreferences sharedPreferences = context.getSharedPreferences(login, Context.MODE_PRIVATE);
        return sharedPreferences.contains(context.getString(R.string.feed_url_key));
    }
}
