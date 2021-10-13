package com.some.studychats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.Notifications.Token;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity1 extends AppCompatActivity {
    private View PrivateChatsView;
    RecyclerView chatsList;
    private FloatingActionButton fab_Load, fab_status;
    private DatabaseReference ChatsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID = "";
    private String theLastMessage;
    String AES = "AES";
    String outputString;
    String retImage;

    ChatsAdapter addFriendAdapter;
    ArrayList<Comment> commentList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chats);





        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference(currentUserID);


        chatsList = (RecyclerView) PrivateChatsView.findViewById(R.id.chats_list);
        fab_Load = PrivateChatsView.findViewById(R.id.fab_friends);
        fab_status = PrivateChatsView.findViewById(R.id.fab_status);



        addFriendAdapter = new ChatsAdapter(MainActivity1.this, commentList);
        chatsList.setAdapter(addFriendAdapter);




        fab_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity1.this, FindFriendActivity.class);
                startActivity(intent);


            }
        });

        chatsList.setLayoutManager(new LinearLayoutManager(MainActivity1.this));


        chatsList.setHasFixedSize(true);

        updateToken(FirebaseInstanceId.getInstance().getToken());

        //set data and list adapter

        readUser();


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

    /*

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Comment> options =
                new FirebaseRecyclerOptions.Builder<Comment>()
                        .setQuery(UsersRef, Comment.class)
                        .build();


        FirebaseRecyclerAdapter<Comment, ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Comment, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, final int position, @NonNull Comment model) {

                        final String usersIDs = getRef(position).getKey();
                        int count = 0;

                        Picasso.get().load(model.getImageurl()).into(holder.profileImage);




                        String messageid = model.getMessageID();

                        holder.userName.setText(model.getFullname());


                        holder.last_msg.setVisibility(View.VISIBLE);
                        lastMessage(model.getReceiver(), holder.last_msg,holder.password_null);


                        DatabaseReference Rootref = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver());
                        Rootref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    retImage = dataSnapshot.child("imageurl").getValue().toString();
                                    String bio = dataSnapshot.child("bio").getValue().toString();


                                    Picasso.get().load(retImage).into(holder.profileImage);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(model.getReceiver()).child(currentUserID);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                int unread = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Chat chat = snapshot.getValue(Chat.class);
                                    if (chat.getSender().equals(model.getReceiver()) && !chat.isIsseen()) {
                                        unread++;
                                    }
                                }

                                if (unread == 0) {
                                    holder.unseen.setText("");
                                } else {
                                    holder.unseen.setVisibility(View.VISIBLE);
                                    holder.unseen.setText("" + unread);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), MessageActivity.class);
                                chatIntent.putExtra("userid", model.getReceiver());
                            //    chatIntent.putExtra("messageid",model.getMessageID());
                                startActivity(chatIntent);

                            }

                        });
                        holder.profileImage.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View view) {

                                Intent chatIntent = new Intent(holder.itemView.getContext(), UserImage.class);
                                chatIntent.putExtra("url",  retImage);


                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(holder.profileImage, "imageTransition");

                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), pairs);


                                holder.itemView.getContext().startActivity(chatIntent, options.toBundle());




                            }
                        });

                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                holder.user_delete.setVisibility(View.VISIBLE);
                                return true;
                            }
                        });
                        holder.user_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Contacts").child(currentUserID).child(model.getReceiver());
                                rootRef

                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                            Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(holder.itemView.getContext(), "Error occurred..", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }


                        });




                    }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_chat_fragment, viewGroup, false);
                        return new ChatsViewHolder(view);
                    }
                };

        chatsList.setAdapter(adapter);
        adapter.startListening();
    }
    */


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



    //check for last message
    private void lastMessage(final String userid, final TextView last_msg,final TextView password_null){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(currentUserID).child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
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

    private  void startMainActivity(){
        Intent mainIntent = new Intent(MainActivity1.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(currentUserID).setValue(token1);
    }

}
