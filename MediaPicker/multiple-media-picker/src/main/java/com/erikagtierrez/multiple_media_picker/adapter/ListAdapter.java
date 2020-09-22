package com.erikagtierrez.multiple_media_picker.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erikagtierrez.multiple_media_picker.R;
import com.erikagtierrez.multiple_media_picker.model.FileItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<FileItem> fileItems;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public TextView titleTextView;
        public ViewHolder(LinearLayout v) {
            super(v);
            this.imageView = v.findViewById(R.id.fileImage);
            this.titleTextView = v.findViewById(R.id.fileName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(ArrayList<FileItem> fileItems) {
        this.fileItems = fileItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.imageView.setText(mDataset[position]);
        final FileItem fileItem = this.fileItems.get(position);
        holder.imageView.setImageResource(fileItem.getImage());
        holder.titleTextView.setText(fileItem.getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.fileItems.size();
    }
}
