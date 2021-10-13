package com.some.studychats.FragmentsInstagram;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.AdapterInstagram.UserAdapter;
import com.some.studychats.MessageActivity;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.ModelInstagram.User;
import com.some.studychats.Notifications.Client;
import com.some.studychats.Notifications.Data;
import com.some.studychats.Notifications.MyResponse;
import com.some.studychats.Notifications.Sender;
import com.some.studychats.Notifications.Token;
import com.some.studychats.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<User> userList;
        ProgressBar progress_circular;

        EditText search_bar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_search, container, false);


            progress_circular = view.findViewById(R.id.progress_circular);
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            search_bar = view.findViewById(R.id.search_bar);

            userList = new ArrayList<>();
            userAdapter = new UserAdapter(getContext(), userList);
            recyclerView.setAdapter(userAdapter);

            readUsers();
            search_bar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    searchUsers(charSequence.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            return view;
        }

        private void searchUsers(String s){
            Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                    .startAt(s)
                    .endAt(s+"\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        userList.add(user);
                    }


                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        private void readUsers() {

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Users").orderByChild("search");


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (search_bar.getText().toString().equals("")) {
                        userList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            userList.add(user);

                        }


                        userAdapter.notifyDataSetChanged();
                        progress_circular.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }