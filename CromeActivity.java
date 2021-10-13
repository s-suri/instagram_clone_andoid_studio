package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.ModelInstagram.Comment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CromeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView FindFreindrecyclerList;
    DatabaseReference UsersRef;
    SearchView searchView;
    EditText search_users;
    FirebaseAuth mAuth;
    String currentUserID;
    Window window;

    private String saveCurrentTime, saveCurrentDate,userType;

    DatabaseReference reference,uploaderUserInformation;

    ImageView dataupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crome);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        if (Build.VERSION.SDK_INT>=21){
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }

        dataupload = findViewById(R.id.dataUpload);


        uploaderUserInformation = FirebaseDatabase.getInstance().getReference("Users");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Weburls");
        FindFreindrecyclerList = (RecyclerView)findViewById(R.id.chats_list);
        FindFreindrecyclerList.setLayoutManager(new LinearLayoutManager(this));


        uploaderUserInformation.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("type")){
                    userType = dataSnapshot.child("type").getValue().toString();
                    dataupload.setVisibility(View.VISIBLE);

                }
                else {

                    dataupload.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dataupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userType.equals("pdf_uploader")){
                    Intent intent = new Intent(CromeActivity.this, PdfPostUpload.class);
                    startActivity(intent);
                }
                else if (userType.equals("url_uploader")){
                    Intent intent = new Intent(CromeActivity.this,UrlUploader.class);
                    startActivity(intent);
                }
                else if (userType.equals("post_uploader")){
                    Intent intent = new Intent(CromeActivity.this,PdfPostUpload.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(CromeActivity.this, "Sorry...", Toast.LENGTH_SHORT).show();

                }

            }
        });


        search_users = findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                firebaseSearch(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void firebaseSearch(String newText) {

        String query = newText.toLowerCase();

        Query ChatsRef = UsersRef.orderByChild("search").startAt(newText).endAt(newText + "\uf8ff");

        FirebaseRecyclerOptions<WebItems> options=
                new FirebaseRecyclerOptions.Builder<WebItems>()
                        .setQuery(ChatsRef, WebItems.class).build();


        final FirebaseRecyclerAdapter<WebItems, ContactViewHolder> adapter =
                new FirebaseRecyclerAdapter<WebItems, ContactViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position, @NonNull final WebItems model) {


                            holder.web_title.setText(model.getTitle());
                            holder.web_des.setText(model.getDescription());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                                if (model.getType().equals("web_pdf")){
                                  Intent intent = new Intent(CromeActivity.this, ViewPdfPosts.class);
                                  intent.putExtra("university_name",model.getUniversityName());
                                  startActivity(intent);
                                }

                                else if(model.getType().equals("web_url")){
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(model.getUrl()));
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_open_website,viewGroup,false);
                        ContactViewHolder viewHolder = new ContactViewHolder(view);
                        return viewHolder;
                    }
                };
        FindFreindrecyclerList.setAdapter(adapter);
        adapter.startListening();
    }


    /*
    @Override
    protected void onStart() {
        super.onStart();




        FirebaseRecyclerOptions<WebItems> options=
                new FirebaseRecyclerOptions.Builder<WebItems>()
                        .setQuery(UsersRef, WebItems.class).build();


        final FirebaseRecyclerAdapter<WebItems, ContactViewHolder>  adapter =
                new FirebaseRecyclerAdapter<WebItems, ContactViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position, @NonNull final WebItems model) {

                        holder.web_title.setText(model.getTitle());
                        holder.web_des.setText(model.getDescription());



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(model.getUrl()));
                                startActivity(intent);    }
                        });



                    }

                    @NonNull
                    @Override
                    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_open_website,viewGroup,false);
                        ContactViewHolder viewHolder = new ContactViewHolder(view);
                        return viewHolder;
                    }
                };
        FindFreindrecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

     */



    public static  class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView web_title, web_des;

        CircleImageView profileImage;


        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);

            web_title = itemView.findViewById(R.id.web_title);
            web_des = itemView.findViewById(R.id.web_description);



        }
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



}
