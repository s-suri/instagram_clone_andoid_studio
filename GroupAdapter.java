package com.some.studychats;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.AdapterInstagram.MyFotosAdapter;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ImageViewHolder> {

    List<Comment> mChats;
    Context context;
    private LayoutInflater inflater;
    private String theLastMessage;
    String AES = "AES";
    String outputString;



    public GroupAdapter(Context context, List<Comment> mChats) {
        this.mChats = mChats;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public GroupAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_chat_fragment, parent, false);
        return new ImageViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ImageViewHolder holder, int position) {


        Comment model = mChats.get(position);
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

                Intent intent = new Intent(holder.itemView.getContext(),MassageActivityInstagram.class);
                intent.putExtra("visit_user_id",model.getGroupName());
                intent.putExtra("adminId",model.getSender());
                holder.itemView.getContext().startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        TextView last_msg, userName, user_delete,password_null;
        private TextView userStatus, unseen, sender_unseen;



        public ImageViewHolder(@NonNull View itemView) {
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

}
