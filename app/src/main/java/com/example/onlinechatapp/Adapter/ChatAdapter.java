package com.example.onlinechatapp.Adapter;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Message_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    private ArrayList<Message_Model> messageModels;
    private Context context;
    int receiver_View_Type = 1;
    int sender_View_Type = 2;

    public  ChatAdapter(ArrayList<Message_Model> messageModel, Context context){
        this.messageModels = messageModel;
        this.context = context;

    }

    public int getItemViewType(int position){
        if (messageModels.get(position).getuserid().equals(FirebaseAuth.getInstance().getUid())) {
            return sender_View_Type;
        }
        else {
            return receiver_View_Type;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == sender_View_Type){
            View view = LayoutInflater.from(context).inflate(R.layout.sender, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message_Model messageModel = messageModels.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int i = position;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Unsend Message");
                    builder.setMessage("Note: This Message will not be seen by anyone after unsending.");
                    builder.setPositiveButton("Unsend", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return false;
                }
            });
            if (messageModel.getMsg().equals("*Photo*")) {
                Glide.with(context).load(messageModel.getImageUrl()).into(((SenderViewHolder) holder).photo);
                ((SenderViewHolder) holder).photo.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.GONE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).play.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).card_thumb.setVisibility(View.GONE);
                ((SenderViewHolder) holder).card_pic.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PhotoView.class);
                        intent.putExtra("image", messageModel.getImageUrl());
                        context.startActivity(intent);
                    }
                });
            }
            else if (messageModel.getMsg().equals("*Video*")) {
                ((SenderViewHolder) holder).play.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.GONE);
                ((SenderViewHolder) holder).card_thumb.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).card_pic.setVisibility(View.GONE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(messageModel.getVideoUrl());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(500000); //unit in microsecond

                ((SenderViewHolder) holder).play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent vid = new Intent(context, Videos.class);
                        vid.putExtra("video", messageModel.getVideoUrl());
                        context.startActivity(vid);
                    }
                });
            } else if (messageModel.getMsg().equals("*Audio*")) {
                ((SenderViewHolder) holder).play.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).card_thumb.setVisibility(View.GONE);
                ((SenderViewHolder) holder).card_pic.setVisibility(View.GONE);

            } else if (messageModel.getMsg().equals("*Location*")) {
                ((SenderViewHolder) holder).play.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).location.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).card_thumb.setVisibility(View.GONE);
                ((SenderViewHolder) holder).card_pic.setVisibility(View.GONE);

                ((SenderViewHolder) holder).stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SenderViewHolder) holder).stop.setVisibility(View.GONE);
                    }
                });
            }
            else if (messageModel.getMsg().equals("*Video*")) {
                ((SenderViewHolder) holder).play.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.GONE);
                ((SenderViewHolder) holder).card_thumb.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).card_pic.setVisibility(View.GONE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(messageModel.getVideoUrl());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(500000); //unit in microsecond

                ((SenderViewHolder) holder).play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent vid = new Intent(context, Videos.class);
                        vid.putExtra("video", messageModel.getVideoUrl());
                        context.startActivity(vid);
                    }
                });
            } else if (messageModel.getMsg().equals("*Audio*")) {
                ((SenderViewHolder) holder).play.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).card_thumb.setVisibility(View.GONE);
                ((SenderViewHolder) holder).card_pic.setVisibility(View.GONE);

            } else if (messageModel.getMsg().equals("*Location*")) {
                ((SenderViewHolder) holder).play.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderMsg.setVisibility(View.GONE);
                ((SenderViewHolder) holder).photo.setVisibility(View.GONE);
                ((SenderViewHolder) holder).constraint.setVisibility(View.GONE);
                ((SenderViewHolder) holder).audio.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderlay.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).location.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).card_thumb.setVisibility(View.GONE);
                ((SenderViewHolder) holder).card_pic.setVisibility(View.GONE);

                ((SenderViewHolder) holder).stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SenderViewHolder) holder).stop.setVisibility(View.GONE);
                    }
                });
            }
            ((SenderViewHolder) holder).sendertime.setText(messageModel.getTime());
            ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMsg());

        } else {
            if (messageModel.getMsg().equals("*Photo*")) {
                Glide.with(context).load(messageModel.getImageUrl()).into(((RecieverViewHolder) holder).photos);
                ((RecieverViewHolder) holder).photos.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).play2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).card_picR.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).card_thumbR.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).photos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PhotoView.class);
                        intent.putExtra("image", messageModel.getImageUrl());
                        context.startActivity(intent);
                    }
                });
            }
            else if (messageModel.getMsg().equals("*Video*")) {
                ((RecieverViewHolder) holder).play2.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).photos.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).card_picR.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).card_thumbR.setVisibility(View.VISIBLE);;
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(messageModel.getVideoUrl());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(500000); //unit in microsecond

                ((RecieverViewHolder) holder).play2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent vid = new Intent(context, Videos.class);
                        vid.putExtra("video", messageModel.getVideoUrl());
                        context.startActivity(vid);
                    }
                });
            } else if (messageModel.getMsg().equals("*Location*")) {
                ((RecieverViewHolder) holder).play2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).photos.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).card_picR.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).card_thumbR.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).loc.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).loc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = "geo:0,0?q=india";
                        Uri gmIntentUri = Uri.parse(uri);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (Manifest.permission.INTERNET.equals(null)) {
                            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show();
                        } else {
                            context.startActivity(mapIntent);
                        }

                    }
                });

            } else {
                ((RecieverViewHolder) holder).photos.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).recieverMsg.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).reclayout.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).play2.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).card_picR.setVisibility(View.GONE);
                ((RecieverViewHolder) holder).card_thumbR.setVisibility(View.GONE);
            }
            ((RecieverViewHolder) holder).recievertime.setText(messageModel.getTime());
            ((RecieverViewHolder) holder).recieverMsg.setText(messageModel.getMsg());
        }
    }

    /*private void deletemessage(int position) {
        String time = messageModels.get(position).getTime();
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        Query query = ref.collection("chats").document(messageModels.get(position).getuserid()).collection("time").orderBy("time").whereEqualTo("");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    snapshot1.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg, recievertime;
        ImageView photos,  play2;
        View reclayout;
        Button loc;
        CardView card_picR, card_thumbR;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.RecieverText);
            recievertime = itemView.findViewById(R.id.Reciever_Time);
            photos = itemView.findViewById(R.id.photos);
            reclayout = itemView.findViewById(R.id.reclayout);
            play2 = itemView.findViewById(R.id.play2);
            card_picR = itemView.findViewById(R.id.card_picR);
            card_thumbR = itemView.findViewById(R.id.card_thumbR);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, sendertime, displayname, phonenumber;
        ImageView photo, icon, play, sel;
        Button stop;
        View constraint, senderlay, audio, location;
        WebView gif;
        CardView card_pic, card_thumb;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.Sender_Text);
            sendertime = itemView.findViewById(R.id.Sender_Time);
            photo = itemView.findViewById(R.id.photo);
            displayname = itemView.findViewById(R.id.displayname);
            phonenumber = itemView.findViewById(R.id.phonenum);
            constraint = itemView.findViewById(R.id.contact);
            icon = itemView.findViewById(R.id.contpic);
            gif = itemView.findViewById(R.id.gif);
            play = itemView.findViewById(R.id.play);
            senderlay = itemView.findViewById(R.id.senderlayout);
            audio = itemView.findViewById(R.id.audio);
            stop = itemView.findViewById(R.id.stop);
            sel = itemView.findViewById(R.id.sel);
            card_pic = itemView.findViewById(R.id.card_pic);
            card_thumb = itemView.findViewById(R.id.card_thumb);
        }
    }
}




    @Override
    public int getItemCount() {
        return 0;
    }

}
