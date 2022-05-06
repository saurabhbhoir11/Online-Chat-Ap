package com.example.onlinechatapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.ChatDetailActivity;
import com.example.onlinechatapp.OtherUserProfile;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    ArrayList<Users> list;
    Context context;
    FirebaseUser firebaseUser;
    FirebaseFirestore FirebaseDatabase;

    public SearchAdapter(List<Users> list, Context context) {
        this.list = (ArrayList<Users>) list;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);
        Glide.with(context).load(users.getProfilepic()).placeholder(R.drawable.user).into(holder.image);
        holder.UserName.setText(users.getUsername());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserProfile.class);
                intent.putExtra("userId", users.getUserid());
                intent.putExtra("profile", users.getProfilepic());
                intent.putExtra("username", users.getUsername());
                context.startActivity(intent);
            }
        });

       holder.msg_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, ChatDetailActivity.class);
               intent.putExtra("userId", users.getUserid());
               intent.putExtra("profile", users.getProfilepic());
               intent.putExtra("username", users.getUsername());
               context.startActivity(intent);
           }
       });
    }


        @Override
        public int getItemCount () {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView UserName;
            Button msg_btn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.srch_img);
                UserName = itemView.findViewById(R.id.username_srch);
                msg_btn = itemView.findViewById(R.id.flw_btn);
            }
        }
}