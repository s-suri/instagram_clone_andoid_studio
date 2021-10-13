package com.some.studychats.Posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.AdapterInstagram.StoryAdapter;
import com.some.studychats.CommentsActivityInstagram;
import com.some.studychats.EditImageActivity;
import com.some.studychats.FollowersActivityInstagram;
import com.some.studychats.FragmentsInstagram.HomeFragment;
import com.some.studychats.FragmentsInstagram.ProfileFragment;
import com.some.studychats.ModelInstagram.Post;
import com.some.studychats.ModelInstagram.Story;
import com.some.studychats.ModelInstagram.User;
import com.some.studychats.PostActivityInstagram;
import com.some.studychats.PostActivityInstagramVideo;
import com.some.studychats.Posts.Activity.MainActivity;
import com.some.studychats.Posts.Adapter.MyVideosAdapter;
import com.some.studychats.Posts.Model.MyModel;
import com.some.studychats.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    SimpleExoPlayer exoPlayer;
    AAH_CustomRecyclerView recyclerView;
    private HomeFragment.PostAdapter postAdapter;
    private List<MyModel> postList;
    Fragment selectedfragment = null;
    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;
    private List<String> followingList;
    ProgressBar progress_circular;
    ImageView openPhotEditer,addImage;
    Dialog myDialog;
    TextView post_image,post_video;
    FirebaseAuth auth;
    String currentUserID;
    LinearLayoutManager mLayoutManager;
    int currentItems,totalItems,scrollOutItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_instagram);

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();

        readPosts();

        post_image = findViewById(R.id.image);
        post_video = findViewById(R.id.video);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setActivity(this);


        recyclerView.setPlayOnlyFirstVideo(true);

        recyclerView.setCheckForMp4(false); //true by default

        recyclerView.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default

        recyclerView.setDownloadVideos(true); // false by default

        recyclerView.setVisiblePercent(50); // percentage of View that needs to be visible to start playing



        List<String> urls = new ArrayList<>();
        for (MyModel object : postList) {
            if (object.getVideo_url() != null && object.getVideo_url().contains("http"))
                urls.add(object.getVideo_url());
        }

        recyclerView.preDownload(urls);



        recyclerView.smoothScrollBy(0,1);
        recyclerView.smoothScrollBy(0,-1);


        post_image.setVisibility(View.GONE);
        post_video.setVisibility(View.GONE);

        myDialog = new Dialog(PostActivity.this);


        recyclerView_story = findViewById(R.id.recycler_view_story);
        openPhotEditer = findViewById(R.id.add_photoediter);
        addImage = findViewById(R.id.add_image);


        currentItems = mLayoutManager.getChildCount();
        totalItems = mLayoutManager.getItemCount();
        scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_image.setVisibility(View.VISIBLE);
                post_video.setVisibility(View.VISIBLE);
                openPhotEditer.setVisibility(View.GONE);

            }
        });

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedfragment = null;
                Intent intent = new Intent(PostActivity.this, PostActivityInstagram.class);
                startActivity(intent);

            }
        });

        post_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedfragment = null;
                Intent  intent = new Intent(PostActivity.this, PostActivityInstagramVideo.class);
                startActivity(intent);

            }
        });

        openPhotEditer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(PostActivity.this, EditImageActivity.class);
                startActivity(intent);
            }
        });


        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_story.setLayoutManager(linearLayoutManager);
        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(PostActivity.this, storyList);
        recyclerView_story.setAdapter(storyAdapter);

        progress_circular = findViewById(R.id.progress_circular);

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
                readStory();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readPosts(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MyModel post = snapshot.getValue(MyModel.class);

                    postList.add(post);
                }

                MyVideosAdapter mAdapter = new MyVideosAdapter(postList, PostActivity.this);
                recyclerView.setAdapter(mAdapter);


                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readStory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long timecurrent = System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("", 0, 0, "",
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for (String id : followingList) {
                    int countStory = 0;
                    Story story = null;
                    for (DataSnapshot snapshot : dataSnapshot.child(id).getChildren()) {
                        story = snapshot.getValue(Story.class);
                        if (timecurrent > story.getTimestart() && timecurrent < story.getTimeend()) {
                            countStory++;
                        }
                    }
                    if (countStory > 0){
                        storyList.add(story);
                    }
                }

                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ImageViewHolder> {

        SimpleExoPlayer exoPlayer;

        private Context mContext;
        private List<Post> mPosts;

        private FirebaseUser firebaseUser;
        private LayoutInflater inflater;

        public PostAdapter(Context context, List<Post> posts){
            mContext = context;
            mPosts = posts;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_instagram, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {

            holder.ic_video.setVisibility(View.GONE);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final Post post = mPosts.get(position);

            holder.post_video.setVisibility(View.GONE);
            holder.post_image.setVisibility(View.GONE);



//            if (currentItems + scrollOutItems == totalItems) {
//            }
//            if (currentItems + scrollOutItems != totalItems) {
//            }


            if (post.getType().equals("image")){

                holder.post_image.setVisibility(View.VISIBLE);
                Picasso.get().load(post.getPostimage()).into(holder.post_image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(holder.itemView.getContext(), String.valueOf(totalItems), Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else if (post.getType().equals("video")){

                holder.post_video.setVisibility(View.VISIBLE);
                holder.ic_video.setVisibility(View.VISIBLE);

                //      Picasso.get().load(post.getPostimage()).into(holder.post_video);


//                try {
//                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(getContext()).build();
//                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//                    exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(getContext());
//                    Uri video = Uri.parse(post.getThumbnail());
//                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
//                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//                    MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
//                    holder.post_video.setPlayer(exoPlayer);
//                    exoPlayer.prepare(mediaSource);
//                    exoPlayer.setPlayWhenReady(true);
//                }
//                catch (Exception e){
//                    Log.e("ViewHolder","exoplayer error" + e.toString());
//
//                }

                holder.post_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(holder.itemView.getContext(),String.valueOf(totalItems), Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(holder.itemView.getContext(), DirectVideo.class);
//                        intent.putExtra("visit_user_id", post.getPublisher());
//                        intent.putExtra("messageId", post.getThumbnail());
//                        holder.itemView.getContext().startActivity(intent);

                    }
                });

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                Uri video = Uri.parse(mPosts.get(position).getThumbnail());
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                holder.post_video.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);

                Toast.makeText(holder.itemView.getContext(), String.valueOf(position)+"      "+String.valueOf(currentItems), Toast.LENGTH_SHORT).show();
                if (position ==currentItems){
                    exoPlayer.setPlayWhenReady(true);
                }
                else
                {
                    exoPlayer.setPlayWhenReady(false);
                }

            }
            else{
                holder.post_video.setVisibility(View.GONE);
                holder.post_image.setVisibility(View.GONE);

            }


            if (post.getDescription().equals("")){
                holder.description.setVisibility(View.GONE);
            } else {
                holder.description.setVisibility(View.VISIBLE);
                holder.description.setText(post.getDescription());
            }

            publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
            isLiked(post.getPostid(), holder.like);
            isSaved(post.getPostid(), holder.save);
            nrLikes(holder.likes, post.getPostid());
            getCommetns(post.getPostid(), holder.comments);

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.like.getTag().equals("like")) {
                        FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                                .child(firebaseUser.getUid()).setValue(true);
                        addNotification(post.getPublisher(), post.getPostid());
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                                .child(firebaseUser.getUid()).removeValue();
                    }
                }
            });

            holder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.save.getTag().equals("save")){
                        FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                                .child(post.getPostid()).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                                .child(post.getPostid()).removeValue();
                    }
                }
            });

            holder.image_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

/*
                Intent intent = new Intent(mContext, MainActivityInstagram.class);
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);

 */


                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", post.getPublisher());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();


                }
            });

            holder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", post.getPublisher());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();


                }
            });

            holder.publisher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*

                Intent intent = new Intent(mContext, MainActivityInstagram.class);
                intent.putExtra("publisherid", post.getPublisher());
                mContext.startActivity(intent);

                 */


                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", post.getPublisher());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();


                }
            });
//
            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentsActivityInstagram.class);
                    intent.putExtra("postid", post.getPostid());
                    intent.putExtra("publisherid", post.getPublisher());
                    mContext.startActivity(intent);
                }
            });

            holder.comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentsActivityInstagram.class);
                    intent.putExtra("postid", post.getPostid());
                    intent.putExtra("publisherid", post.getPublisher());
                    mContext.startActivity(intent);
                }
            });


            holder.post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//                    editor.putString("postid", post.getPostid());
//                    editor.apply();
//
//                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            new PostDetailFragment()).commit();
                }
            });



            holder.likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, FollowersActivityInstagram.class);
                    intent.putExtra("id", post.getPostid());
                    intent.putExtra("title", "likes");
                    mContext.startActivity(intent);
                }
            });

            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.edit:
                                    editPost(post.getPostid());
                                    return true;
                                case R.id.delete:
                                    final String id = post.getPostid();
                                    FirebaseDatabase.getInstance().getReference("Posts")
                                            .child(post.getPostid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        deleteNotifications(id, firebaseUser.getUid());
                                                    }
                                                }
                                            });
                                    return true;
                                case R.id.report:
                                    Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.inflate(R.menu.post_menu);
                    if (!post.getPublisher().equals(firebaseUser.getUid())){
                        popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    }
                    popupMenu.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {

            public ImageView image_profile, post_image, like, comment, save, more;
            public TextView username, likes, publisher, description, comments,ic_video;

            private PlayerView post_video;

            public ImageViewHolder(View itemView) {
                super(itemView);

                image_profile = itemView.findViewById(R.id.image_profile);
                username = itemView.findViewById(R.id.username);
                post_image = itemView.findViewById(R.id.post_image);
                like = itemView.findViewById(R.id.like);
                comment = itemView.findViewById(R.id.comment);
                save = itemView.findViewById(R.id.save);
                likes = itemView.findViewById(R.id.likes);
                publisher = itemView.findViewById(R.id.publisher);
                description = itemView.findViewById(R.id.description);
                comments = itemView.findViewById(R.id.comments);
                more = itemView.findViewById(R.id.more);
                post_video = itemView.findViewById(R.id.post_video);
                ic_video = itemView.findViewById(R.id.ic_video);



            }
        }

        private void addNotification(String userid, String postid){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", firebaseUser.getUid());
            hashMap.put("text", "liked your post");
            hashMap.put("postid", postid);
            hashMap.put("ispost", true);

            reference.push().setValue(hashMap);
        }

        private void deleteNotifications(final String postid, String userid){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.child("postid").getValue().equals(postid)){
                            snapshot.getRef().removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void nrLikes(final TextView likes, String postId){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    likes.setText(dataSnapshot.getChildrenCount()+" likes");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void getCommetns(String postId, final TextView comments){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    comments.setText("View All "+dataSnapshot.getChildrenCount()+" Comments");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(userid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Picasso.get().load(user.getImageurl()).into(image_profile);
                    username.setText(user.getUsername());
                    publisher.setText(user.getUsername());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void isLiked(final String postid, final ImageView imageView){

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Likes").child(postid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                        imageView.setImageResource(R.drawable.ic_liked);
                        imageView.setTag("liked");
                    } else{
                        imageView.setImageResource(R.drawable.ic_like);
                        imageView.setTag("like");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void isSaved(final String postid, final ImageView imageView){

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Saves").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postid).exists()){
                        imageView.setImageResource(R.drawable.ic_save_black);
                        imageView.setTag("saved");
                    } else{
                        imageView.setImageResource(R.drawable.ic_savee_black);
                        imageView.setTag("save");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void editPost(final String postid){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Edit Post");

            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            editText.setLayoutParams(lp);
            alertDialog.setView(editText);

            getText(postid, editText);

            alertDialog.setPositiveButton("Edit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("description", editText.getText().toString());

                            FirebaseDatabase.getInstance().getReference("Posts")
                                    .child(postid).updateChildren(hashMap);
                        }
                    });
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            alertDialog.show();
        }

        private void getText(String postid, final EditText editText){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts")
                    .child(postid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    editText.setText(dataSnapshot.getValue(Post.class).getDescription());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public class MyVideosAdapter extends AAH_VideosAdapter {

        private final List<MyModel> list;
        private static final int TYPE_VIDEO = 0, TYPE_TEXT = 1;
        private Context context;


        public class MyViewHolder extends AAH_CustomViewHolder {
            final TextView tv;
            final ImageView img_vol, img_playback;
            boolean isMuted;
            VideoView auto_update_video_1,auto_update_video_2,auto_update_video_3,auto_update_video_4,auto_update_video_5,auto_update_video_6;

            public MyViewHolder(View x) {
                super(x);
                tv = x.findViewById(R.id.tv);
                img_vol = x.findViewById(R.id.img_vol);
                img_playback = x.findViewById(R.id.img_playback);
                auto_update_video_1 = x.findViewById(R.id.auto_update_video_1);
                auto_update_video_2 = x.findViewById(R.id.auto_update_video_2);
                auto_update_video_3 = x.findViewById(R.id.auto_update_video_3);
                auto_update_video_4 = x.findViewById(R.id.auto_update_video_4);
                auto_update_video_5 = x.findViewById(R.id.auto_update_video_5);
                auto_update_video_6 = x.findViewById(R.id.auto_update_video_6);


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
            View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_card, parent, false);
                return new MyViewHolder(itemView);
//            if (viewType==TYPE_TEXT) {
//                View itemView1 = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.single_text, parent, false);
//                return new MyTextViewHolder(itemView1);
//            } else {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.single_card, parent, false);
//                return new MyViewHolder(itemView);
//            }
        }

        @Override
        public void onBindViewHolder(final AAH_CustomViewHolder holder, int position) {

            MyModel post = list.get(position);

//            if (post.getName().startsWith("text")) {
//                ((MyTextViewHolder) holder).tv.setText(post.getName());
//            } else {
                ((MyViewHolder) holder).tv.setText(post.getName());

                //todo
                holder.setImageUrl(post.getImage_url());
                holder.setVideoUrl(post.getImage_url());

                MediaController controller = new MediaController(PostActivity.this);
                ((MyViewHolder) holder).auto_update_video_1.setMediaController(controller);
                ((MyViewHolder) holder).auto_update_video_1.setVideoPath(post.getImage_url());
                ((MyViewHolder) holder).auto_update_video_1.start();


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
    //    }


        @Override
        public int getItemCount() {
            return list.size();
        }


//        @Override
//        public int getItemViewType(int position) {
//            if (list.get(position).getName().startsWith("text")) {
//                return TYPE_TEXT;
//            } else return TYPE_VIDEO;
//        }
    }
}
