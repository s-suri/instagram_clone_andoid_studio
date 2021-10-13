package com.some.studychats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.some.studychats.AdapterInstagram.CommentAdapter;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.ModelInstagram.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivityInstagram extends AppCompatActivity {


        private RecyclerView recyclerView;
        private CommentAdapter commentAdapter;
        private List<Comment> commentList;

        EditText addcomment;
        ImageView image_profile;
        TextView post;

        String postid;
        String publisherid;

        FirebaseUser firebaseUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_comments_instagram);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Comments");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            Intent intent = getIntent();
            postid = intent.getStringExtra("postid");
            publisherid = intent.getStringExtra("publisherid");

            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            commentList = new ArrayList<>();
            commentAdapter = new CommentAdapter(this, commentList, postid);
            recyclerView.setAdapter(commentAdapter);

            post = findViewById(R.id.post);
            addcomment = findViewById(R.id.add_comment);
            image_profile = findViewById(R.id.image_profile);

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addcomment.getText().toString().equals("")){
                        Toast.makeText(CommentsActivityInstagram.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                    } else {
                        addComment();
                    }
                }
            });

            getImage();
            readComments();

        }

        private void addComment(){

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

            String commentid = reference.push().getKey();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("comment", addcomment.getText().toString());
            hashMap.put("publisher", firebaseUser.getUid());
            hashMap.put("commentid", commentid);

            reference.child(commentid).setValue(hashMap);
            addNotification();
            addcomment.setText("");

        }

        private void addNotification(){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherid);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", firebaseUser.getUid());
            hashMap.put("text", "commented: "+addcomment.getText().toString());
            hashMap.put("postid", postid);
            hashMap.put("ispost", true);

            reference.push().setValue(hashMap);
        }

        private void getImage(){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Picasso.get().load(user.getImageurl()).into(image_profile);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void readComments(){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    commentList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Comment comment = snapshot.getValue(Comment.class);
                        commentList.add(comment);
                    }

                    commentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
