package com.develop.dubhad.sdlab.rss_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.ToolbarTitleListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RssFeedItemFragment extends Fragment {

    private WebView feedItemView;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_rss_feed_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        feedItemView = view.findViewById(R.id.rss_feed_item_wv);
        
        setupFeedItemView();
        
        String feedItemTitle = getArguments().getString(getResources().getString(R.string.feed_item_title_key));
        ((ToolbarTitleListener)requireActivity()).updateTitle(feedItemTitle);
        
        String feedItemContent = getArguments().getString(getResources().getString(R.string.feed_item_content_key));
        
        feedItemView.loadData("<style>img{display: inline; height: auto; max-width: 100%;} " +
                "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + feedItemContent, 
                null, "utf-8");
    }
    
    private void setupFeedItemView() {
        feedItemView.getSettings().setLoadWithOverviewMode(true);
        feedItemView.setHorizontalScrollBarEnabled(false);
        feedItemView.getSettings().setSupportZoom(true);
        feedItemView.getSettings().setBuiltInZoomControls(true);
        feedItemView.getSettings().setDisplayZoomControls(false);
        feedItemView.setWebChromeClient(new WebChromeClient());
    }
}
