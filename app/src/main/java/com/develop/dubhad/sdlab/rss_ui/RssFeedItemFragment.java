package com.develop.dubhad.sdlab.rss_ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.ToolbarTitleListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RssFeedItemFragment extends Fragment {

    private WebView feedItemView;

    @Nullable
    private String title;

    @Nullable
    private String content;
    
    @Nullable
    private String link;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle passedData = getArguments();
        title = Objects.requireNonNull(passedData).getString(getString(R.string.feed_item_title_key));
        content = passedData.getString(getString(R.string.feed_item_content_key));
        link = passedData.getString(getString(R.string.feed_item_link_key));

        return inflater.inflate(R.layout.fragment_rss_feed_item, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.rss_feed_item_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedItemView = view.findViewById(R.id.rss_feed_item_wv);

        setupFeedItemView();

        ((ToolbarTitleListener) requireActivity()).updateTitle(title);

        if (content != null) {
            feedItemView.loadData("<style>img{display: inline; height: auto; max-width: 100%;} " +
                            "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + content,
                    null, "utf-8");
        } else {
            feedItemView.loadUrl(link);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupFeedItemView() {
        feedItemView.getSettings().setLoadWithOverviewMode(true);
        feedItemView.setHorizontalScrollBarEnabled(false);
        feedItemView.getSettings().setSupportZoom(true);
        feedItemView.getSettings().setBuiltInZoomControls(true);
        feedItemView.getSettings().setDisplayZoomControls(false);
        feedItemView.getSettings().setDomStorageEnabled(true);
        feedItemView.getSettings().setJavaScriptEnabled(true);
        feedItemView.setWebViewClient(new WebViewClient());
    }
}
