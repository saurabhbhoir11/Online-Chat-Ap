package com.example.onlinechatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Users;

import java.util.ArrayList;
import java.util.List;

public  class haveAdapter extends RecyclerView.Adapter<haveAdapter.HolderRoom> {
    ArrayList<Users> lists;
    Context context;

    public haveAdapter(List<Users> list, Context context) {
        this.lists = (ArrayList<Users>) list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderRoom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatroom, parent, false);
        return new HolderRoom(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRoom holder, int position) {
        Users users = lists.get(position);
        String name = users.getUsername();
        String profile = users.getProfilepic();
        holder.roomuser.setText(name);
        if(profile==null){
            Glide.with(context).load(R.drawable.user).into(holder.roomimg);
        }
        else {
            Glide.with(context).load(profile).placeholder(R.drawable.user).into(holder.roomimg);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return lists.size();
    }

    class HolderRoom extends RecyclerView.ViewHolder {
        TextView roomuser;
        ImageView roomimg;

        public HolderRoom(@NonNull View itemView) {
            super(itemView);
            roomimg = itemView.findViewById(R.id.room_img);
            roomuser = itemView.findViewById(R.id.roomuser);
        }
    }
}

