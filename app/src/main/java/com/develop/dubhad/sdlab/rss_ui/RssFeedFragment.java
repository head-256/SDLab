package com.develop.dubhad.sdlab.rss_ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.rss.FeedItemAdapter;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RssFeedFragment extends Fragment {

    private RecyclerView rssFeedRecyclerView;
    
    private FeedItemAdapter feedItemAdapter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rss_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        rssFeedRecyclerView = view.findViewById(R.id.rss_feed_rv);
        
        rssFeedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Parser parser = new Parser();
        parser.execute("https://www.androidcentral.com/feed");
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> arrayList) {
                feedItemAdapter = new FeedItemAdapter(arrayList);
                rssFeedRecyclerView.setAdapter(feedItemAdapter);
            }

            @Override
            public void onError() {

            }
        });
    }
}
