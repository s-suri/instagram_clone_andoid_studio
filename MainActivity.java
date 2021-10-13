package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.Model.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    ViewPager myViewPager;
    TabLayout myTabLayout;
    Button button;
    TextView search;

    DatabaseReference RootRef;
    private FirebaseUser currentUser;
    FirebaseAuth mAuth;

    private String currentUserID;




    private TabsAccessorAdapter myTabsAccesorAdapter;
    Window window;

    DatabaseReference reference;
    private String saveCurrentTime, saveCurrentDate;

    private String bio,fullname,sender,reciever,messageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        if (Build.VERSION.SDK_INT>=21){
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                state("offline");
                    Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
                  startActivity(intent);
            }
        });

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccesorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccesorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


        RootRef.child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    bio = dataSnapshot.child("bio").getValue().toString();
                    fullname = dataSnapshot.child("fullname").getValue().toString();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Please group Name :");
        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("e.g Coding cafe");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(MainActivity.this, "Please wirte Group Name...", Toast.LENGTH_SHORT).show();
                } else {
                    CreatNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }

    private void CreatNewGroup(final String groupName) {
        String currentUserid = mAuth.getCurrentUser().getUid();

        RootRef.child("Group").child(currentUserid).child("groupName").setValue(groupName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserid).child("Group").child(groupName);


                            HashMap<String, Object> hashMap1 = new HashMap<>();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());


                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                            hashMap1.put("time", saveCurrentTime);
                            hashMap1.put("date", saveCurrentDate);
                            hashMap1.put("fullname", fullname);
                            hashMap1.put("groupName", groupName);
                            hashMap1.put("sender", currentUser.getUid());
                            hashMap1.put("bio", bio);


                            reference.updateChildren(hashMap1);

                            RootRef.child("GroupChatName").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    String currentUserid = mAuth.getCurrentUser().getUid();
                                    Intent intent = new Intent(MainActivity.this, AddFrientUser.class);
                                    intent.putExtra("groupName", groupName);
                                    startActivity(intent);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);


                                }
                            });


                        }
                    }
                });


    }



    @Override
    protected void onStart() {
        super.onStart();

        state("online");
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


}








