package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Pair;
import android.view.Window;
import android.widget.EditText;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.ModelInstagram.User;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private RecyclerView FindFreindrecyclerList;
    DatabaseReference UsersRef;
    SearchView searchView;
    EditText search_users;
    FirebaseAuth mAuth;
    String currentUserID;
    Window window;

    private String saveCurrentTime, saveCurrentDate;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        if (Build.VERSION.SDK_INT>=21){
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }
        UsersRef = FirebaseDatabase.getInstance().getReference().child(currentUserID);
        FindFreindrecyclerList = (RecyclerView)findViewById(R.id.find_friends_recycler_list);
        FindFreindrecyclerList.setLayoutManager(new LinearLayoutManager(this));


        search_users = findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                firebaseSearch(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void firebaseSearch(String newText) {

        String query = newText.toLowerCase();

        Query ChatsRef = UsersRef.orderByChild("search").startAt(newText).endAt(newText + "\uf8ff");

        FirebaseRecyclerOptions<Comment> options=
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(ChatsRef, Comment.class).build();


        final FirebaseRecyclerAdapter<Comment, ContactViewHolder>  adapter =
                new FirebaseRecyclerAdapter<Comment, ContactViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position, @NonNull final Comment model) {

                        holder.username.setText(model.getFullname());

                        holder.userStatus.setVisibility(View.VISIBLE);
                        holder.userStatus.setText(model.getBio());


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    String retImage = dataSnapshot.child("imageurl").getValue().toString();

                                    Picasso.get().load(retImage).into(holder.profileImage);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent profileIntent = new Intent(ContactsActivity.this,MessageActivity.class);
                                profileIntent.putExtra("userid",model.getReceiver());
                                startActivity(profileIntent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_chat_fragment,viewGroup,false);
                        ContactViewHolder viewHolder = new ContactViewHolder(view);
                        return viewHolder;
                    }
                };
        FindFreindrecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();

        state("online");


        FirebaseRecyclerOptions<Comment> options=
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(UsersRef, Comment.class).build();


        final FirebaseRecyclerAdapter<Comment, ContactViewHolder>  adapter =
                new FirebaseRecyclerAdapter<Comment, ContactViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position, @NonNull final Comment model) {


                        holder.username.setText(model.getFullname());
                        holder.userStatus.setVisibility(View.VISIBLE);
                        holder.userStatus.setText(model.getBio());


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())
                                {
                                    String retImage = dataSnapshot.child("imageurl").getValue().toString();
                                    Picasso.get().load(retImage).into(holder.profileImage);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent profileIntent = new Intent(ContactsActivity.this,MessageActivity.class);
                                profileIntent.putExtra("userid",model.getReceiver());
                                startActivity(profileIntent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_chat_fragment,viewGroup,false);
                        ContactViewHolder viewHolder = new ContactViewHolder(view);
                        return viewHolder;
                    }
                };
        FindFreindrecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

public static  class ContactViewHolder extends RecyclerView.ViewHolder{

    TextView username, userStatus;

    CircleImageView profileImage;


    public ContactViewHolder(@NonNull View itemView) {

        super(itemView);

        username = itemView.findViewById(R.id.user_profile_name);
        userStatus = itemView.findViewById(R.id.user_status);
        profileImage = itemView.findViewById(R.id.users_profile_image);


    }
}





    @Override
    protected void onPause() {
        super.onPause();

        state("offline");

    }

    @Override
    protected void onStop() {
        super.onStop();
        state("offline");

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    private void state(String online) {

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("userState");


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
