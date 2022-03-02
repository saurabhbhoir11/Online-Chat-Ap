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

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<Message_Model> messageModels;
    private Context context;
    int receiver_View_Type = 1;
    int sender_View_Type = 2;

    public ChatAdapter(ArrayList<Message_Model> messageModel, Context context) {
        this.messageModels = messageModel;
        this.context = context;

    }

    public int getItemViewType(int position) {
        if (messageModels.get(position).getuserid().equals(FirebaseAuth.getInstance().getUid())) {
            return sender_View_Type;
        } else {
            return receiver_View_Type;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == sender_View_Type) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_reciever, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message_Model messageModel = messageModels.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).sendertime.setText(messageModel.getTime());
            ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMsg());

        } else {
            ((RecieverViewHolder) holder).recievertime.setText(messageModel.getTime());
            ((RecieverViewHolder) holder).recieverMsg.setText(messageModel.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg, recievertime;


        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.rec_msg);
            recievertime = itemView.findViewById(R.id.rec_time);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, sendertime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.send_msg);
            sendertime = itemView.findViewById(R.id.send_time);
        }
    }
}
