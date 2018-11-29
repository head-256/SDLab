package com.develop.dubhad.sdlab.rss;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.develop.dubhad.sdlab.R;
import com.prof.rssparser.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View feedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_feed_item, parent, false);
        
        return new ViewHolder(feedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article feedItem = feedItems.get(position);
        
        TextView title = holder.title;
        TextView description = holder.description;
        ImageView image = holder.image;
        TextView pubDate = holder.pubDate;
        
        title.setText(feedItem.getTitle());

        description.setText(Html.fromHtml(feedItem.getDescription()));
        
        Glide.with(holder.itemView)
                .load(feedItem.getImage())
                .into(image);

        Date date = feedItem.getPubDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String dateString = sdf.format(date);
        pubDate.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }
}