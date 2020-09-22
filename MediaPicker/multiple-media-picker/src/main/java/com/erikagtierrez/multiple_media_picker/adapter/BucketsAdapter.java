package com.erikagtierrez.multiple_media_picker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.erikagtierrez.multiple_media_picker.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.MyViewHolder> {
    private List<String> bucketNames, bitmapList;
    private Context context;
    private int fallbackImageId;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail = view.findViewById(R.id.image);
        }
    }

    public BucketsAdapter(int fallbackImageId, List<String> bucketNames, List<String> bitmapList, Context context) {
        this.fallbackImageId = fallbackImageId;
        this.bucketNames = bucketNames;
        this.bitmapList = bitmapList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        bucketNames.get(position);
        holder.title.setText(bucketNames.get(position));
        Glide.with(context).load("file://" + bitmapList.get(position)).error(this.fallbackImageId).override(300, 300).centerCrop().into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return bucketNames.size();
    }
}

