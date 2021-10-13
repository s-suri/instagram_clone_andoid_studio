package com.some.studychats.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.some.studychats.DirectVideo;
import com.some.studychats.ImageViewActivity;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.R;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/*
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {



    SimpleExoPlayer exoPlayer;


    boolean vidioplay = false;


    String AES = "AES";
    String outputString,convertString;

    ValueEventListener seenListener;

    private int current = 0;
    private int duration = 0;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;



    private FirebaseAuth mAuth;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Comment> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Comment> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        final String messageSenderId = mAuth.getCurrentUser().getUid();
        final Comment chat = mChat.get(position);

        String fromMessageType = chat.getType();
        String fromUserID = chat.getSender();
        String messageId = chat.getMessageID();
        holder.show_message.setText(chat.getMessage());


   //     Picasso.get().load(imageurl).into(holder.profile_image);


        if (position == mChat.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }




            holder.delete_one_message.setVisibility(View.GONE);
            holder.delete_one_linear.setVisibility(View.GONE);
            holder.pdfName.setVisibility(View.GONE);
            holder.textTimeDate.setVisibility(View.GONE);
            holder.imageTimeDate.setVisibility(View.GONE);
            holder.pdfTimeDate.setVisibility(View.GONE);
            holder.videoTimeDate.setVisibility(View.GONE);
            holder.linearpdf.setVisibility(View.GONE);
            holder.linearName.setVisibility(View.GONE);

            holder.show_message.setVisibility(View.GONE);
            holder.show_image.setVisibility(View.GONE);
            holder.linearimage.setVisibility(View.GONE);
            holder.linearpdf.setVisibility(View.GONE);
            holder.show_pdf.setVisibility(View.GONE);
            holder.linearText.setVisibility(View.GONE);
            holder.linearVideo.setVisibility(View.GONE);
            holder.download_pdf.setVisibility(View.GONE);
            holder.fullLinesrvideo.setVisibility(View.GONE);
            holder.text_show_password.setVisibility(View.GONE);
            holder.password.setVisibility(View.GONE);
            holder.password_ok.setVisibility(View.GONE);
            holder.password_null.setVisibility(View.GONE);
            holder.pause.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
        holder.reply_image.setVisibility(View.GONE);
        holder.reply_pdf.setVisibility(View.GONE);
        holder.reply_video.setVisibility(View.GONE);


            /*

            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playvideo(position, holder);

                }
            });
            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pausevideo(position, holder);

                }
            });


            holder.linearVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.play.setVisibility(View.GONE);
                    holder.pause.setVisibility(View.VISIBLE);

                }
            });





            holder.linearVideo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.delete_one_message.setVisibility(View.VISIBLE);
                    holder.delete_one_linear.setVisibility(View.VISIBLE);


                    return true;
                }
            });

            holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteSentMessage(position, holder);
                }
            });


            if (fromMessageType.equals("text")) {
                if (fromUserID.equals(messageSenderId)) {
                    holder.linearText.setVisibility(View.VISIBLE);
                    holder.text_show_password.setVisibility(View.VISIBLE);

                 //   lastMessage(holder.txt_seen);


                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    holder.text_show_password.setText(outputString);


                    holder.linearText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            holder.linearText.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());


                            try {
                                convertString = decrypt(chat.getBio(), holder.password_null.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(convertString);
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


                            try {
                                convertString = decrypt(chat.getBio(), holder.password.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(convertString);

                        }
                    });


                    holder.linearText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {


                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.delete_one_linear.setVisibility(View.VISIBLE);


                            return true;

                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                } else {


                    holder.linearText.setVisibility(View.VISIBLE);
                    holder.text_show_password.setVisibility(View.VISIBLE);


                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    holder.text_show_password.setText(outputString);


                    holder.linearText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.linearText.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());


                            try {
                                convertString = decrypt(chat.getBio(), holder.password_null.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(convertString);
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


                            try {
                                convertString = decrypt(chat.getBio(), holder.password.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(convertString);
                        }
                    });


                    holder.linearText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.delete_one_linear.setVisibility(View.VISIBLE);


                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteReceiveMessage(position, holder);
                        }
                    });
                }

            } else if (fromMessageType.equals("image")) {

                if (fromUserID.equals(messageSenderId)) {
                    holder.show_image.setVisibility(View.VISIBLE);
                    holder.linearimage.setVisibility(View.VISIBLE);
                    holder.imageTimeDate.setVisibility(View.VISIBLE);
                    holder.imageTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    Picasso.get().load(chat.getMessage()).into(holder.show_image);

                    holder.reply_image.setVisibility(View.VISIBLE);
                    holder.reply_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    holder.show_image.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {


                            Intent chatIntent = new Intent(holder.itemView.getContext(), ImageViewActivity.class);
                            chatIntent.putExtra("url", mChat.get(position).getMessage());


                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(holder.show_image, "imageTransition");

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), pairs);


                            holder.itemView.getContext().startActivity(chatIntent, options.toBundle());


                        }
                    });

                    holder.show_image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.delete_one_linear.setVisibility(View.VISIBLE);


                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteMessageForEveryone(position, holder);
                        }
                    });


                } else {

                    holder.show_image.setVisibility(View.VISIBLE);
                    holder.linearimage.setVisibility(View.VISIBLE);
                    holder.imageTimeDate.setVisibility(View.VISIBLE);
                    holder.imageTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    Picasso.get().load(chat.getMessage()).into(holder.show_image);


                    holder.show_image.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            Intent chatIntent = new Intent(holder.itemView.getContext(), ImageViewActivity.class);
                            chatIntent.putExtra("url", mChat.get(position).getMessage());


                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(holder.show_image, "imageTransition");

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), pairs);


                            holder.itemView.getContext().startActivity(chatIntent, options.toBundle());

                        }
                    });


                    holder.show_image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.delete_one_linear.setVisibility(View.VISIBLE);


                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteReceiveMessage(position, holder);
                        }
                    });


                }
            } else if (fromMessageType.equals("video")) {
                if (fromUserID.equals(messageSenderId)) {

                    holder.linearVideo.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    holder.show_video.setVisibility(View.VISIBLE);



                /*    holder.show_video.setVideoPath(chat.getMessage());
                    holder.show_video.requestFocus();
                    holder.show_video.seekTo(1);
                    holder.play.setVisibility(View.VISIBLE);






                    try {
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                        Uri video = Uri.parse(mChat.get(position).getMessage());
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                        holder.show_video.setPlayer(exoPlayer);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(false);

                    } catch (Exception e) {
                        Log.e("ViewHolder", "exoplayer error" + e.toString());

                    }






                    holder.open_video_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exoPlayer.stop();
                            Intent intent = new Intent(holder.itemView.getContext(), DirectVideo.class);
                            intent.putExtra("visit_user_id", chat.getReceiver());
                            intent.putExtra("messageId", mChat.get(position).getMessage());
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });


                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteMessageForEveryone(position, holder);
                        }
                    });


                } else {

                    holder.linearVideo.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    holder.show_video.setVisibility(View.VISIBLE);

                 /*    holder.show_video.setVideoPath(chat.getMessage());
                    holder.show_video.requestFocus();
                    holder.show_video.seekTo(1);
                    holder.play.setVisibility(View.VISIBLE)


                     try {
                         BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                         TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                         exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                         Uri video = Uri.parse(mChat.get(position).getMessage());
                         DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                         ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                         MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                         holder.show_video.setPlayer(exoPlayer);
                         exoPlayer.prepare(mediaSource);
                         exoPlayer.seekTo(1);
                         exoPlayer.setPlayWhenReady(false);
                        exoPlayer.stop();


                     } catch (Exception e) {
                         Log.e("ViewHolder", "exoplayer error" + e.toString());

                     }





                    holder.open_video_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(holder.itemView.getContext(), DirectVideo.class);
                            intent.putExtra("visit_user_id", chat.getReceiver());
                            intent.putExtra("messageId", mChat.get(position).getMessage());
                            holder.itemView.getContext().startActivity(intent);

                        }
                    });


                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteReceiveMessage(position, holder);
                        }
                    });

                }
            } else if (fromMessageType.equals("PDF") || (fromMessageType.equals("docx"))) {
                {
                    if (fromUserID.equals(messageSenderId)) {
                        holder.linearpdf.setVisibility(View.VISIBLE);
                        holder.pdfName.setVisibility(View.VISIBLE);
                        holder.pdfName.setText(chat.getName());
                        holder.linearName.setVisibility(View.VISIBLE);
                        holder.show_pdf.setVisibility(View.VISIBLE);
                        holder.pdfTimeDate.setVisibility(View.VISIBLE);
                        holder.pdfTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                                .into(holder.show_pdf);

                        holder.show_pdf.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                holder.delete_one_message.setVisibility(View.VISIBLE);
                                holder.delete_one_linear.setVisibility(View.VISIBLE);
                                holder.download_pdf.setVisibility(View.VISIBLE);

                                return true;
                            }
                        });

                        holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteMessageForEveryone(position, holder);
                            }
                        });

                        holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mChat.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });

                    } else {
                        holder.linearName.setVisibility(View.VISIBLE);
                        holder.pdfName.setVisibility(View.VISIBLE);
                        holder.pdfName.setText(chat.getName());
                        holder.linearpdf.setVisibility(View.VISIBLE);
                        holder.pdfTimeDate.setVisibility(View.VISIBLE);
                        holder.pdfTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                        holder.show_pdf.setVisibility(View.VISIBLE);
                        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                                .into(holder.show_pdf);

                        holder.show_pdf.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                holder.delete_one_message.setVisibility(View.VISIBLE);
                                holder.delete_one_linear.setVisibility(View.VISIBLE);
                                holder.download_pdf.setVisibility(View.VISIBLE);

                                return true;
                            }
                        });

                        holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteReceiveMessage(position, holder);
                            }
                        });
                        holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mChat.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });

                    }

                }

            } else {
                holder.fullLinesrvideo.setVisibility(View.GONE);

            }


    }

        @Override
        public int getItemCount () {
            return mChat.size();
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_show_password,play,pause,show_message,imageTimeDate,videoTimeDate,pdfTimeDate,delete_one_message,delete_text_message,download_pdf,
                    open_video_activity,reply_image,reply_pdf,reply_video;
            public ImageView profile_image,show_image,show_pdf;
            VideoView full_video;
            EditText  password,password_null;
            public TextView txt_seen,textTimeDate,pdfName,full_screen,iconHidden;
            LinearLayout linearText,delete_text_linear,linearName;
            RelativeLayout  linearimage,linearVideo,linearpdf,fullLinesrvideo,delete_one_linear;


            private PlayerView show_video;

            Button password_ok;
            ProgressBar splashProgress;
            public ViewHolder(View itemView) {
                super(itemView);





                password = itemView.findViewById(R.id.password);
                password_null = itemView.findViewById(R.id.password_null);
                password_ok = itemView.findViewById(R.id.password_ok);
                text_show_password = itemView.findViewById(R.id.show_message_password);
                splashProgress = itemView.findViewById(R.id.splashProgress);
                imageTimeDate = itemView.findViewById(R.id.imageTimeDate);
                show_message = itemView.findViewById(R.id.show_message);
                linearText = itemView.findViewById(R.id.lineartext);
                profile_image = itemView.findViewById(R.id.profile_image);
                txt_seen = itemView.findViewById(R.id.txt_seen);
                show_image = itemView.findViewById(R.id.show_image_image);
                textTimeDate = itemView.findViewById(R.id.texttimeDate);
                linearimage = itemView.findViewById(R.id.linearsuri);
                linearpdf = itemView.findViewById(R.id.linearsuripdf);
                show_pdf = itemView.findViewById(R.id.show_pdf);
                show_video = itemView.findViewById(R.id.show_Video);
                linearVideo = itemView.findViewById(R.id.linearsuriVideo);
                videoTimeDate = itemView.findViewById(R.id.videoTimeDate);
                pdfTimeDate = itemView.findViewById(R.id.pdfTimeDate);
                delete_one_linear = itemView.findViewById(R.id.delete_one_linear);
                delete_one_message = itemView.findViewById(R.id.delete_one_message);
                download_pdf = itemView.findViewById(R.id.download_pdf);
                delete_text_linear =itemView.findViewById(R.id.delete_text_linear);
                delete_text_message =itemView.findViewById(R.id.delete_text_message);
                pdfName = itemView.findViewById(R.id.pdfName);
                linearName = itemView.findViewById(R.id.linearName);
                full_video = itemView.findViewById(R.id.full_Video);
                fullLinesrvideo = itemView.findViewById(R.id.fulLinearVideo);
                play =itemView.findViewById(R.id.play);
                pause = itemView.findViewById(R.id.pause);
                open_video_activity = itemView.findViewById(R.id.open_video_activity);
                reply_image = itemView.findViewById(R.id.replyImage);
                reply_pdf = itemView.findViewById(R.id.replyPdf);
                reply_video = itemView.findViewById(R.id.replyVideo);


            }
        }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    private void deleteSentMessage(int position, final ViewHolder holder) {

        String suri = FirebaseDatabase.getInstance().getReference().child(fuser.getUid()).child(mChat.get(position).getReceiver()).getKey();
        Comment chat = mChat.get(position);

        String messageId = mChat.get(position).getMessageID();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Chats")
                .child(mChat.get(position).getSender())
                .child(mChat.get(position).getReceiver())
                .child(mChat.get(position).getMessageID())
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
    private void deleteReceiveMessage(int position, final ViewHolder holder) {



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Chats")
                .child(mChat.get(position).getReceiver())
                .child(mChat.get(position).getSender())
                .child(mChat.get(position).getMessageID())
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

    private void deleteMessageForEveryone(final int position, final ViewHolder holder) {

        Comment chat = mChat.get(position);
        String messageId = mChat.get(position).getMessageID();

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Chats")
                .child(mChat.get(position).getSender())
                .child(mChat.get(position).getReceiver())
                .child(mChat.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    holder.itemView.setVisibility(View.INVISIBLE);

                    rootRef.child("Chats")
                            .child(mChat.get(position).getReceiver())
                            .child(mChat.get(position).getSender())
                            .child(mChat.get(position).getMessageID())
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

    private void playProgress(final int position, final ViewHolder holder) {
        ObjectAnimator.ofInt(holder.splashProgress, "progress", 100)
                .setDuration(5000)
                .start();

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

    private void playvideo(int position, final ViewHolder holder) {
      //  holder.show_video.start();
        holder.play.setVisibility(View.GONE);
    }

    private void pausevideo(int position, final ViewHolder holder) {
     //   holder.show_video.pause();
        holder.pause.setVisibility(View.GONE);
        holder.play.setVisibility(View.VISIBLE);
    }

    private void seenMessagesender(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(userid).child(fuser.getUid());
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(userid) && chat.getReceiver().equals(fuser.getUid())
                    ) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}



 */