package com.example.onlinechatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewholder> {
    ArrayList<Users> list;
    Context context;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    public NotificationAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public NotificationAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_confirmation, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.viewholder holder, int position) {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        Users users = list.get(position);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", users.getUserid());


        firestore.collection("Users").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String username= String.valueOf(value.get("username"));
                holder.username.setText(username);
                Glide.with(context).load(value.get("profilepic")).placeholder(R.drawable.user).into(holder.profile);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashMap.put("status", "1");
                firestore.collection("notifications").document(auth.getCurrentUser().getUid())
                        .collection("userid").document(users.getUserid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                        HashMap<String, Object> hashMap1 = new HashMap<>();
                        hashMap1.put("uid", auth.getCurrentUser().getUid());
                        firestore.collection("friends").document(users.getUserid()).collection("userid").document(auth.getCurrentUser().getUid()).set(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                HashMap<String, Object> hashMap2 = new HashMap<>();
                                hashMap2.put("uid", users.getUserid());
                                firestore.collection("friends").document(auth.getCurrentUser().getUid()).collection("userid").document(users.getUserid()).set(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashMap.put("status", "-1");
                firestore.collection("notifications").document(auth.getCurrentUser().getUid())
                        .collection("userid").document(users.getUserid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Request Declined", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        Button accept, decline;
        TextView username;
        ImageView profile;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            accept = itemView.findViewById(R.id.acpt_btn);
            decline = itemView.findViewById(R.id.dec_btn);
            username = itemView.findViewById(R.id.username2);
            profile = itemView.findViewById(R.id.prof_img);
        }
    }
}
