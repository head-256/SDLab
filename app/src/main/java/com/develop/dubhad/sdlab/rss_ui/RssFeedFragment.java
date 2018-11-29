package com.develop.dubhad.sdlab.rss_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.rss.FeedItemAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class RssFeedFragment extends Fragment {

    private RecyclerView rssFeedRecyclerView;
    private ProgressBar rssFeedProgressBar;
    private SwipeRefreshLayout rssFeedSwipeRefreshLayout;
    private TextView emptyFeedView;

    private FeedItemAdapter feedItemAdapter;
    

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rss_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        emptyFeedView = view.findViewById(R.id.empty_feed_view);
        rssFeedProgressBar = view.findViewById(R.id.rss_feed_progress_bar);
        rssFeedSwipeRefreshLayout = view.findViewById(R.id.rss_feed_srl);
        rssFeedRecyclerView = view.findViewById(R.id.rss_feed_rv);

        rssFeedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        rssFeedRecyclerView.setItemAnimator(new DefaultItemAnimator());

        rssFeedSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        rssFeedSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rssFeedSwipeRefreshLayout.setRefreshing(true);
                loadFeed();
            }
        });
        
        if (feedItemAdapter == null) {
            loadFeed();
        }
        else {
            rssFeedProgressBar.setVisibility(View.GONE);
            rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
        }
    }
    
    private void loadFeed() {
        Parser parser = new Parser();
        parser.execute("https://www.androidcentral.com/feed");
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> arrayList) {
                emptyFeedView.setVisibility(View.GONE);
                rssFeedProgressBar.setVisibility(View.GONE);
                rssFeedSwipeRefreshLayout.setRefreshing(false);
                rssFeedRecyclerView.setVisibility(View.VISIBLE);
                
                feedItemAdapter = new FeedItemAdapter(arrayList);
                rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
                
                Snackbar.make(getView(), getString(R.string.feed_loaded_message), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rssFeedProgressBar.setVisibility(View.GONE);
                        rssFeedSwipeRefreshLayout.setRefreshing(false);
                        rssFeedRecyclerView.setVisibility(View.GONE);
                        emptyFeedView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
