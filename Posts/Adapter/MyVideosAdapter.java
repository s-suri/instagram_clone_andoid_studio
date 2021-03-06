package com.some.studychats.Posts.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.some.studychats.Posts.AAH_CustomViewHolder;
import com.some.studychats.Posts.AAH_VideosAdapter;
import com.some.studychats.Posts.Model.MyModel;
import com.some.studychats.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyVideosAdapter extends AAH_VideosAdapter {

    private final List<MyModel> list;
    private static final int TYPE_VIDEO = 0, TYPE_TEXT = 1;
    private Context context;


    public class MyViewHolder extends AAH_CustomViewHolder {
        final TextView tv;
        final ImageView img_vol, img_playback;
        boolean isMuted;
        public MyViewHolder(View x) {
            super(x);
            tv = x.findViewById(R.id.tv);
            img_vol = x.findViewById(R.id.img_vol);
            img_playback = x.findViewById(R.id.img_playback);

        }

        //override this method to get callback when video starts to play
        @Override
        public void videoStarted() {
            super.videoStarted();
            img_playback.setImageResource(R.drawable.ic_pause);
            if (isMuted) {
                muteVideo();
                img_vol.setImageResource(R.drawable.ic_mute);
            } else {
                unmuteVideo();
                img_vol.setImageResource(R.drawable.ic_unmute);
            }
        }

        @Override
        public void pauseVideo() {
            super.pauseVideo();
            img_playback.setImageResource(R.drawable.ic_play);
        }
    }


    public class MyTextViewHolder extends AAH_CustomViewHolder {
        final TextView tv;

        public MyTextViewHolder(View x) {
            super(x);
            tv = x.findViewById(R.id.tv);
        }
    }

    public MyVideosAdapter(List<MyModel> list_urls, Context context) {
        this.list = list_urls;
        this.context = context;

    }

    @Override
    public AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_TEXT) {
            View itemView1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_text, parent, false);
            return new MyTextViewHolder(itemView1);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_card, parent, false);
            return new MyViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(final AAH_CustomViewHolder holder, int position) {

        MyModel post = list.get(position);

        if (post.getName().startsWith("text")) {
            ((MyTextViewHolder) holder).tv.setText(post.getName());
        } else {
            ((MyViewHolder) holder).tv.setText(post.getName());

            //todo
            holder.setImageUrl(post.getImage_url());
            holder.setVideoUrl(post.getVideo_url());

            if (list.get(position).getImage_url() != null && !list.get(position).getImage_url().isEmpty()) {
                Picasso.get().load(holder.getImageUrl()).config(Bitmap.Config.RGB_565).into(holder.getAAH_ImageView());
            }

            holder.setLooping(true);

            ((MyViewHolder) holder).img_playback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.isPlaying()) {
                        holder.pauseVideo();
                        holder.setPaused(true);
                    } else {
                        holder.playVideo();
                        holder.setPaused(false);
                    }
                }
            });

            ((MyViewHolder) holder).img_vol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((MyViewHolder) holder).isMuted) {
                        holder.unmuteVideo();
                        ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_unmute);
                    } else {
                        holder.muteVideo();
                        ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_mute);
                    }
                    ((MyViewHolder) holder).isMuted = !((MyViewHolder) holder).isMuted;
                }
            });

            if (post.getVideo_url() == null) {
                ((MyViewHolder) holder).img_vol.setVisibility(View.GONE);
                ((MyViewHolder) holder).img_playback.setVisibility(View.GONE);
            } else {
                ((MyViewHolder) holder).img_vol.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).img_playback.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getName().startsWith("text")) {
            return TYPE_TEXT;
        } else return TYPE_VIDEO;
    }


}