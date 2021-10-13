package com.some.studychats.AdapterInstagram;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.some.studychats.FragmentsInstagram.PostDetailFragment;
import com.some.studychats.ModelInstagram.Post;
import com.some.studychats.R;
import com.some.studychats.SimpleVideoViewActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyFotosAdapter extends RecyclerView.Adapter<MyFotosAdapter.ImageViewHolder> {

    SimpleExoPlayer exoPlayer;
    private Context mContext;
    private List<Post> mPosts;
    FirebaseAuth auth;
    String currentUserID;
    private LayoutInflater inflater;


    public MyFotosAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyFotosAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.fotos_item_instagram, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyFotosAdapter.ImageViewHolder holder, final int position) {

        final Post post = mPosts.get(position);

        holder.post_video.setVisibility(View.GONE);
        holder.post_image.setVisibility(View.GONE);
        holder.post_camera.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        currentUserID = auth.getCurrentUser().getUid();



        if (post.getType().equals("image")) {
            holder.post_image.setVisibility(View.VISIBLE);

            Picasso.get().load(post.getPostimage()).into(holder.post_image);

        } else if (post.getType().equals("video")) {

            holder.post_video.setVisibility(View.VISIBLE);

            holder.post_camera.setVisibility(View.VISIBLE);


            Picasso.get().load(post.getPostimage()).into(holder.post_video);

            /*
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                Uri video = Uri.parse(mPosts.get(position).getPostimage());
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                holder.post_video.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(false);

            } catch (Exception e) {
                Log.e("ViewHolder", "exoplayer error" + e.toString());

            }

             */


        } else {
            holder.post_video.setVisibility(View.GONE);
            holder.post_image.setVisibility(View.GONE);

        }


        holder.post_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

               ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       new PostDetailFragment()).commit();
            }
        });

/*
        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(), Image.class);
                intent.putExtra("messageId",post.getPostimage());
                holder.itemView.getContext().startActivity(intent);
            }
        });


 */





        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();
            }
        });







        holder.delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Do you want to delete?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (post.getSender().equals(currentUserID)) {
                                    FirebaseDatabase.getInstance().getReference("Posts")
                                            .child(post.getPostid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(holder.itemView.getContext(), "You are not Admin !!", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image,post_video;
        TextView delete_post,post_camera;



        public ImageViewHolder(View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);
            post_video = itemView.findViewById(R.id.post_video);
            delete_post = itemView.findViewById(R.id.delete);
            post_camera = itemView.findViewById(R.id.post_camera);

        }
    }



}