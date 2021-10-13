package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.some.studychats.Adapter.MyAdapterMovie;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.ModelInstagram.User;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    Button getText_btn;
    ImageView showImage_img;
    TextView showText_txt;
    static final int REQUEST_IMAGE_CAMERA = 1;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    String AES = "AES";

    String postid,adminId;

    String msg,trans_msg;

    FirebaseAuth mAuth;


    DatabaseReference RootRef;
    private TextView userLastSeen;

    private Uri fileUri;
    private ProgressDialog loadingBar;

    private List<String> fileNameList;
    private List<String> fileDoneList;

    ProgressBar splashProgress;

    private String imageurl;

    Dialog myDialog;

    CircleImageView profile_image;
    TextView username,messageConvert;

    private String cheker = "", myUrl = "", url = "";
    FirebaseUser fuser;
    DatabaseReference reference;
    private static final int RESULT_LOAD_IMAGE = 1;

    private StorageTask uploadTask;
    ImageButton btn_send, SendFilesButton;
    EditText text_send;

    MyAdapterMovie messageAdapter;
    List<Comment> mchat;


    RecyclerView recyclerView, findFriendLis;

    Intent intent;

    private String saveCurrentTime, saveCurrentDate;

    ValueEventListener seenListener;

    RelativeLayout bottom;
    String messageId;;

    boolean notify = false;

    TextView inputPassword,fab_status;
    Window window;
    String str_name;

    String imageReciever,bioReciever,fullnameReciever,imageSender,bioSender,fullnameSender,theLastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage_instagram);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        myDialog = new Dialog(this);
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        //This is additional feature, used to run a progress bar
        splashProgress = findViewById(R.id.splashProgress);


        //Method to run progress bar for 5 seconds


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        splashProgress = findViewById(R.id.splashProgress);

        mAuth = FirebaseAuth.getInstance();

        //Method to run progress bar for 5 seconds

        bottom = findViewById(R.id.bottom);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setEnabled(false);
        inputPassword = findViewById(R.id.password);
        text_send = findViewById(R.id.text_send);

        findFriendLis = findViewById(R.id.search_translate);



        LinearLayoutManager linearLayoutTranslate = new LinearLayoutManager(getApplicationContext());
        linearLayoutTranslate.setStackFromEnd(true);
        findFriendLis.setLayoutManager(linearLayoutTranslate);


        getText_btn = findViewById(R.id.btn_gettext);
        showText_txt = findViewById(R.id.txt_show_text);
        showImage_img = findViewById(R.id.img_imageview);
        messageConvert = findViewById(R.id.message_convert);
        //Set OnClick event for getImage_btn Button to take image from camera

        //Set OnClick event for getText_btn Button to get Text from image

        getText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //         GetTextFromImageFunction();
            }
        });

        fab_status = findViewById(R.id.fab_status);
        fab_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(mchat.size());

                fab_status.setVisibility(View.GONE);
            }
        });




        loadingBar = new ProgressDialog(this);
        userLastSeen = (TextView) findViewById(R.id.custom_user_last_seen);
        RootRef = FirebaseDatabase.getInstance().getReference();

        intent = getIntent();
        postid = intent.getStringExtra("visit_user_id");
        adminId = intent.getStringExtra("adminId");




        fuser = FirebaseAuth.getInstance().getCurrentUser();

        SendFilesButton = (ImageButton) findViewById(R.id.send_files_btn);
        SendFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtclose;
                LinearLayout image, images, video, videos, pdf, pdfs, ms_file, ms_files;

                Button btnFollow;
                myDialog.setContentView(R.layout.custompopup);
                txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
                image = (LinearLayout) myDialog.findViewById(R.id.image);
                images = (LinearLayout) myDialog.findViewById(R.id.images);
                video = (LinearLayout) myDialog.findViewById(R.id.video);
                videos = (LinearLayout) myDialog.findViewById(R.id.videos);
                pdf = (LinearLayout) myDialog.findViewById(R.id.pdf);
                pdfs = (LinearLayout) myDialog.findViewById(R.id.pdfs);
                ms_file = (LinearLayout) myDialog.findViewById(R.id.ms_word);
                ms_files = (LinearLayout) myDialog.findViewById(R.id.ms_words);
                txtclose.setText("M");
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "image";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select Image"), 438);
                    }
                });

                images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "image";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_LOAD_IMAGE);

                    }
                });

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "video";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), 438);

                    }
                });

                videos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "video";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), RESULT_LOAD_IMAGE);

                    }
                });

                pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "PDF";

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        startActivityForResult(intent.createChooser(intent, "Select PDF"), 438);


                    }
                });

                pdfs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();

                        cheker = "PDF";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(intent.createChooser(intent, "Select PDF"), RESULT_LOAD_IMAGE);

                    }
                });


                ms_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "docx";

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/msword");
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), 438);

                    }
                });


                ms_files.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "docx";

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/msword");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), RESULT_LOAD_IMAGE);

                    }
                });


                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });



        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,GroupInfo.class);
                intent.putExtra("groupName",postid);
                intent.putExtra("adminId",adminId);
                startActivity(intent);
            }
        });



        text_send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {

                    btn_send.setEnabled(false);
                    findFriendLis.setVisibility(View.GONE);

                } else {
                    btn_send.setEnabled(true);
                    findFriendLis.setVisibility(View.VISIBLE);
                    typingStatus(fuser.getUid());
                    firebaseSearch(s.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;

                try {
                    msg = encrypt(text_send.getText().toString(), inputPassword.getText().toString());
                    trans_msg = encrypt(messageConvert.getText().toString(), inputPassword.getText().toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!msg.equals("")) {


                    addCommentSender(msg,trans_msg);

                } else {
                    Toast.makeText(LoginActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findFriendLis.setVisibility(View.GONE);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(adminId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("imageurl")) {
                        imageReciever = dataSnapshot.child("imageurl").getValue().toString();
                        bioReciever = dataSnapshot.child("bio").getValue().toString();
                        fullnameReciever = dataSnapshot.child("fullname").getValue().toString();




                        readMesagges();


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RerieveUserInfo();


        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(postid);
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("imageurl")) {
                        imageSender = dataSnapshot.child("imageurl").getValue().toString();
                        bioSender = dataSnapshot.child("bio").getValue().toString();
                        fullnameSender = dataSnapshot.child("fullname").getValue().toString();

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    str_name = dataSnapshot.child("fullname").getValue().toString();
                }
                else
                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void addCommentSender(String msg,String trans_msg) {


        RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);

        final String messagePushID = RootRef.push().getKey();


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
        saveCurrentDate = currendateFormat.format(calForDate.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currenTimeFormat.format(calForTime.getTime());



        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", saveCurrentTime);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("messageID", messagePushID);
        hashMap.put("sender", fuser.getUid());
        hashMap.put("receiver", postid);
        hashMap.put("message", msg);
        hashMap.put("bio", trans_msg);
        hashMap.put("type", "text");
        hashMap.put("isseen", false);
        hashMap.put("username",str_name);
        hashMap.put(fuser.getUid(),"hi");


        RootRef.child(messagePushID).setValue(hashMap);

        findFriendLis.setVisibility(View.GONE);
        text_send.setText("");
        messageConvert.setText("");

    }






    private void readMesagges() {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment chat = snapshot.getValue(Comment.class);

                    mchat.add(chat);



                    messageAdapter = new MyAdapterMovie(LoginActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void state(String online) {

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("userState");


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


    private void typingStatus(String typing) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typingTo", typing);

        reference.updateChildren(hashMap);
    }

    private void RerieveUserInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(adminId);


        reference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if ((dataSnapshot.exists()) &&(dataSnapshot.hasChild(postid +"image") && (dataSnapshot.hasChild(postid +"status"))))
                        {
                            String retriveimage = dataSnapshot.child(postid +"image").getValue().toString();
                            String retrieveStatus = dataSnapshot.child(postid +"status").getValue().toString();


                            userLastSeen.setVisibility(View.VISIBLE);

                            userLastSeen.setText(retrieveStatus);




                            Picasso.get().load(retriveimage).into(profile_image);


                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(postid +"status"))){

                            String retrieveStatus = dataSnapshot.child(postid +"status").getValue().toString();


                            userLastSeen.setVisibility(View.VISIBLE);
                            userLastSeen.setText(retrieveStatus);




                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(postid +"image"))){

                            String retriveimage = dataSnapshot.child(postid +"image").getValue().toString();


                            Picasso.get().load(retriveimage).into(profile_image);


                        }
                        else {


                            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/instagramtest-fcbef.appspot.com/o/placeholder.png?alt=media&token=b09b809d-a5f8-499b-9563-5252262e9a49").into(profile_image);

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    if (!cheker.equals("image")) {

                        RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);

                        final String messagePushID = RootRef.push().getKey();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");


                        final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");
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

                                    Toast.makeText(LoginActivity.this, "upload successfully", Toast.LENGTH_SHORT).show();
                                    Uri downloadUrl = task.getResult();
                                    myUrl = downloadUrl.toString();

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                                    saveCurrentDate = currendateFormat.format(calForDate.getTime());


                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                                    HashMap<String, Object> groupMessageKey = new HashMap<>();
                                    RootRef.updateChildren(groupMessageKey);


                                    Map messageTextBody = new HashMap();


                                    messageTextBody.put("message", myUrl);
                                    messageTextBody.put("username", str_name);
                                    messageTextBody.put("name", fileUri.getLastPathSegment());
                                    messageTextBody.put("type", cheker);
                                    messageTextBody.put("sender", fuser.getUid());
                                    messageTextBody.put("receiver", postid);
                                    messageTextBody.put("messageID", messagePushID);
                                    messageTextBody.put("time", saveCurrentTime);
                                    messageTextBody.put("date", saveCurrentDate);
                                    messageTextBody.put("isseen", false);



                                    RootRef.child(messagePushID).updateChildren(messageTextBody);


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (cheker.equals("image")) {
                        RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");



                        final String messagePushID = RootRef.push().getKey();

                        final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");

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

                                    Uri downloadUrl = task.getResult();
                                    myUrl = downloadUrl.toString();

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                                    saveCurrentDate = currendateFormat.format(calForDate.getTime());

                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                                    Map messageTextBody = new HashMap();
                                    messageTextBody.put("message", myUrl);
                                    messageTextBody.put("username", str_name);
                                    messageTextBody.put("name", fileUri.getLastPathSegment());
                                    messageTextBody.put("type", cheker);
                                    messageTextBody.put("sender", fuser.getUid());
                                    messageTextBody.put("receiver", postid);
                                    messageTextBody.put("messageID", messagePushID);
                                    messageTextBody.put("time", saveCurrentTime);
                                    messageTextBody.put("date", saveCurrentDate);



                                    RootRef.child(messagePushID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(LoginActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                            } else {

                                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });

                                }
                            }
                        });
                    }

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else

                Toast.makeText(this, "Please Select Multiple Items", Toast.LENGTH_SHORT).show();


        }


        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                }
                break;
        }

        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();


            if (!cheker.equals("image")) {


                RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messagePushID = RootRef.push().getKey();

                final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");
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

                            Toast.makeText(LoginActivity.this, "upload successfully", Toast.LENGTH_SHORT).show();
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());


                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());




                            Map messageTextBody = new HashMap();


                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("username", str_name);
                            messageTextBody.put("name", fileUri.getLastPathSegment());
                            messageTextBody.put("type", cheker);
                            messageTextBody.put("sender", fuser.getUid());
                            messageTextBody.put("receiver", postid);
                            messageTextBody.put("messageID", messagePushID);
                            messageTextBody.put("time", saveCurrentTime);
                            messageTextBody.put("date", saveCurrentDate);
                            messageTextBody.put("isseen", false);



                            RootRef.child(messagePushID).updateChildren(messageTextBody);


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (cheker.equals("image")) {
                RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");




                final String messagePushID = RootRef.push().getKey();

                final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");

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

                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());

                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                            Map messageTextBody = new HashMap();
                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("username", str_name);
                            messageTextBody.put("name", fileUri.getLastPathSegment());
                            messageTextBody.put("type", cheker);
                            messageTextBody.put("sender", fuser.getUid());
                            messageTextBody.put("receiver", postid);
                            messageTextBody.put("messageID", messagePushID);
                            messageTextBody.put("time", saveCurrentTime);
                            messageTextBody.put("date", saveCurrentDate);


                            RootRef.child(messagePushID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(LoginActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                        }
                    }
                });
            } else {

                Toast.makeText(this, "nothing selected, Error.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    private void GetTextFromImageFunction() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(this, "Error occur", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            //Show the text to TextView
            text_send.setText(stringBuilder.toString());
            //Thats All
        }

    }

     */


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

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();


    }

    @Override
    protected void onStart() {
        super.onStart();
        state("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        typingStatus("noOne");
        state("offline");
        findFriendLis.setVisibility(View.GONE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        typingStatus("noOne");
        state("offline");
        findFriendLis.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        typingStatus("noOne");
        findFriendLis.setVisibility(View.GONE);

    }



    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView username, userStatus,textConvert;

        CircleImageView profileImage;


        public SearchViewHolder(@NonNull View itemView) {

            super(itemView);

            username = itemView.findViewById(R.id.transText);
            textConvert = itemView.findViewById(R.id.convet_text);


        }

    }



    private void firebaseSearch(String newText) {

        String query = newText.toLowerCase();

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference("TransSearch");

        Query ChatsRef = UsersRef.orderByChild("fullname").startAt(newText).endAt(newText + "\uf8ff");


        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(ChatsRef, User.class).build();


        final FirebaseRecyclerAdapter<User, SearchViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, SearchViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SearchViewHolder holder, final int position, @NonNull final User model) {

                        holder.username.setText(model.getFullname());
                        holder.textConvert.setText(model.getBio());


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                text_send.setText(model.getFullname());
                                messageConvert.setText(model.getBio());

                                findFriendLis.setVisibility(View.GONE);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trans_search, viewGroup, false);
                        SearchViewHolder viewHolder = new SearchViewHolder(view);
                        return viewHolder;
                    }
                };
        findFriendLis.setAdapter(adapter);
        adapter.startListening();

    }

    private void lastMessage(final String userid) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(fuser.getUid()).child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessageID();
                        }
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        HashMap<String,Object> hashMap1 = new HashMap<>();

                        hashMap1.put("date",null);
                        break;

                    default:
                        username.setText(theLastMessage);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(fuser.getUid());
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference(userid);


                        HashMap<String,Object> hashMap = new HashMap<>();

                        hashMap.put("date",null);
                        reference.child(theLastMessage).setValue(hashMap);
                        reference1.child(theLastMessage).setValue(hashMap);



                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}