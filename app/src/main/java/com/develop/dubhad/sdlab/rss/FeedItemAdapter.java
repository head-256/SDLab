package com.develop.dubhad.sdlab.rss;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.util.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;
import com.prof.rssparser.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        
        public TextView title;
        public TextView description;
        public ImageView image;
        public TextView pubDate;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            pubDate = itemView.findViewById(R.id.pub_date);
        }
    }
    
    private List<Article> feedItems;
    
    public FeedItemAdapter(List<Article> feedItems) {
        this.feedItems = feedItems;
    }
    
    public void refreshData(List<Article> feedItems) {
        this.feedItems.clear();
        this.feedItems.addAll(feedItems);
        notifyDataSetChanged();
    }
    
    public List<Article> getFeedItems() {
        return feedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View feedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_feed_item, parent, false);
        
        return new ViewHolder(feedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Article feedItem = feedItems.get(position);
        
        TextView title = holder.title;
        TextView description = holder.description;
        ImageView image = holder.image;
        TextView pubDate = holder.pubDate;
        
        title.setText(Html.fromHtml(feedItem.getTitle()));

        description.setText(Html.fromHtml(feedItem.getDescription().replaceAll("<img.+?>", ""))
                .toString().replaceAll("\n", "").trim());
        
        Glide.with(holder.itemView)
                .load(feedItem.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_broken_image_black_24dp))
                .into(image);

        Date date = feedItem.getPubDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String dateString = sdf.format(date);
        pubDate.setText(dateString);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtil.isNetworkAvailable(v.getContext())) {
                    Snackbar.make(v, "No connection", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                
                Bundle bundle = new Bundle();
                bundle.putString(v.getContext().getString(R.string.feed_item_title_key), feedItem.getTitle());
                bundle.putString(v.getContext().getString(R.string.feed_item_content_key), feedItem.getContent());
                bundle.putString(v.getContext().getString(R.string.feed_item_link_key), feedItem.getLink());
                Navigation.findNavController(v).navigate(R.id.rssFeedItemFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }
}
