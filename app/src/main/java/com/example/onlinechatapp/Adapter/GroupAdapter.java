package com.example.onlinechatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.GroupDetailedActivity;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.HolderGroupAdapter> {
    private Context context;
    private ArrayList<GroupModel> GroupList;
    FirebaseFirestore firestore;

    public GroupAdapter(Context context, ArrayList<GroupModel> GroupList) {
        this.context = context;
        this.GroupList = GroupList;
    }

    @NonNull
    @Override
    public HolderGroupAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_group, parent, false);
        return new HolderGroupAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupAdapter holder, int position) {
        GroupModel model = GroupList.get(position);
        firestore= FirebaseFirestore.getInstance();

        firestore.collection("groups").document(model.getGroupId()).collection("Messages").orderBy("timestamp").limitToLast(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot snapshot: value.getDocuments()){
                    if(snapshot.get("msg").toString().equals("$2y$10$39cSefzbHNYvvwTmQpmN2OTZ7jfX.vWd7QeSqgs9pRRWKU7zF7txm")){
                        holder.lastmsg.setText("\uD83D\uDCF7 Photo");
                    }
                    else if(snapshot.get("msg").toString().equals("$ncw$&nwcbwcwjdd!@cnwkcScwxj#5cjwc9qw8dw5cn")){
                        holder.lastmsg.setText("\uD83D\uDCCD Location");
                    }
                    else if(snapshot.get("msg").toString().equals("%msjCkvjx08GH#mc0*mxvhvx4VHs13Nch!cnq-nss.uyCX7xvC")){
                        holder.lastmsg.setText("\uD83D\uDCDE Contact");
                    }
                    else {
                        holder.lastmsg.setText(snapshot.get("msg").toString());
                    }
                    holder.time.setText(snapshot.get("time").toString());
                    firestore.collection("Users").document(snapshot.get("sender").toString()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            holder.sender.setText(value.get("username").toString()+": ");
                        }
                    });
                }
            }
        });

        String groupId = model.getGroupId();
        String groupIcon = model.getGroupIcon();
        holder.grpTitle.setText(model.getGroupTitle());

        Glide.with(context).load(groupIcon).placeholder(R.drawable.user).into(holder.grpIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupDetailedActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("GroupIcon", groupIcon);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return GroupList.size();
    }

    public static class HolderGroupAdapter extends RecyclerView.ViewHolder {
        ImageView grpIcon;
        TextView grpTitle, sender, lastmsg, time;

        public HolderGroupAdapter(@NonNull View itemView) {
            super(itemView);

            grpIcon = itemView.findViewById(R.id.grp_Icon);
            grpTitle = itemView.findViewById(R.id.grp_title);
            sender = itemView.findViewById(R.id.sender_name);
            lastmsg = itemView.findViewById(R.id.last_msg1);
            time = itemView.findViewById(R.id.grp_time);
        }
    }
}
