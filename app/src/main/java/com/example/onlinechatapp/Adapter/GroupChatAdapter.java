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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.PhotoView;
import com.example.onlinechatapp.Video;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.GroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public GroupChatAdapter(ArrayList<GroupChat> GroupChatModel, Context context) {
        this.GroupChatModel = GroupChatModel;
        this.context = context;

        auth=FirebaseAuth.getInstance();
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
            if (grpmodel.getMsg().equals("*Photo*")) {
                Glide.with(context).load(grpmodel.getImageUrl()).into(((SenderViewHolder) holder).photo);
                ((SenderViewHolder) holder).photo.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.GONE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).play.setVisibility(View.GONE);
                ((SenderViewHolder) holder).thumbanail.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).sticker.setVisibility(View.GONE);
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
                ((SenderViewHolder) holder).play.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.GONE);
                ((SenderViewHolder) holder).thumbanail.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).sticker.setVisibility(View.GONE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(grpmodel.getVideoUrl());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(500000); //unit in microsecond
                ((SenderViewHolder) holder).thumbanail.setImageBitmap(bmFrame);

                ((SenderViewHolder) holder).play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent vid = new Intent(context, Video.class);
                        vid.putExtra("video", grpmodel.getVideoUrl());
                        context.startActivity(vid);
                    }
                });
            }
            else if (grpmodel.getMsg().equals("*Audio*")) {
                ((SenderViewHolder) holder).play.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).thumbanail.setVisibility(View.GONE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).sticker.setVisibility(View.GONE);
            }
            else if (grpmodel.getMsg().equals("*GIF*")) {
                ((SenderViewHolder) holder).gif.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).gif.loadUrl(grpmodel.getWebUrl());
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
            }
            else {
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.VISIBLE);

            }
            //((SenderViewHolder) holder).sendertime.setText(grpmodel.getTime());
            ((SenderViewHolder) holder).senderMsg.setText(grpmodel.getMsg());

        }

        else{
            if(grpmodel.getMsg().equals("*Photo*")) {
                Glide.with(context).load(grpmodel.getImageUrl()).into(((RecieverViewHolder) holder).photos);
                ((RecieverViewHolder) holder).photos.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).play2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).thumbnail2.setVisibility(View.GONE);
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
                ((RecieverViewHolder) holder).play2.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).photos.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recsticker.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).thumbnail2.setVisibility(View.VISIBLE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(grpmodel.getVideoUrl());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(500000); //unit in microsecond
                ((RecieverViewHolder) holder).thumbnail2.setImageBitmap(bmFrame);

                ((RecieverViewHolder) holder).play2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent vid = new Intent(context, Video.class);
                        vid.putExtra("video",grpmodel.getVideoUrl());
                        context.startActivity(vid);
                    }
                });
            }
            else
            {
                ((RecieverViewHolder) holder).photos.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recsticker.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).reclayout.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).play2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).thumbnail2.setVisibility(View.GONE);
            }
            //((RecieverViewHolder)holder).recievertime.setText(grpmodel.getTime());
            ((RecieverViewHolder) holder).recieverMsg.setText(grpmodel.getMsg());
            //FirebaseFirestore.getInstance().collection("Users").orderBy("userid").equals(grpmodel.getSender()).

        }
    }

    @Override
    public int getItemCount() {
        return GroupChatModel.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg, recievertime,sendname;
        ImageView photos, recsticker,play2, thumbnail2,send_prof;
        View reclayout;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.rec_msg2);
            recievertime = itemView.findViewById(R.id.rec_time2);
            photos= itemView.findViewById(R.id.grp_img);
            //reclayout=itemView.findViewById(R.id.reclayout);
            sendname=itemView.findViewById(R.id.sender_name);
            send_prof=itemView.findViewById(R.id.sender_profile);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, sendertime, displayname, phonenumber;
        ImageView photo, icon, sticker,play, thumbanail;
        Button allow,stop;
        View constraint, senderlay,audio, location;
        WebView gif;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.Sender_Text);
            sendertime = itemView.findViewById(R.id.Sender_Time);
            photo= itemView.findViewById(R.id.photo);
        }
    }
}
