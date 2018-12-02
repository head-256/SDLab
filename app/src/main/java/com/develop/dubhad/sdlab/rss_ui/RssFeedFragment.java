package com.develop.dubhad.sdlab.rss_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.rss.FeedCacheManager;
import com.develop.dubhad.sdlab.rss.FeedItemAdapter;
import com.develop.dubhad.sdlab.util.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class RssFeedFragment extends Fragment implements EditFeedUrlDialogFragment.EditFeedDialogListener {

    private static final String DEFAULT_FEED_URL = "https://www.androidcentral.com/feed";
    
    private RecyclerView rssFeedRecyclerView;
    private ProgressBar rssFeedProgressBar;
    private SwipeRefreshLayout rssFeedSwipeRefreshLayout;
    private ConstraintLayout noConnectionBanner;
    private ConstraintLayout rssErrorBanner;
    private Button tryAgainButton;
    private Button changeSourceButton;

    private FeedItemAdapter feedItemAdapter;
    
    private static String currentUrl;
    
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_rss_feed, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.rss_feed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_feed_url:
                DialogFragment dialogFragment = new EditFeedUrlDialogFragment();
                dialogFragment.setTargetFragment(this, 400);
                dialogFragment.show(getFragmentManager(), "editFeedUrl");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        noConnectionBanner = view.findViewById(R.id.no_connection_banner);
        rssErrorBanner = view.findViewById(R.id.rss_error_banner);
        rssFeedProgressBar = view.findViewById(R.id.rss_feed_progress_bar);
        rssFeedSwipeRefreshLayout = view.findViewById(R.id.rss_feed_srl);
        rssFeedRecyclerView = view.findViewById(R.id.rss_feed_rv);

        tryAgainButton = view.findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFeed(currentUrl);
            }
        });
        
        changeSourceButton = view.findViewById(R.id.change_source_button);
        changeSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new EditFeedUrlDialogFragment();
                dialogFragment.setTargetFragment(RssFeedFragment.this, 400);
                dialogFragment.show(getFragmentManager(), "editFeedUrl");
            }
        });


        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        float xInches = metrics.widthPixels / metrics.xdpi;
        Log.d("DISPLAY SIZE", Float.toString(xInches));
        if (xInches <= 5) {
            rssFeedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
        else {
            rssFeedRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), (int)Math.ceil(xInches / 3)));
        }
        rssFeedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        rssFeedRecyclerView.setHasFixedSize(true);

        rssFeedSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        rssFeedSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rssFeedSwipeRefreshLayout.setRefreshing(true);
                loadFeed(currentUrl);
            }
        });

        String login = Authentication.getCurrentUser().getLogin();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(login, Context.MODE_PRIVATE);
        currentUrl = sharedPreferences.getString("feedUrl", null);
        
        if (currentUrl == null) {
            DialogFragment dialogFragment = new EditFeedUrlDialogFragment();
            dialogFragment.setTargetFragment(this, 400);
            dialogFragment.show(getFragmentManager(), "editFeedUrl");
            return;
        }
        
        if (feedItemAdapter == null) {
            loadFeed(currentUrl);
        }
        else {
            rssFeedProgressBar.setVisibility(View.GONE);
            rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
        }
    }

    @Override
    public void onDialogPositiveClick(String feedUrl) {
        rssFeedRecyclerView.setVisibility(View.GONE);
        rssFeedProgressBar.setVisibility(View.VISIBLE);

        currentUrl = feedUrl;
        if (currentUrl != null) {
            loadFeed(currentUrl);
        }
        
        String login = Authentication.getCurrentUser().getLogin();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(login, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("feedUrl", feedUrl);
        editor.apply();
    }

    private void loadFeed(String feedUrl) {
        if (!rssFeedSwipeRefreshLayout.isRefreshing()) {
            rssFeedProgressBar.setVisibility(View.VISIBLE);
        }
        if (!NetworkUtil.isNetworkAvailable(requireContext())) {
            rssFeedProgressBar.setVisibility(View.GONE);
            rssErrorBanner.setVisibility(View.GONE);
            rssFeedSwipeRefreshLayout.setRefreshing(false);
            noConnectionBanner.setVisibility(View.VISIBLE);
            
            if (FeedCacheManager.isCacheExist(requireActivity())) {
                rssFeedRecyclerView.setVisibility(View.VISIBLE);
                feedItemAdapter = new FeedItemAdapter(FeedCacheManager.getCache(requireActivity()));
                rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
            }
            else {
                feedItemAdapter = new FeedItemAdapter(new ArrayList<Article>());
                rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
            }
            return;
        }
        Parser parser = new Parser();
        parser.execute(feedUrl);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> arrayList) {
                noConnectionBanner.setVisibility(View.GONE);
                rssErrorBanner.setVisibility(View.GONE);
                rssFeedProgressBar.setVisibility(View.GONE);
                rssFeedSwipeRefreshLayout.setRefreshing(false);
                rssFeedRecyclerView.setVisibility(View.VISIBLE);
                
                feedItemAdapter = new FeedItemAdapter(arrayList);
                rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
                
                Snackbar.make(getView(), getString(R.string.feed_loaded_message), Snackbar.LENGTH_SHORT).show();

                FeedCacheManager.saveCache(requireActivity(), arrayList, 10);
            }

            @Override
            public void onError() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noConnectionBanner.setVisibility(View.GONE);
                        rssFeedProgressBar.setVisibility(View.GONE);
                        rssFeedSwipeRefreshLayout.setRefreshing(false);
                        rssFeedRecyclerView.setVisibility(View.GONE);
                        rssErrorBanner.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
