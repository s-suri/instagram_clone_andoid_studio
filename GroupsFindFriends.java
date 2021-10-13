package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsFindFriends extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView FindFreindrecyclerList;
    DatabaseReference UsersRef;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FindFreindrecyclerList = (RecyclerView)findViewById(R.id.find_friends_recycler_list);
        FindFreindrecyclerList.setLayoutManager(new LinearLayoutManager(this));

        mToolbar = findViewById(R.id.find_friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friend");


        searchView = findViewById(R.id.find_freind_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {

                firebaseSearch(newText);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                firebaseSearch(newText);

                return false;
            }
        });


    }

    private void firebaseSearch(String newText) {

        String query = newText.toLowerCase();

        Query ChatsRef = UsersRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff");


        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(ChatsRef, Contacts.class).build();


        final FirebaseRecyclerAdapter<Contacts, FindFriendActivity.FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindFriendActivity.FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendActivity.FindFriendViewHolder holder, final int position, @NonNull Contacts model) {

                        holder.username.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visiy_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(GroupsFindFriends.this,SelectUsesProfileActivity.class);
                                profileIntent.putExtra("visit_usr_id",visiy_user_id);
                                startActivity(profileIntent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FindFriendActivity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_chat_fragment,viewGroup,false);
                        FindFriendActivity.FindFriendViewHolder viewHolder = new FindFriendActivity.FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };
        FindFreindrecyclerList.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UsersRef, Contacts.class).build();


        final FirebaseRecyclerAdapter<Contacts, FindFriendActivity.FindFriendViewHolder>  adapter =
                new FirebaseRecyclerAdapter<Contacts, FindFriendActivity.FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendActivity.FindFriendViewHolder holder, final int position, @NonNull Contacts model) {

                        holder.username.setText(model.getName());
                        holder.userStatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visiy_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(GroupsFindFriends.this,SelectUsesProfileActivity.class);
                                profileIntent.putExtra("visit_usr_id",visiy_user_id);
                                startActivity(profileIntent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FindFriendActivity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_chat_fragment,viewGroup,false);
                        FindFriendActivity.FindFriendViewHolder viewHolder = new FindFriendActivity.FindFriendViewHolder(view);
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
