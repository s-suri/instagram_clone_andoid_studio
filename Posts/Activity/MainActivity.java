package com.some.studychats.Posts.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.Posts.AAH_CustomRecyclerView;
import com.some.studychats.Posts.AAH_CustomViewHolder;
import com.some.studychats.Posts.AAH_VideosAdapter;
import com.some.studychats.Posts.Adapter.MyVideosAdapter;
import com.some.studychats.Posts.Model.MyModel;
import com.some.studychats.Posts.PostActivityInstagramVideo;
import com.some.studychats.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AAH_CustomRecyclerView recyclerView;
    private List<String> followingList;
    private TextView add_video;
    VideoView auto_update_video_1,auto_update_video_2,auto_update_video_3,auto_update_video_4,auto_update_video_5,auto_update_video_6;


    private final List<MyModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_close_video);
        Toast.makeText(this, "auto", Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.rv_home);
        auto_update_video_1 = findViewById(R.id.auto_update_video_1);
        auto_update_video_2 = findViewById(R.id.auto_update_video_2);
        auto_update_video_3 = findViewById(R.id.auto_update_video_3);
        auto_update_video_4 = findViewById(R.id.auto_update_video_4);
        auto_update_video_5 = findViewById(R.id.auto_update_video_5);
        auto_update_video_6 = findViewById(R.id.auto_update_video_6);

        readPosts();

        MyVideosAdapter mAdapter = new MyVideosAdapter(modelList,MainActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setActivity(this);

        recyclerView.setPlayOnlyFirstVideo(true);

        recyclerView.setCheckForMp4(false); //true by default

        recyclerView.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default

        recyclerView.setDownloadVideos(true); // false by default

        recyclerView.setVisiblePercent(50); // percentage of View that needs to be visible to start playing

        List<String> urls = new ArrayList<>();
        for (MyModel object : modelList) {
            if (object.getVideo_url() != null && object.getVideo_url().contains("http"))
                urls.add(object.getVideo_url());
        }
        recyclerView.preDownload(urls);

        recyclerView.setAdapter(mAdapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
        recyclerView.smoothScrollBy(0,1);
        recyclerView.smoothScrollBy(0,-1);


    }

    private void checkFollowing(){
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }

                readPosts();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerView.stopVideos();
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MyModel post = snapshot.getValue(MyModel.class);
                    modelList.add(post);
//                    for (String id : followingList){
//                        if (post.getPublisher().equals(id)){
//                            modelList.add(post);
//                        }
//                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

                MediaController controller = new MediaController(MainActivity.this);
                auto_update_video_1.setMediaController(controller);
                auto_update_video_1.setVideoPath(post.getImage_url());
                auto_update_video_1.start();


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

}
