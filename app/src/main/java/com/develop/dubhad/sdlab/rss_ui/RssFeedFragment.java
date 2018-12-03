package com.develop.dubhad.sdlab.rss_ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.develop.dubhad.sdlab.rss.FeedCacheManager;
import com.develop.dubhad.sdlab.rss.FeedItemAdapter;
import com.develop.dubhad.sdlab.rss.FeedUrlManager;
import com.develop.dubhad.sdlab.util.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final static int EDIT_FEED_URL_DIALOG_REQUEST_CODE = 400;
    private final static String EDIT_FEED_URL_DIALOG_TAG = "editFeedUrl";

    private RecyclerView rssFeedRecyclerView;

    private ProgressBar rssFeedProgressBar;
    private SwipeRefreshLayout rssFeedSwipeRefreshLayout;

    private ConstraintLayout noConnectionBanner;
    private ConstraintLayout rssErrorBanner;

    private FeedItemAdapter feedItemAdapter;


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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_feed_url:
                showEditFeedUrlDialog();
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

        Button tryAgainButton = view.findViewById(R.id.try_again_button);
        Button changeSourceButton = view.findViewById(R.id.change_source_button);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFeed(FeedUrlManager.getCurrentUrl(requireContext()));
            }
        });

        changeSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditFeedUrlDialog();
            }
        });

        rssFeedSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        rssFeedSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rssFeedSwipeRefreshLayout.setRefreshing(true);
                loadFeed(FeedUrlManager.getCurrentUrl(requireContext()));
            }
        });

        setupRssFeedRecyclerView();

        if (!FeedUrlManager.isCurrentUrlExist(requireContext())) {
            showEditFeedUrlDialog();
            return;
        }
        
        if (feedItemAdapter == null) {
            loadFeed(FeedUrlManager.getCurrentUrl(requireContext()));
            return;
        }
        
        if (savedInstanceState == null) {
            setupInitialStateDisplay();
        }
        else {
            rssFeedRecyclerView.setVisibility(savedInstanceState.getInt("rvVisibility"));
            rssFeedProgressBar.setVisibility(savedInstanceState.getInt("pbVisibility"));
            noConnectionBanner.setVisibility(savedInstanceState.getInt("ncbannerVisibility"));
            rssErrorBanner.setVisibility(savedInstanceState.getInt("rebannerVisibility"));
            rssFeedSwipeRefreshLayout.setRefreshing(savedInstanceState.getBoolean("srlVisibility"));
        }
        
        refreshRssFeedRecyclerView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("rvVisibility", rssFeedRecyclerView.getVisibility());
        outState.putInt("pbVisibility", rssFeedProgressBar.getVisibility());
        outState.putBoolean("srlVisibility", rssFeedSwipeRefreshLayout.isRefreshing());
        outState.putInt("ncbannerVisibility", noConnectionBanner.getVisibility());
        outState.putInt("rebannerVisibility", rssErrorBanner.getVisibility());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDialogPositiveClick(String feedUrl) {
        loadFeed(feedUrl);
        FeedUrlManager.saveFeedUrl(requireContext(), feedUrl);
    }

    private void loadFeed(String feedUrl) {
        setupStartFeedLoadDisplay();

        if (!NetworkUtil.isNetworkAvailable(requireContext())) {
            setupNoConnectionDisplay();
            tryDisplayCache();
            return;
        }
        
        Parser parser = new Parser();
        parser.execute(feedUrl);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(@NonNull ArrayList<Article> arrayList) {
                setupFeedLoadedDisplay();

                refreshRssFeedRecyclerView(arrayList);

                Snackbar.make(Objects.requireNonNull(getView()),
                        getString(R.string.feed_loaded_message), Snackbar.LENGTH_SHORT).show();

                FeedCacheManager.saveCache(requireActivity(), arrayList, 10);
            }

            @Override
            public void onError() {
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupFeedLoadErrorDisplay();
                        }
                    });
                }
            }
        });
    }

    private void showEditFeedUrlDialog() {
        DialogFragment dialogFragment = new EditFeedUrlDialogFragment();
        dialogFragment.setTargetFragment(this, EDIT_FEED_URL_DIALOG_REQUEST_CODE);
        dialogFragment.show(requireFragmentManager(), EDIT_FEED_URL_DIALOG_TAG);
    }

    private float getScreenWidthInches() {
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        return metrics.widthPixels / metrics.xdpi;
    }

    private void setupRssFeedRecyclerView() {
        float width = getScreenWidthInches();
        if (width <= 5) {
            rssFeedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        } else {
            rssFeedRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), (int) Math.ceil(width / 3)));
        }
        rssFeedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        rssFeedRecyclerView.setHasFixedSize(true);
    }

    private void refreshRssFeedRecyclerView() {
        rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
    }

    private void refreshRssFeedRecyclerView(List<Article> data) {
        feedItemAdapter = new FeedItemAdapter(data);
        rssFeedRecyclerView.swapAdapter(feedItemAdapter, false);
    }

    private void setupInitialStateDisplay() {
        noConnectionBanner.setVisibility(View.GONE);
        rssErrorBanner.setVisibility(View.GONE);
        rssFeedProgressBar.setVisibility(View.GONE);
        rssFeedSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupStartFeedLoadDisplay() {
        noConnectionBanner.setVisibility(View.GONE);
        rssErrorBanner.setVisibility(View.GONE);
        if (!rssFeedSwipeRefreshLayout.isRefreshing()) {
            rssFeedProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setupFeedLoadedDisplay() {
        noConnectionBanner.setVisibility(View.GONE);
        rssErrorBanner.setVisibility(View.GONE);
        rssFeedProgressBar.setVisibility(View.GONE);
        rssFeedSwipeRefreshLayout.setRefreshing(false);
        rssFeedRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setupFeedLoadErrorDisplay() {
        noConnectionBanner.setVisibility(View.GONE);
        rssFeedProgressBar.setVisibility(View.GONE);
        rssFeedSwipeRefreshLayout.setRefreshing(false);
        rssFeedRecyclerView.setVisibility(View.GONE);
        rssErrorBanner.setVisibility(View.VISIBLE);
    }

    private void setupNoConnectionDisplay() {
        rssFeedProgressBar.setVisibility(View.GONE);
        rssErrorBanner.setVisibility(View.GONE);
        rssFeedSwipeRefreshLayout.setRefreshing(false);
        rssFeedRecyclerView.setVisibility(View.VISIBLE);
        noConnectionBanner.setVisibility(View.VISIBLE);
    }

    private void tryDisplayCache() {
        if (FeedCacheManager.isCacheExist(requireActivity())) {
            refreshRssFeedRecyclerView(FeedCacheManager.getCache(requireActivity()));
        } else {
            refreshRssFeedRecyclerView(new ArrayList<Article>());
        }
    }
}
