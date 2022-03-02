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

    public NotificationAdapter(ArrayList<Users>list,Context context){
        this.list=list;
        this.context=context;
    }


    @NonNull
    @Override
    public NotificationAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_confirmation, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.viewholder holder, int position) {
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        Users users= list.get(position);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",users.getUserid());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashMap.put("status","1");
                firestore.collection("notifications").document(auth.getCurrentUser().getUid())
                        .collection("userid").document(users.getUserid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashMap.put("status","-1");
                firestore.collection("notifications").document(auth.getCurrentUser().getUid())
                        .collection("userid").document(users.getUserid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Request Declined", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        firestore.collection("Users").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                holder.username.setText(value.get("username").toString());
                Glide.with(context).load(value.get("profilepic").toString()).into(holder.profile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        Button accept,decline;
        TextView username;
        ImageView profile;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            accept=itemView.findViewById(R.id.acpt_btn);
            decline=itemView.findViewById(R.id.dec_btn);
            username=itemView.findViewById(R.id.username2);
            profile=itemView.findViewById(R.id.prof_img);
        }
    }
}
