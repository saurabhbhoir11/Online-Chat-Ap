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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.viewholder> {
    ArrayList<Users> list;
    Context context;
    FirebaseFirestore firestore;
    String profilepic;

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

        firestore.collection("chats").document(FirebaseAuth.getInstance().getUid() + users.getUserid())
                .collection(FirebaseAuth.getInstance().getUid() + users.getUserid()).orderBy("timestamp").limitToLast(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for(DocumentSnapshot snapshot: value.getDocuments()){
                        if(snapshot.get("msg").toString().equals("$2y$10$39cSefzbHNYvvwTmQpmN2OTZ7jfX.vWd7QeSqgs9pRRWKU7zF7txm")){
                            holder.LastMsg.setText("\uD83D\uDCF7 Photo");
                        }
                        else if(snapshot.get("msg").toString().equals("$ncw$&nwcbwcwjdd!@cnwkcScwxj#5cjwc9qw8dw5cn")){
                            holder.LastMsg.setText("\uD83D\uDCCD Location");
                        }
                        else if(snapshot.get("msg").toString().equals("%msjCkvjx08GH#mc0*mxvhvx4VHs13Nch!cnq-nss.uyCX7xvC")){
                            holder.LastMsg.setText("\uD83D\uDCDE Contact");
                        }
                        else {
                            holder.LastMsg.setText(snapshot.get("msg").toString());
                        }
                        holder.time.setText(snapshot.get("time").toString());
                }
            }
        });

        firestore.collection("Users").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                profilepic = String.valueOf(value.get("profilepic"));
                Glide.with(context).load(value.get("profilepic")).override(96, 96).placeholder(R.drawable.user).into(holder.image);
                holder.UserName.setText(String.valueOf(value.get("username")));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChatDetailActivity.class);
                        intent.putExtra("userId", users.getUserid());
                        intent.putExtra("profile", String.valueOf(value.get("profilepic")));
                        intent.putExtra("username", String.valueOf(value.get("username")));
                        context.startActivity(intent);
                    }
                });

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OtherUserProfile.class);
                        intent.putExtra("userId", users.getUserid());
                        intent.putExtra("profile", String.valueOf(value.get("profilepic")));
                        intent.putExtra("username", String.valueOf(value.get("username")));
                        intent.putExtra("bio", String.valueOf(value.get("Bio")));
                        context.startActivity(intent);
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
        ImageView image;
        TextView UserName, LastMsg, time;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.prof_pic);
            UserName = itemView.findViewById(R.id.user_name);
            LastMsg = itemView.findViewById(R.id.last_msg);
            time = itemView.findViewById(R.id.grp_time);
        }
    }
}
