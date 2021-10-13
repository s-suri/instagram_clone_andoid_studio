package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupProfileActivity extends AppCompatActivity {
    private  String receiveUserId, Current_atate,SenderUserId;
    private CircleImageView userProfileImage;
    TextView userprofileName,userProfiletatus;
    Button SendMassagerequetButton, deslineMassagerequetButton;

    FirebaseAuth mAuth;
    DatabaseReference UserRef,ChatRequestRef, ContacttoRef, notificationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);


        mAuth = FirebaseAuth.getInstance();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Group");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Group User Chat Request");
        ContacttoRef = FirebaseDatabase.getInstance().getReference().child("Group User Contacts");
        notificationRef = FirebaseDatabase.getInstance().getReference().child("Group User Notifications");


        SenderUserId = mAuth.getCurrentUser().getUid();
        receiveUserId = getIntent().getExtras().get("visit_usr_id").toString();


        Toast.makeText(this, "User Id :"+receiveUserId, Toast.LENGTH_SHORT).show();



        userProfileImage = findViewById(R.id.visit_profile_image);
        userprofileName = findViewById(R.id.visit_user_name);
        userProfiletatus = findViewById(R.id.visit_user_status);
        SendMassagerequetButton = findViewById(R.id.send_massage_request_button);
        deslineMassagerequetButton = findViewById(R.id.decline_massage_request_button);

        Current_atate = "new";


        retreiveInformation();
    }

    private void retreiveInformation() {

        UserRef.child(receiveUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image")))
                {
                    String userimage = dataSnapshot.child("image").getValue().toString();
                    String userName = dataSnapshot.child("groupName").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();

                    Picasso.get().load(userimage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userprofileName.setText(userName);
                    userProfiletatus.setText(userstatus);

                    ManageChatRequet();


                }
                else {

                    ManageChatRequet();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ManageChatRequet() {

        ChatRequestRef.child(SenderUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(receiveUserId))
                        {
                            String request_type = dataSnapshot.child(receiveUserId).child("requestChat").toString();

                            if (request_type.equals("sent"))
                            {
                                Current_atate = "request_sent";
                                SendMassagerequetButton.setText("Cencel Chat Request");
                            }
                            else if (request_type.equals("receive"))
                            {
                                Current_atate = "request_receive";
                                SendMassagerequetButton.setText("Accept Chat request");

                                deslineMassagerequetButton.setEnabled(true);

                                deslineMassagerequetButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelChatRequest();
                                    }
                                });
                            }
                        }
                        else {
                            ContacttoRef.child(SenderUserId)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(receiveUserId))
                                            {
                                                Current_atate = "friend";
                                                SendMassagerequetButton.setText("Remove This Contacts");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (!SenderUserId.equals(receiveUserId)){

            SendMassagerequetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendMassagerequetButton.setEnabled(false);

                    if (Current_atate.equals("new"))
                    {
                        SendChatRequest();
                    }
                    if (Current_atate.equals("request_sent"))
                    {
                        CancelChatRequest();
                    }
                    if (Current_atate.equals("request_recieved"))
                    {
                        AcceptChatRequest();
                    }
                    if (Current_atate.equals("friends"))
                    {
                        RemoveSpecficContacts();
                    }


                }
            });

        }

        else {
            SendMassagerequetButton.setVisibility(View.INVISIBLE);

        }

    }

    private void RemoveSpecficContacts()
    {
        ContacttoRef.child(SenderUserId).child(receiveUserId).child(receiveUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            ContacttoRef .child(receiveUserId).child(SenderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                SendMassagerequetButton.setEnabled(true);
                                                Current_atate = "new";
                                                SendMassagerequetButton.setText("Send Massage");

                                                deslineMassagerequetButton.setVisibility(View.INVISIBLE);
                                                deslineMassagerequetButton.setEnabled(false);

                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void AcceptChatRequest() {
        ContacttoRef.child(SenderUserId).child(receiveUserId)
                .child("Comntacts").setValue("seved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ContacttoRef.child(receiveUserId).child(SenderUserId)
                                    .child("Comntacts").setValue("seved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                ChatRequestRef.child(SenderUserId).child(receiveUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    ChatRequestRef.child(SenderUserId).child(receiveUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {

                                                                                        SendMassagerequetButton.setEnabled(true);
                                                                                        Current_atate = "Friends";
                                                                                        SendMassagerequetButton.setText("Remove this Contacts");

                                                                                        deslineMassagerequetButton.setVisibility(View.INVISIBLE);
                                                                                        deslineMassagerequetButton.setEnabled(false);

                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void CancelChatRequest() {
        ChatRequestRef.child(SenderUserId).child(receiveUserId).child(receiveUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            ChatRequestRef.child(receiveUserId).child(SenderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                SendMassagerequetButton.setEnabled(true);
                                                Current_atate = "new";
                                                SendMassagerequetButton.setText("Send Massage");

                                                deslineMassagerequetButton.setVisibility(View.INVISIBLE);
                                                deslineMassagerequetButton.setEnabled(false);

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void SendChatRequest() {
        ChatRequestRef.child(SenderUserId).child(receiveUserId).child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            ChatRequestRef.child(receiveUserId).child(SenderUserId)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                HashMap<String, String> chatnotificationMAP = new HashMap<>();
                                                chatnotificationMAP.put("from",SenderUserId);
                                                chatnotificationMAP.put("type","request");

                                                notificationRef.child(receiveUserId).push()
                                                        .setValue(chatnotificationMAP)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){

                                                                    SendMassagerequetButton.setEnabled(true);
                                                                    Current_atate = "request_sent";
                                                                    SendMassagerequetButton.setText("Cancel Chat Request");
                                                                }
                                                            }
                                                        });


                                            }
                                        }
                                    });
                            {

                            }
                        }
                    }
                });

    }
}
