package com.some.studychats;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.Notifications.Data;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;



public class GroupFragment extends Fragment {

    private View PrivateChatsView;
    RecyclerView chatsList;
    private FloatingActionButton fab_Load, fab_status;
    private DatabaseReference ChatsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID = "";
    private String theLastMessage;
    String AES = "AES";
    String outputString;


    private String saveCurrentTime, saveCurrentDate;
    DatabaseReference RootRef;
    private String bio,fullname,sender,reciever,messageId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PrivateChatsView = inflater.inflate(R.layout.fragment_group, container, false);


         RootRef = FirebaseDatabase.getInstance().getReference();




        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Group");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        chatsList = (RecyclerView) PrivateChatsView.findViewById(R.id.chats_list);
        fab_Load = PrivateChatsView.findViewById(R.id.fab_friends);
        fab_status = PrivateChatsView.findViewById(R.id.fab_status);



        fab_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestNewGroup();
            }
        });

        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));


        chatsList.setHasFixedSize(true);


        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
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


        //set data and list adapter

        return PrivateChatsView;
    }


    @Override
    public void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Comment> options =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(ChatsRef, Comment.class)
                        .build();


        FirebaseRecyclerAdapter<Comment, ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Comment, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, final int position, @NonNull Comment model) {
                        final String usersIDs = getRef(position).getKey();
                        int count = 0;


                        final String[] retImage = {"default_image"};

                        holder.userName.setText(model.getGroupName());

                        holder.last_msg.setVisibility(View.VISIBLE);
                        lastMessage(model.getGroupName(), holder.last_msg,holder.password_null,model.getSender());


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getSender());

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(model.getGroupName()+"image")){

                                    String retimage = dataSnapshot.child(model.getGroupName()+"image").getValue().toString();

                                    Picasso.get().load(retimage).into(holder.profileImage);


                                }
                                else{
                                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/instagramtest-fcbef.appspot.com/o/placeholder.png?alt=media&token=b09b809d-a5f8-499b-9563-5252262e9a49").into(holder.profileImage);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getContext(),MassageActivityInstagram.class);
                                intent.putExtra("visit_user_id",model.getGroupName());
                                intent.putExtra("adminId",model.getSender());
                                startActivity(intent);

                            }
                        });

                        }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.user_chat_fragment, viewGroup, false);
                        return new ChatsViewHolder(view);
                    }
                };

        chatsList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView last_msg, userName, user_delete,password_null;
        private TextView userStatus, unseen, sender_unseen;


        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.users_profile_image);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            user_delete = itemView.findViewById(R.id.user_delete);
            unseen = itemView.findViewById(R.id.unseen);
            last_msg = itemView.findViewById(R.id.last_msg);
            password_null = itemView.findViewById(R.id.password_null);


        }


    }

    private void lastMessage(final String userid, final TextView last_msg,final TextView password_null,String adminId){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(userid);


        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(userid) && chat.getType().equals("text")) {
                            theLastMessage = chat.getLastSendMessage();
                        }

                    }
                }

                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        try {
                            outputString = decrypt(theLastMessage,password_null.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        last_msg.setText(outputString);
                        break;

                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String decrypt(String outputString, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodeValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decvalue = c.doFinal(decodeValue);
        String decryptvalue = new String(decvalue);
        return decryptvalue;

    }

    private String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptValue;

    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AEs");
        return secretKeySpec;

    }


    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        builder.setTitle("Please group Name :");
        final EditText groupNameField = new EditText(getContext());
        groupNameField.setHint("e.g Coding cafe");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(getContext(), "Please wirte Group Name...", Toast.LENGTH_SHORT).show();
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



                            HashMap<String,Object> hashMap1 =new HashMap<>();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());


                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                            hashMap1.put("time", saveCurrentTime);
                            hashMap1.put("date", saveCurrentDate);
                            hashMap1.put("fullname",fullname);
                            hashMap1.put("groupName",groupName);
                            hashMap1.put("sender",currentUserID);
                            hashMap1.put("bio",bio);



                            reference.updateChildren(hashMap1);

                            RootRef.child("GroupChatName").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent= new Intent(getContext(),AddFrientUser.class);
                                    intent.putExtra("groupName",groupName);
                                    startActivity(intent);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);


                                }
                            });



                        }
                    }
                });
    }
}
