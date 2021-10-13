package com.some.studychats.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.AdapterInstagram.CommentAdapter;
import com.some.studychats.CommentsActivityInstagram;
import com.some.studychats.ImageViewActivity;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.ModelInstagram.User;
import com.some.studychats.R;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MessageAdapterInstagram extends RecyclerView.Adapter<MessageAdapterInstagram.ImageViewHolder> {

    private Context mContext;
    private List<Comment> mComment;
    private String postid;
    String outputString;

    String AES = "AES";

    private FirebaseUser firebaseUser;

    public MessageAdapterInstagram(Context context, List<Comment> comments, String postid) {
        mContext = context;
        mComment = comments;
        this.postid = postid;
    }

    public MessageAdapterInstagram(CommentsActivityInstagram context, List<Comment> commentList, String postid) {
        mContext = context;
        mComment = commentList;
        this.postid = postid;
    }

    @NonNull
    @Override
    public MessageAdapterInstagram.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item_instagram, parent, false);
        return new MessageAdapterInstagram.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapterInstagram.ImageViewHolder holder, final int position) {

        final Comment chat = mComment.get(position);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String messageSenderId = mAuth.getCurrentUser().getUid();
        String fromMessageType = chat.getType();
        String fromUserID = chat.getComment();



        if (position == mComment.size() - 1) {
            if (chat.isIsseen()) {

                holder.txt_seen_sender.setText("Seen");

            }
            //Method to run progress bar for 5 seconds
            else {
                holder.txt_seen_sender.setText("Delivered");
            }
        } else {
            holder.txt_seen_sender.setVisibility(View.GONE);
        }


        holder.linear_text_sender.setVisibility(View.GONE);
        holder.relativeText.setVisibility(View.GONE);
        holder.linear_text.setVisibility(View.GONE);
        holder.linear_image.setVisibility(View.GONE);
        holder.linear_image_sender.setVisibility(View.GONE);
        holder.linear_video.setVisibility(View.GONE);
        holder.show_video_sender.setVisibility(View.GONE);
        holder.show_video.setVisibility(View.GONE);
        holder.linear_video_sender.setVisibility(View.GONE);
        holder.timeDateVideo_sender.setVisibility(View.GONE);
        holder.timeDateVideo.setVisibility(View.GONE);
        holder.relativePdf.setVisibility(View.GONE);
        holder.relativePdf_sender.setVisibility(View.GONE);
        holder.linearpdf_sender.setVisibility(View.GONE);
        holder.linearpdf.setVisibility(View.GONE);

        holder.play_sender.setVisibility(View.GONE);
        holder.pause_sender.setVisibility(View.GONE);
        holder.pause.setVisibility(View.GONE);
        holder.play.setVisibility(View.GONE);

        holder.password_sender.setVisibility(View.GONE);
        holder.password_ok_sender.setVisibility(View.GONE);

        holder.password.setVisibility(View.GONE);
        holder.password_ok.setVisibility(View.GONE);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComment.get(position);



        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playvideo(position,holder);

            }
        });
        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausevideo(position,holder);

            }
        });

        holder.linear_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.play.setVisibility(View.GONE);
                holder.pause.setVisibility(View.VISIBLE);

            }
        });

        holder.linear_video.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.delete_one_message.setVisibility(View.VISIBLE);
                holder.delete_on_linear.setVisibility(View.VISIBLE);



                return true;
            }
        });

        holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSentMessage(position,holder);
            }
        });


        holder.play_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playvideoSender(position,holder);

            }
        });
        holder.pause_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausevideoSender(position,holder);

            }
        });

        holder.linear_video_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.play_sender.setVisibility(View.GONE);
                holder.pause_sender.setVisibility(View.VISIBLE);

            }
        });

        holder.linear_video_sender.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.delete_one_message.setVisibility(View.VISIBLE);
                holder.delete_on_linear.setVisibility(View.VISIBLE);



                return true;
            }
        });

        holder.delete_one_message_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSentMessage(position,holder);
            }
        });



        if (fromMessageType.equals("text")) {
            if (messageSenderId.equals(comment.getSender())) {
                holder.linear_text_sender.setVisibility(View.VISIBLE);
                holder.relativeText_sender.setVisibility(View.VISIBLE);
                holder.linear_text_sender.setVisibility(View.VISIBLE);
                holder.text_time_date_sender.setText(comment.getDate() +"  -  "+ comment.getTime());


                holder.text_show_password_sender.setVisibility(View.VISIBLE);


                try {
                    outputString = decrypt(chat.getMessage(), holder.password_null_sender.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.text_show_password_sender.setText(outputString);




                holder.linear_text_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.linear_text_sender.setVisibility(View.VISIBLE);


                        holder.password_sender.setVisibility(View.VISIBLE);
                        holder.password_ok_sender.setVisibility(View.VISIBLE);


                    }
                });

                holder.password_ok_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            outputString = decrypt(chat.getMessage(), holder.password_sender.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        holder.text_show_password_sender.setText(outputString);
                    }
                });



            } else {

                holder.linear_text.setVisibility(View.VISIBLE);
                holder.relativeText.setVisibility(View.VISIBLE);
                holder.linear_text.setVisibility(View.VISIBLE);
                holder.text_time_date.setText(comment.getDate() +"  -  "+ comment.getTime());
                holder.text_show_password.setVisibility(View.VISIBLE);



                try {
                    outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.text_show_password.setText(outputString);


                holder.linear_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.linear_text.setVisibility(View.VISIBLE);

                        holder.password.setVisibility(View.VISIBLE);
                        holder.password_ok.setVisibility(View.VISIBLE);


                    }
                });

                holder.password_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            outputString = decrypt(chat.getMessage(), holder.password.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        holder.text_show_password.setText(outputString);
                    }
                });


            }
        }
        else if (fromMessageType.equals("image")) {
            if (messageSenderId.equals(comment.getSender())) {
                holder.show_Image_sender.setVisibility(View.VISIBLE);
                holder.linear_image_sender.setVisibility(View.VISIBLE);

                holder.timeDateImage_sender.setVisibility(View.VISIBLE);
                holder.timeDateImage_sender.setText(chat.getDate() + "  -  " + chat.getTime());
                Picasso.get().load(chat.getMessage()).into(holder.show_Image_sender);


                holder.show_Image_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), ImageViewActivity.class);
                        intent.putExtra("url", mComment.get(position).getMessage());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });



            } else {

                holder.show_Image.setVisibility(View.VISIBLE);
                holder.linear_image.setVisibility(View.VISIBLE);

                holder.timeDateImage.setVisibility(View.VISIBLE);
                holder.timeDateImage.setText(chat.getDate() + "  -  " + chat.getTime());
                Picasso.get().load(chat.getMessage()).into(holder.show_Image);

                holder.show_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), ImageViewActivity.class);
                        intent.putExtra("url", mComment.get(position).getMessage());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });

            }
        }
        else if (fromMessageType.equals("PDF") || (fromMessageType.equals("docx"))) {
            if (messageSenderId.equals(comment.getSender())) {
                holder.relativePdf_sender.setVisibility(View.VISIBLE);
                holder.linearpdf_sender.setVisibility(View.VISIBLE);

                holder.linearpdf_sender.setVisibility(View.VISIBLE);
                holder.pdfName_sender.setText(chat.getName());

                holder.pdfTimeDate_sender.setVisibility(View.VISIBLE);
                holder.pdfTimeDate_sender.setText(chat.getDate() + "  -  " + chat.getTime());
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6").into(holder.show_pdf_sender);

                holder.show_pdf_sender.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        holder.delete_one_message_sender.setVisibility(View.VISIBLE);
                        holder.delete_one_linear_sender.setVisibility(View.VISIBLE);
                        holder.download_pdf_sender.setVisibility(View.VISIBLE);

                        return true;
                    }
                });

                holder.delete_one_message_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMessageForEveryone(position,holder);
                    }
                });

                holder.download_pdf_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mComment.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });

            }

            else {

                holder.relativePdf.setVisibility(View.VISIBLE);
                holder.linearpdf.setVisibility(View.VISIBLE);

                holder.linearpdf.setVisibility(View.VISIBLE);
                holder.pdfName.setText(chat.getName());

                holder.pdfTimeDate.setVisibility(View.VISIBLE);
                holder.pdfTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6").into(holder.show_pdf);

                holder.show_pdf.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        holder.delete_one_message.setVisibility(View.VISIBLE);
                        holder.delete_on_linear.setVisibility(View.VISIBLE);
                        holder.download_pdf.setVisibility(View.VISIBLE);

                        return true;
                    }
                });

                holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMessageForEveryone(position,holder);
                    }
                });

                holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mComment.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });

            }
        }
        else if (fromMessageType.equals("video")) {
            if (messageSenderId.equals(comment.getSender())) {

                holder.linear_video_sender.setVisibility(View.VISIBLE);
                holder.show_video_sender.setVisibility(View.VISIBLE);

                holder.show_video_sender.setVideoPath(chat.getMessage());
                holder.timeDateVideo_sender.setVisibility(View.VISIBLE);
                holder.timeDateVideo_sender.setText(chat.getDate() + "  -  " + chat.getTime());

                holder.show_video_sender.requestFocus();
                holder.show_video_sender.seekTo(1);


                holder.play_sender.setVisibility(View.VISIBLE);


                holder.delete_one_message_sender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteReceiveMessage(position,holder);
                    }
                });


            }else {
                holder.linear_video.setVisibility(View.VISIBLE);
                holder.show_video.setVisibility(View.VISIBLE);

                holder.show_video.setVideoPath(chat.getMessage());
                holder.timeDateVideo_sender.setVisibility(View.VISIBLE);
                holder.timeDateVideo_sender.setText(chat.getDate() + "  -  " + chat.getTime());

                holder.show_video.requestFocus();
                holder.show_video.seekTo(1);


                holder.play.setVisibility(View.VISIBLE);


                holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteReceiveMessage(position,holder);
                    }
                });


            }
        }

        else {
            Toast.makeText(mContext, "hello", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile,show_Image,show_Image_sender,show_pdf,show_pdf_sender;
        public TextView username, comment,comment_sender,timeDateImage,timeDateImage_sender,timeDateVideo,timeDateVideo_sender,play,pause,play_sender,pause_sender;
      public  TextView text_show_password,pdfTimeDate,pdfName,pdfName_sender,pdfTimeDate_sender,show_message
                ,download_pdf,download_pdf_sender,delete_one_message,delete_one_message_sender,text_time_date,text_time_date_sender
                ,txt_seen,txt_seen_sender
                ,text_show_password_sender,password_sender,password_null_sender,password_ok_sender,show_message_sender;
        public RelativeLayout relativePdf,relativePdf_sender,linear_image,linear_video,linear_video_sender,
                linear_image_sender,relativeText,relativeText_sender;
        LinearLayout linear_text,linear_text_sender,linearpdf,linearpdf_sender,linear_text_password_sender
                , delete_on_linear,delete_one_linear_sender;
      public   Button password_ok;
      public   EditText password,password_null;
        VideoView show_video,show_video_sender;

        public ImageViewHolder(View itemView) {
            super(itemView);


            relativeText = itemView.findViewById(R.id.relativeText);
            text_show_password = itemView.findViewById(R.id.show_message_password);
            password = itemView.findViewById(R.id.password);
            password_null = itemView.findViewById(R.id.password_null);
            password_ok = itemView.findViewById(R.id.password_ok);
            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            linear_text = itemView.findViewById(R.id.lineartext);
            show_message = itemView.findViewById(R.id.show_message);
            text_time_date = itemView.findViewById(R.id.texttimeDate);



            relativeText_sender = itemView.findViewById(R.id.relativeText_sender);
            text_show_password_sender = itemView.findViewById(R.id.show_message_password_sender);
            password_sender = itemView.findViewById(R.id.password_sender);
            password_null_sender = itemView.findViewById(R.id.password_null_sender);
            password_ok_sender = itemView.findViewById(R.id.password_ok_sender);
            image_profile = itemView.findViewById(R.id.image_profile);
            linear_text_sender = itemView.findViewById(R.id.lineartext_sender);
            show_message_sender = itemView.findViewById(R.id.show_message_sender);
            text_time_date_sender = itemView.findViewById(R.id.texttimeDate_sender);




            show_Image = itemView.findViewById(R.id.show_image_image);
            timeDateImage = itemView.findViewById(R.id.imageTimeDate);
            linear_image = itemView.findViewById(R.id.linearImage);

            show_Image_sender = itemView.findViewById(R.id.show_image_image_sender);
            timeDateImage_sender = itemView.findViewById(R.id.imageTimeDate_sender);
            linear_image_sender = itemView.findViewById(R.id.linearImage_sender);



            show_video = itemView.findViewById(R.id.show_Video);
            timeDateVideo = itemView.findViewById(R.id.videoTimeDate);
            linear_video = itemView.findViewById(R.id.linearsuriVideo);
            play = itemView.findViewById(R.id.play);
            pause = itemView.findViewById(R.id.pause);

            show_video_sender = itemView.findViewById(R.id.show_Video_sender);
            timeDateVideo_sender = itemView.findViewById(R.id.videoTimeDate_sender);
            linear_video_sender = itemView.findViewById(R.id.linearsuriVideo_sender);
            play_sender = itemView.findViewById(R.id.play_sender);
            pause_sender = itemView.findViewById(R.id.pause_sender);



            linearpdf = itemView.findViewById(R.id.linearsuripdf);
            show_pdf = itemView.findViewById(R.id.show_pdf);
            pdfTimeDate = itemView.findViewById(R.id.pdfTimeDate);
            pdfName = itemView.findViewById(R.id.pdfName);
            relativePdf = itemView.findViewById(R.id.relativesuripdf);

            linearpdf_sender = itemView.findViewById(R.id.linearsuripdf_sender);
            show_pdf_sender = itemView.findViewById(R.id.show_pdf_sender);
            pdfTimeDate_sender = itemView.findViewById(R.id.pdfTimeDate_sender);
            pdfName_sender = itemView.findViewById(R.id.pdfName_sender);
            relativePdf_sender = itemView.findViewById(R.id.relativesuripdf_sender);



            delete_on_linear = itemView.findViewById(R.id.delete_one_linear);
            delete_one_message = itemView.findViewById(R.id.delete_one_message);
            download_pdf = itemView.findViewById(R.id.download_pdf);
            delete_one_linear_sender = itemView.findViewById(R.id.delete_one_linear_sender);
            delete_one_message_sender = itemView.findViewById(R.id.delete_one_message_sender);
            download_pdf_sender = itemView.findViewById(R.id.download_pdf_sender);


            txt_seen_sender = itemView.findViewById(R.id.txt_seen_sender);




        }
    }


    private String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodeValue = Base64.decode(outputString,Base64.DEFAULT);
        byte[] decvalue = c.doFinal(decodeValue);
        String decryptvalue = new String(decvalue);
        return decryptvalue;

    }

    private String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptValue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptValue;

    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AEs");
        return secretKeySpec;

    }

    private void deleteReceiveMessage(int position, final MessageAdapterInstagram.ImageViewHolder holder) {



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Chats")
                .child(mComment.get(position).getReceiver())
                .child(mComment.get(position).getSender())
                .child(mComment.get(position).getMessageID())
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

    private void deleteMessageForEveryone(final int position, final MessageAdapterInstagram.ImageViewHolder holder) {

        Comment chat = mComment.get(position);
        String messageId = mComment.get(position).getMessageID();

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Chats")
                .child(mComment.get(position).getSender())
                .child(mComment.get(position).getReceiver())
                .child(mComment.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    holder.itemView.setVisibility(View.INVISIBLE);

                    rootRef.child("Chats")
                            .child(mComment.get(position).getReceiver())
                            .child(mComment.get(position).getSender())
                            .child(mComment.get(position).getMessageID())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(), "Error occurred..", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void playvideo(int position, final MessageAdapterInstagram.ImageViewHolder holder) {
        holder.show_video.start();
        holder.play.setVisibility(View.GONE);
    }

    private void pausevideo(int position, final MessageAdapterInstagram.ImageViewHolder holder) {
        holder.show_video.pause();
        holder.pause.setVisibility(View.GONE);
        holder.play.setVisibility(View.VISIBLE);
    }

    private void playvideoSender(int position, final MessageAdapterInstagram.ImageViewHolder holder) {
        holder.show_video_sender.start();
        holder.play_sender.setVisibility(View.GONE);
    }

    private void pausevideoSender(int position, final MessageAdapterInstagram.ImageViewHolder holder) {
        holder.show_video_sender.pause();
        holder.pause_sender.setVisibility(View.GONE);
        holder.play_sender.setVisibility(View.VISIBLE);
    }

    private void deleteSentMessage(int position, final MessageAdapterInstagram.ImageViewHolder holder) {

        String suri = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(mComment.get(position).getReceiver()).getKey();
        Comment chat = mComment.get(position);

        String messageId = mComment.get(position).getMessageID();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Chats")
                .child(mComment.get(position).getSender())
                .child(mComment.get(position).getReceiver())
                .child(mComment.get(position).getMessageID())
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

}


