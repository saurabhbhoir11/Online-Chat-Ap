package com.example.onlinechatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.ChatDetailActivity;
import com.example.onlinechatapp.OtherUserProfile;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.viewholder> {
    ArrayList<Users> list;
    Context context;
    FirebaseFirestore firestore;
    String username,profilepic;

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
        firestore=FirebaseFirestore.getInstance();

        firestore.collection("Users").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                username = String.valueOf(value.get("username"));
                profilepic = String.valueOf(value.get("profilepic"));
                Glide.with(context).load(value.get("profilepic")).override(96, 96).placeholder(R.drawable.user).into(holder.image);
                holder.UserName.setText(username);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", users.getUserid());
                intent.putExtra("profile", profilepic);
                intent.putExtra("username", username);
                intent.putExtra("tagline", users.getTagline());
                context.startActivity(intent);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherUserProfile.class);
                intent.putExtra("userId", users.getUserid());
                intent.putExtra("profile", profilepic);
                intent.putExtra("username", username);
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
