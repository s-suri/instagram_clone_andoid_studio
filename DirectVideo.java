package com.some.studychats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DirectVideo extends AppCompatActivity {

    RecyclerView mRecycler;
    FirebaseDatabase database;
    DatabaseReference reference;
    Intent intent;
    String userid,messageId;
    FirebaseUser fuser;

    SimpleExoPlayer exoPlayer;
    private PlayerView mExoplayer;
    TextView mtextView;
    Window window;

    private FirebaseUser currentUser;
    FirebaseAuth mAuth;

    private String currentUserID;



    private String saveCurrentTime, saveCurrentDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_video);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }


        intent = getIntent();
        userid = intent.getStringExtra("visit_user_id");
        messageId = intent.getStringExtra("messageId");


        mExoplayer = findViewById(R.id.exoplayer_view);


        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(getApplicationContext()).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(getApplicationContext());
            Uri video = Uri.parse(messageId);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
            mExoplayer.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
        catch (Exception e){
            Log.e("ViewHolder","exoplayer error" + e.toString());

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        state("online");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        state("offline");
        exoPlayer.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        exoPlayer.stop();
        state("offline");

    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.stop();
        state("offline");



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }



    private void state(String online) {

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("userState");


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
        saveCurrentDate = currendateFormat.format(calForDate.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("state", online);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("time", saveCurrentTime);

        reference.updateChildren(hashMap);
    }
}
