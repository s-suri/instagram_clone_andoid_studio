package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.some.studychats.ModelInstagram.Comment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupInfo extends AppCompatActivity {
    private Button UpdateAccoutSetting;
    private TextView edit,userStatus,name,delete_group,adminInformation;
    private ImageView userprofileImage;
    private String currentUserid;
    private FirebaseAuth mAuth;
    DatabaseReference RootRef;
    private static  final int GalleryPic = 1;
    private StorageReference UserprofileImageref;
    StorageTask uUploadTask;

    ArrayList<Comment> mChats = new ArrayList<>();

    AddFriendAdapter addFriendAdapter;


    DatabaseReference reference;
    private String saveCurrentTime, saveCurrentDate;

    Window window;

    Intent intent;
    String groupName,adminId;
    String imageUri;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);



        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserprofileImageref = FirebaseStorage.getInstance().getReference().child("Profile Images");


        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        addFriendAdapter = new AddFriendAdapter(this,mChats);
        recyclerView.setAdapter(addFriendAdapter);


        groupName = getIntent().getStringExtra("groupName");
        adminId = getIntent().getStringExtra("adminId");

        InitializeField();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminId.equals(currentUserid)){
                    Intent intent = new Intent(GroupInfo.this,AddFrientUser.class);
                    intent.putExtra("groupName",groupName);
                    intent.putExtra("adminId",adminId);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(GroupInfo.this, "Sorry' you are not admin", Toast.LENGTH_SHORT).show();
                }
            }
        });


        delete_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(GroupInfo.this).create();
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


                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserid).child("Group").child(groupName);

                               reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(GroupInfo.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                                     SenDUserToMainActivity();
                                            }
                                        }
                                    });

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();





            }
        });






        RerieveUserInfo();
        readUser();


    }

    private void RerieveUserInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(adminId);


       reference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if ((dataSnapshot.exists()) &&(dataSnapshot.hasChild(groupName +"image") && (dataSnapshot.hasChild(groupName +"status"))))
                        {
                            String retriveimage = dataSnapshot.child(groupName +"image").getValue().toString();
                            String retrivename = dataSnapshot.child("fullname").getValue().toString();
                            String retrieveStatus = dataSnapshot.child(groupName +"status").getValue().toString();

                            userStatus.setText(retrieveStatus);
                            adminInformation.setText("Created By :  "+"  " +retrivename);

                            Picasso.get().load(retriveimage).into(userprofileImage);


                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(groupName +"status"))){

                            String retrieveStatus = dataSnapshot.child(groupName +"status").getValue().toString();
                            String retrivename = dataSnapshot.child("fullname").getValue().toString();

                            userStatus.setText(retrieveStatus);
                            adminInformation.setText("Created By :  "+"  " +retrivename);




                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(groupName +"image"))){

                            String retriveimage = dataSnapshot.child(groupName +"image").getValue().toString();
                            String retrivename = dataSnapshot.child("fullname").getValue().toString();

                            adminInformation.setText("Created By :  " +"  "+retrivename);

                            Picasso.get().load(retriveimage).into(userprofileImage);

                        }
                        else {


                            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/instagramtest-fcbef.appspot.com/o/placeholder.png?alt=media&token=b09b809d-a5f8-499b-9563-5252262e9a49").into(userprofileImage);

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void InitializeField() {

        name = findViewById(R.id.name);
        name.setText(groupName);

        adminInformation = findViewById(R.id.adminInformetion);


        edit = findViewById(R.id.editGroup);
        delete_group = findViewById(R.id.delete_group);


        userStatus = findViewById(R.id.description);
        userprofileImage = findViewById(R.id.set_profile_image);

    }


        private void readUser() {

            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Group Users").child(currentUserid).child(groupName);

            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mChats.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Comment comment = snapshot.getValue(Comment.class);
                        mChats.add(comment);

                    }

                    addFriendAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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

    }



    private void state(String online) {

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserid).child("userState");


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




    private void SenDUserToMainActivity() {
        Intent intent = new Intent(GroupInfo.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ContactViewHolder> {

        List<Comment> mChats;
        Context context;
        String messageId;

        String group;

        boolean isClick = false;

        public AddFriendAdapter(Context context, List<Comment> mChats) {
            this.mChats = mChats;
            this.context = context;
        }

        @NonNull
        @Override
        public AddFriendAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_users_items, parent, false);
            AddFriendAdapter.ContactViewHolder viewHolder = new AddFriendAdapter.ContactViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull AddFriendAdapter.ContactViewHolder holder, int position) {

            Comment model = mChats.get(position);


            String groupName = model.getGroupName();


            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Group Users").child(currentUserid).child(groupName);

            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        holder.userStatus.setVisibility(View.VISIBLE);
                        holder.username.setText(model.getFullname());
                        holder.userStatus.setText(model.getBio());



                    }
                    else {
                        Toast.makeText(context, "No User Yet", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }








    @Override
        public int getItemCount() {
            return mChats.size();
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder {

            TextView username, userStatus;
            ImageView checkTextAddPeople;

            CircleImageView profileImage;

            TextView checkButtonTrue, checkButtonfalse;

            public ContactViewHolder(@NonNull View itemView) {

                super(itemView);

                username = itemView.findViewById(R.id.user_profile_name);
                userStatus = itemView.findViewById(R.id.user_status);
                profileImage = itemView.findViewById(R.id.users_profile_image);
                checkButtonTrue = itemView.findViewById(R.id.checkAddPeopleTrue);
                checkButtonfalse = itemView.findViewById(R.id.checkAddPeopleFalse);
                checkTextAddPeople = itemView.findViewById(R.id.checkTextAddPeople);


            }
        }
    }


}
