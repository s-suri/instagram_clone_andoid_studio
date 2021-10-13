package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.some.studychats.ModelInstagram.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView FindFreindrecyclerList;
    DatabaseReference UsersRef;
    EditText searchView;
    Window window;

    DatabaseReference reference;
    private String saveCurrentTime, saveCurrentDate;

    private FirebaseUser currentUser;
    FirebaseAuth mAuth;

    private String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        if (Build.VERSION.SDK_INT>=21){
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }




        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FindFreindrecyclerList = (RecyclerView)findViewById(R.id.find_friends_recycler_list);
        FindFreindrecyclerList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        searchView = findViewById(R.id.find_freind_search);





        searchView.addTextChangedListener(new TextWatcher() {
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
        state("offline");


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






    private void firebaseSearch(String newText) {

        String query = newText.toLowerCase();

        Query ChatsRef = UsersRef.orderByChild("search").startAt(newText).endAt(newText + "\uf8ff");


        FirebaseRecyclerOptions<User> options=
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(ChatsRef, User.class).build();


        final FirebaseRecyclerAdapter<User,FindFriendViewHolder>  adapter =
                new FirebaseRecyclerAdapter<User, FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull User model) {

                        holder.username.setText(model.getFullname());
                        holder.userStatus.setText(model.getBio());
                        Picasso.get().load(model.getImageurl()).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {

                                String visiy_user_id = getRef(position).getKey();

                                Intent chatIntent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                                chatIntent.putExtra("visit_usr_id",visiy_user_id);

                                /*

                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(holder.profileImage, "imageTransition");

                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), pairs);

 holder.itemView.getContext().startActivity(chatIntent, options.toBundle());

                                 */

                                holder.itemView.getContext().startActivity(chatIntent);


                            }
                        });


                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.instagram_theme,viewGroup,false);
                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
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

        FirebaseRecyclerOptions<User> options=
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(UsersRef, User.class).build();


        final FirebaseRecyclerAdapter<User,FindFriendViewHolder>  adapter =
                new FirebaseRecyclerAdapter<User, FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull User model) {

                        holder.username.setText(model.getFullname());
                        holder.userStatus.setText(model.getBio());
                        Picasso.get().load(model.getImageurl()).into(holder.profileImage);



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {

                                String visiy_user_id = getRef(position).getKey();

                                Intent chatIntent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                                chatIntent.putExtra("visit_usr_id",visiy_user_id);


                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(holder.profileImage, "imageTransition");

                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), pairs);


                                holder.itemView.getContext().startActivity(chatIntent, options.toBundle());


                            }
                        });




                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.instagram_theme,viewGroup,false);
                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };
        FindFreindrecyclerList.setAdapter(adapter);
        adapter.startListening();
    }




    public static  class FindFriendViewHolder extends RecyclerView.ViewHolder{

        TextView username, userStatus;

        CircleImageView profileImage;


        public FindFriendViewHolder(@NonNull View itemView) {

            super(itemView);

            username = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);


        }
    }
}
