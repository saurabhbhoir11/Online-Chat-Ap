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
import com.example.onlinechatapp.Notifications.MyFirebaseMessagingService;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddPartiAdapter extends  RecyclerView.Adapter<AddPartiAdapter.ViewHolder>{
    ArrayList<Users> list;
    Context context;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    String groupId;

    public AddPartiAdapter(List<Users> list, Context context, String groupId) {
        this.list = (ArrayList<Users>) list;
        this.context = context;
        this.groupId = groupId;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_add_participants, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);
        Glide.with(context).load(users.getProfilepic()).placeholder(R.drawable.user).into(holder.image);
        holder.UserName.setText(users.getUsername());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("groups").document(groupId).collection("Participants").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value.exists()){
                            holder.add.setVisibility(View.GONE);
                        }
                        else {
                            addParticipant(users);
                        }
                    }
                });


            }
        });
    }

    private void addParticipant(Users users) {
        String timestamp=""+System.currentTimeMillis();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",users.getUserid());
        hashMap.put("role","participant");
        hashMap.put("timestamp",timestamp);

        FirebaseFirestore.getInstance().collection("Users").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String token = String.valueOf(value.get("token"));
                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm:aa");
                final String savetime = currenttime.format(ctime.getTime());
                MyFirebaseMessagingService.senPushNotification("You were added to the group", "New Group", token, savetime);
            }
        });

        firestore.collection("groups").document(groupId).collection("Participants").document(users.getUserid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Added " + users.getUsername() + " to the room", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView UserName;
        Button add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.contpic);
            UserName = itemView.findViewById(R.id.UserName);
            add = itemView.findViewById(R.id.add);
        }
    }
}
