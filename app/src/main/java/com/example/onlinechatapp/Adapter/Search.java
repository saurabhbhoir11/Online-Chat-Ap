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

import com.bumptech.glide.Glide;
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

    public SearchAdapter(List<Users> list, Context context){
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
        isFollowed(users.getUserid(), holder.follow);

        if(users.getUserid().equals(firebaseUser.getUid())){
            holder.follow.setVisibility(View.GONE);
        }

        holder.follow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(holder.follow.getText().toString().equals("Follow")){
                    FirebaseDatabase.getInstance().collection("Follow")
                            .document(users.getUserid()).collection("following").document(users.getUserid())
                            .set(true);

                    FirebaseDatabase.getInstance().collection("Follow")
                            .document(users.getUserid()).collection("followers").document(users.getUserid())
                            .set(true);
                }
                else{
                    FirebaseDatabase.getInstance().collection("Follow")
                            .document(users.getUserid()).collection("following").document(users.getUserid())
                            .delete();

                    FirebaseDatabase.getInstance().collection("Follow")
                            .document(users.getUserid()).collection("followers").document(users.getUserid())
                            .delete();
                }
            }
        });
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfile.class);
                intent.putExtra("userId", users.getUserid());
                intent.putExtra("profile", users.getProfilepic());
                intent.putExtra("username", users.getUsername());
                intent.putExtra("tagline", users.getTagline());
                context.startActivity(intent);

            }
        });*/
    }

    public void isFollowed(String userid, Button follow){
        FirebaseDatabase.getInstance().collection("Follow")
                .document(firebaseUser.getUid()).collection("following").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.getDocuments().()){
                    follow.setText("Following");
                }
                        else
                {
                    follow.setText("Follow");
                }
            }
        })




        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView UserName;
            Button follow;
            public ViewHolder(@NonNull View itemView){
                super(itemView);
                image = itemView.findViewById(R.id.contpic);
                UserName = itemView.findViewById(R.id.UserName);
                follow = itemView.findViewById(R.id.follower);
            }
        }
    }