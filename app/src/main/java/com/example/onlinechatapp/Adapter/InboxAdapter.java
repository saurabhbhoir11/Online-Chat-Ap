package com.example.onlinechatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.ChatDetailActivity;
import com.example.onlinechatapp.OtherUserProfile;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.viewholder> {
    ArrayList<Users> list;
    Context context;

    public InboxAdapter(ArrayList<Users> list,Context context) {
      this.list=list;
      this.context=context;
    }

    @NonNull
    @Override
    public InboxAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_users, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.viewholder holder, int position) {
        Users users = list.get(position);
        Glide.with(context).load(users.getProfilepic()).override(96, 96).placeholder(R.drawable.user).into(holder.image);
        holder.UserName.setText(users.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", users.getUserid());
                intent.putExtra("profile", users.getProfilepic());
                intent.putExtra("username", users.getUsername());
                intent.putExtra("tagline", users.getTagline());
                context.startActivity(intent);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserProfile.class);
                intent.putExtra("userId", users.getUserid());
                intent.putExtra("profile", users.getProfilepic());
                intent.putExtra("username", users.getUsername());
                intent.putExtra("tagline", users.getTagline());
                intent.putExtra("follow", users.getFollow());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView UserName, LastMsg, time;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.prof_pic);
            UserName = itemView.findViewById(R.id.user_name);
            LastMsg = itemView.findViewById(R.id.last_msg);
            time = itemView.findViewById(R.id.time);
        }
    }
}
