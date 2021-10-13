package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.internal.DataCollectionConfigStorage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
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
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFrientUser extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView FindFreindrecyclerList;
    DatabaseReference UsersRef, reference;
    SearchView searchView;
    EditText search_users;
    private Uri fileUri;
    private static final int GalleryPick = 1;
    AddFriendAdapter addFriendAdapter;
    FirebaseAuth mAuth;
    String currentUserID;
    Window window;
    private String saveCurrentTime, saveCurrentDate;
    private TextView editTextGroupName;
    EditText status;
    private TextView txtGroupIcon, txtActionName,save;
    private Intent intent;
    LinearLayout creatGroup;
    String group_name;
    boolean isClick = false;
    private String theLastMessage;
    private ProgressDialog loadingBar;
    private  String cheker="",myUrl="";
    DatabaseReference RootRef;
    CircleImageView imageView;
    StorageTask uploadTask;
    String groupName,adminId;
    StorageReference UserProfileImagesRef;
    ArrayList<Comment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");






        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }

        intent = getIntent();

        group_name = intent.getStringExtra("groupName");
        adminId = intent.getStringExtra("adminId");


        UsersRef = FirebaseDatabase.getInstance().getReference().child(currentUserID);
        FindFreindrecyclerList = (RecyclerView) findViewById(R.id.recycleListFriend);
        FindFreindrecyclerList.setLayoutManager(new LinearLayoutManager(this));

        addFriendAdapter = new AddFriendAdapter(this, commentList);
        FindFreindrecyclerList.setAdapter(addFriendAdapter);


        imageView = findViewById(R.id.users_profile_image);
        loadingBar = new ProgressDialog(this);

        editTextGroupName = (TextView) findViewById(R.id.editGroupName);
        editTextGroupName.setText(group_name);
        txtGroupIcon = (TextView) findViewById(R.id.icon_group);
        creatGroup = findViewById(R.id.btnAddGroup);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map messageTextBody = new HashMap();

                messageTextBody.put(group_name +"status", status.getText().toString());

                RootRef.child("Users").child(currentUserID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()){
                            Toast.makeText(AddFrientUser.this, "Update Successfull", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddFrientUser.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



        status = findViewById(R.id.status);


        readUser();
        RetrieveUserInfo();



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);

            }
        });

    }


    private void readUser() {

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);

                }

                addFriendAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void RetrieveUserInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);


        reference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if ((dataSnapshot.exists()) &&(dataSnapshot.hasChild(group_name +"image") && (dataSnapshot.hasChild(group_name +"status"))))
                        {
                            String retriveimage = dataSnapshot.child(group_name +"image").getValue().toString();
                            String retrivename = dataSnapshot.child("fullname").getValue().toString();
                            String retrieveStatus = dataSnapshot.child(group_name +"status").getValue().toString();

                            status.setText(retrieveStatus);

                            Picasso.get().load(retriveimage).into(imageView);


                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(group_name +"status"))){

                            String retrieveStatus = dataSnapshot.child(group_name +"status").getValue().toString();
                            String retrivename = dataSnapshot.child("fullname").getValue().toString();

                            status.setText(retrieveStatus);


                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(group_name +"image"))){

                            String retriveimage = dataSnapshot.child(group_name +"image").getValue().toString();
                            String retrivename = dataSnapshot.child("fullname").getValue().toString();


                            Picasso.get().load(retriveimage).into(imageView);

                        }
                        else {


                            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/instagramtest-fcbef.appspot.com/o/placeholder.png?alt=media&token=b09b809d-a5f8-499b-9563-5252262e9a49").into(imageView);

                        }



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














    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            loadingBar.setTitle("Set Profile Image");
            loadingBar.setMessage("Please wait, your profile image is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            Picasso.get().load(fileUri).into(imageView);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

                    DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                        .child(currentUserID).child(group_name).push();

                final String messagePushID = userMessageKeyRef.getKey();

            final StorageReference filePath = storageReference.child(messagePushID + "." + "image");

                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Map messageTextBody = new HashMap();
                            messageTextBody.put(group_name +"image", myUrl);
                            messageTextBody.put(group_name +"status", status.getText().toString());



                            RootRef.child("Users").child(currentUserID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        loadingBar.dismiss();
                                        Toast.makeText(AddFrientUser.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(AddFrientUser.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                    loadingBar.dismiss();

                                }
                            });

                        }
                    }
                });
            } else {
            loadingBar.dismiss();

                Toast.makeText(this, "nothing selected, Error.", Toast.LENGTH_SHORT).show();
            }
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
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_fragment, parent, false);
            ContactViewHolder viewHolder = new ContactViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

            Comment model = mChats.get(position);


            String groupName = model.getGroupName();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        holder.checkButtonfalse.setVisibility(View.VISIBLE);
                        holder.checkButtonTrue.setVisibility(View.VISIBLE);

                        holder.userStatus.setVisibility(View.VISIBLE);

                        holder.username.setText(model.getFullname());
                        holder.userStatus.setText(model.getBio());


                        DatabaseReference Rootref = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver());
                        Rootref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    String retImage = dataSnapshot.child("imageurl").getValue().toString();
                                    String bio = dataSnapshot.child("bio").getValue().toString();


                                    Picasso.get().load(retImage).into(holder.profileImage);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }
                    else {
                        holder.checkButtonfalse.setVisibility(View.GONE);
                        holder.checkButtonTrue.setVisibility(View.GONE);

                        holder.userStatus.setVisibility(View.VISIBLE);

                        holder.username.setText(model.getFullname());
                        holder.userStatus.setText(model.getBio());

                        Picasso.get().load(model.getImageurl()).into(holder.profileImage);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)

                {


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Group Users").child(currentUserID).child(group_name);


                    HashMap<String, Object> hashMap1 = new HashMap<>();

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                    saveCurrentDate = currendateFormat.format(calForDate.getTime());


                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                    hashMap1.put("time", saveCurrentDate);
                    hashMap1.put("date", saveCurrentDate);
                    hashMap1.put("fullname", model.getFullname());
                    hashMap1.put("groupName", group_name);

                    hashMap1.put("sender", model.getSender());
                    hashMap1.put("receiver", model.getReceiver());
                    hashMap1.put("bio", model.getBio());
                    hashMap1.put("id", "yes");


                    reference.updateChildren(hashMap1);

                    reference1.child(model.getReceiver()).updateChildren(hashMap1);


                    return true;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Group Users").child(currentUserID).child(group_name);


                    HashMap<String, Object> hashMap1 = new HashMap<>();

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                    saveCurrentDate = currendateFormat.format(calForDate.getTime());


                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());

                    group_name = intent.getStringExtra("groupName");




                    hashMap1.put("time", saveCurrentDate);
                    hashMap1.put("date", saveCurrentDate);
                    hashMap1.put("fullname", model.getFullname());
                    hashMap1.put("groupName", group_name);

                    hashMap1.put("sender", model.getSender());
                    hashMap1.put("receiver", model.getReceiver());
                    hashMap1.put("bio", model.getBio());
                    hashMap1.put("id", "yes");

                    reference.updateChildren(hashMap1);


                    reference1.child(model.getReceiver()).updateChildren(hashMap1);




                }
            });

            holder.checkButtonfalse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Group Users").child(currentUserID).child(group_name);


                    HashMap<String, Object> hashMap1 = new HashMap<>();

                    hashMap1.put("time", null);

                    reference.setValue(hashMap1);
                    reference1.child(model.getReceiver()).setValue(hashMap1);


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
