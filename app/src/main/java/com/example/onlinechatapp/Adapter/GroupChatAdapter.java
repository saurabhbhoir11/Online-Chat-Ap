package com.example.onlinechatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.PhotoView;
import com.example.onlinechatapp.Video;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.GroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.Collator;
import java.util.ArrayList;

public class GroupChatAdapter extends RecyclerView.Adapter{
    private ArrayList<GroupChat> GroupChatModel;
    private Context context;
    int Reciever_view_type = 1;
    int Sender_view_type = 2;
    int System_view_type = 3;
    int system=0;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    public GroupChatAdapter(ArrayList<GroupChat> GroupChatModel, Context context) {
        this.GroupChatModel = GroupChatModel;
        this.context = context;

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
    }
    @Override
    public int getItemViewType(int position) {
        if (GroupChatModel.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return Sender_view_type;
        } else {
            return Reciever_view_type;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Sender_view_type) {
            View view = LayoutInflater.from(context).inflate(R.layout.group_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiverroom, parent, false);
            return new RecieverViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupChat grpmodel = GroupChatModel.get(position);
       if (holder.getClass() == SenderViewHolder.class) {
            if (grpmodel.getMsg().equals("$2y$10$39cSefzbHNYvvwTmQpmN2OTZ7jfX.vWd7QeSqgs9pRRWKU7zF7txm")) {
                Glide.with(context).load(grpmodel.getImageUrl()).into(((SenderViewHolder) holder).photo);
                ((SenderViewHolder) holder).photo.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PhotoView.class);
                        intent.putExtra("image", grpmodel.getImageUrl());
                        context.startActivity(intent);
                    }
                });
            }
            else if (grpmodel.getMsg().equals("*Video*")) {
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(grpmodel.getVideoUrl());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(500000); //unit in microsecond
               // ((SenderViewHolder) holder).thumbanail.setImageBitmap(bmFrame);

                /*((SenderViewHolder) holder).play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent vid = new Intent(context, Video.class);
                        vid.putExtra("video", grpmodel.getVideoUrl());
                        context.startActivity(vid);
                    }
                });*/
            }
            else {
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.VISIBLE);

            }
            //((SenderViewHolder) holder).sendertime.setText(grpmodel.getTime());
            ((SenderViewHolder) holder).senderMsg.setText(grpmodel.getMsg());
            ((SenderViewHolder) holder).sendertime.setText(grpmodel.getTime());

        }

        else{
           firestore.collection("Users").document(grpmodel.getSender()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
               @Override
               public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                   ((RecieverViewHolder) holder).sendname.setText(String.valueOf(value.get("username")));
                   Glide.with(context).load(""+value.get("profilepic")).into(((RecieverViewHolder) holder).send_prof);
               }
           });

            if(grpmodel.getMsg().equals("$2y$10$39cSefzbHNYvvwTmQpmN2OTZ7jfX.vWd7QeSqgs9pRRWKU7zF7txm")) {
                Glide.with(context).load(grpmodel.getImageUrl()).into(((RecieverViewHolder) holder).photos);
                ((RecieverViewHolder) holder).photos.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).photos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(context, PhotoView.class);
                        intent.putExtra("image",grpmodel.getImageUrl());
                        context.startActivity(intent);
                    }
                });
            }

            else if(grpmodel.getMsg().equals("*Video*")){

            }
            else
            {
                ((RecieverViewHolder) holder).photos.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.VISIBLE);
              //  ((RecieverViewHolder) holder).reclayout.setVisibility(View.VISIBLE);
            }
            ((RecieverViewHolder) holder).recieverMsg.setText(grpmodel.getMsg());
            ((RecieverViewHolder) holder).recievertime.setText(grpmodel.getTime());
            //FirebaseFirestore.getInstance().collection("Users").orderBy("userid").equals(grpmodel.getSender()).

        }
    }

    @Override
    public int getItemCount() {
        return GroupChatModel.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg, recievertime,sendname;
        ImageView send_prof,photos;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.rec_msg2);
            recievertime = itemView.findViewById(R.id.rec_time2);
            photos= itemView.findViewById(R.id.grp_img);
            sendname=itemView.findViewById(R.id.sender_name);
            send_prof=itemView.findViewById(R.id.sender_prof);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, sendertime;
        ImageView photo;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.Sender_Text);
            sendertime = itemView.findViewById(R.id.Sender_Time);
            photo= itemView.findViewById(R.id.photo);
        }
    }
}
