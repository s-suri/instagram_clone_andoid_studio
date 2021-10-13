package com.some.studychats.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.some.studychats.AddFrientUser;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
/*
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ContactViewHolder> {

    List<Comment>  comments;
    Context context;

    boolean isClick = false;

    public AddFriendAdapter( Context context,List<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_fragment,parent,false);
        ContactViewHolder viewHolder = new ContactViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        Comment model = comments.get(position);

        holder.userStatus.setVisibility(View.VISIBLE);

        holder.username.setText(model.getFullname());
        holder.userStatus.setText(model.getBio());

        Picasso.get().load(model.getImageurl()).into(holder.profileImage);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!isClick){
                    isClick = true;

                    HashMap<String,Object> hashMap =new HashMap<>();



                    UsersRef.child(usersIDs).updateChildren(hashMap);

                    HashMap<String,Object> hashMap1 =new HashMap<>();

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                    saveCurrentDate = currendateFormat.format(calForDate.getTime());


                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                    hashMap1.put("time", saveCurrentDate);
                    hashMap1.put("date", saveCurrentDate);
                    hashMap1.put("fullname",model.getFullname());
                    hashMap1.put("groupName",group_name);
                    hashMap1.put("messageID", model.getMessageID());
                    hashMap1.put("sender", model.getSender());
                    hashMap1.put("receiver", model.getReceiver());
                    hashMap1.put("bio",model.getBio());
                    hashMap1.put("id","yes");



                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);

                    reference.updateChildren(hashMap1);

                }

                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isClick) {

                    datapresent(model.getReceiver(), holder.checkButtonTrue, usersIDs,model.getFullname(),model.getId(),model.getGroupName(),model.getBio(),model.getMessageID(),holder.checkButtonfalse);
                    Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

                }

                else
                    Toast.makeText(getApplicationContext(), "Details Page", Toast.LENGTH_SHORT).show();


                HashMap<String,Object> hashMap =new HashMap<>();

                hashMap.put("groupName",group_name);

                UsersRef.child(usersIDs).updateChildren(hashMap);

                HashMap<String,Object> hashMap1 =new HashMap<>();

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                saveCurrentDate = currendateFormat.format(calForDate.getTime());


                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                saveCurrentTime = currenTimeFormat.format(calForTime.getTime());

                group_name = intent.getStringExtra("groupName");

                hashMap1.put("time", saveCurrentDate);
                hashMap1.put("date", saveCurrentDate);
                hashMap1.put("fullname",model.getFullname());
                hashMap1.put("groupName",model.getGroupName());
                hashMap1.put("messageID", model.getMessageID());
                hashMap1.put("sender", model.getSender());
                hashMap1.put("receiver", model.getReceiver());
                hashMap1.put("bio",model.getBio());


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);

                reference.updateChildren(hashMap1);





            }
        });

        holder.checkButtonfalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                               if (isClick){

                                    HashMap<String,Object> hashMap1 =new HashMap<>();

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                                    saveCurrentDate = currendateFormat.format(calForDate.getTime());


                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                                    hashMap1.put("time", null);
                                    hashMap1.put("date", null);
                                    hashMap1.put("fullname",null);
                                    hashMap1.put("groupName",null);
                                    hashMap1.put("messageID", null);
                                    hashMap1.put("sender",null);
                                    hashMap1.put("receiver", null);
                                    hashMap1.put("bio",null);
                                    hashMap1.put("id",null);



                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);

                                    reference.updateChildren(hashMap1);

                                }
                                else {


                                    HashMap<String, Object> hashMap1 = new HashMap<>();

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                                    saveCurrentDate = currendateFormat.format(calForDate.getTime());


                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                                    hashMap1.put("time", null);
                                    hashMap1.put("date", null);
                                    hashMap1.put("fullname", null);
                                    hashMap1.put("groupName", null);
                                    hashMap1.put("messageID", null);
                                    hashMap1.put("sender", null);
                                    hashMap1.put("receiver", null);
                                    hashMap1.put("bio", null);
                                    hashMap1.put("id", null);


                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(model.getReceiver()).child("Group").child(group_name);

                                    reference.updateChildren(hashMap1);

                                }



                            }
                        });






    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static  class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView username, userStatus;
        ImageView checkTextAddPeople;

        CircleImageView profileImage;

        TextView checkButtonTrue,checkButtonfalse;

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
*/