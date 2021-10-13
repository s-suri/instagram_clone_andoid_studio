package com.some.studychats;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class GallaryImageAdapter extends RecyclerView.Adapter<GallaryImageAdapter.ImageViewHolder> {

    Context context;
    String[] urlList;
    RecyclerViewClickListener clickListener;

    public GallaryImageAdapter(Context context,String[] urlList,RecyclerViewClickListener clickListener)
    {
        this.context = context;
        this.urlList = urlList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallary_item,parent,false);
       return new ImageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        String currentTime = urlList[position];
        ImageView imageView = holder.imageView;
        ProgressBar progressBar = holder.progressBar;


    }



    @Override
    public int getItemCount() {
        return urlList.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        ProgressBar progressBar;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            progressBar = itemView.findViewById(R.id.progBar);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            clickListener.onClick(v,getAdapterPosition());
        }
    }
}
