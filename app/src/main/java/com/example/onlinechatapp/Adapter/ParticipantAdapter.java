package com.example.onlinechatapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.onlinechatapp.OtherUserProfile;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.models.GroupModel;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.HolderParticipants> {
    ArrayList<Users> list;
    Context context;
    String groupId, grprole, hisRole, previousRole;
    FirebaseFirestore firestore;

    public ParticipantAdapter(List<Users> list, Context context, String groupId, String grprole) {
        this.list = (ArrayList<Users>) list;
        this.context = context;
        this.groupId = groupId;
        this.grprole = grprole;
    }

    @NonNull
    @Override
    public HolderParticipants onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_participants, parent, false);
        return new HolderParticipants(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderParticipants holder, int position) {
        Users users = list.get(position);
        GroupModel groupModel = new GroupModel();
        firestore = FirebaseFirestore.getInstance();
        String name = users.getUsername();
        String profile = users.getProfilepic();
        String uid = users.getUserid();
        holder.participant.setVisibility(View.GONE);
        holder.creator.setVisibility(View.GONE);
        holder.admin.setVisibility(View.GONE);
        holder.partiuser.setText(name);
        if (profile == null) {
            Glide.with(context).load(R.drawable.user).into(holder.partipic);
        } else {
            Glide.with(context).load(profile).placeholder(R.drawable.user).into(holder.partipic);
        }

        firestore.collection("groups").document(groupId).collection("Participants").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                grprole = "" + value.get("role");
            }
        });

        holder.partipic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserProfile.class);
                intent.putExtra("username", users.getUsername());
                intent.putExtra("profile", users.getProfilepic());
                intent.putExtra("userId", users.getUserid());
                context.startActivity(intent);
            }
        });
        CheckIfAlreadyExists(users, holder);
        holder.PartiLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("groups").document(groupId).collection("Participants").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            previousRole = "" + value.get("role");

                            String[] options;
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(users.getUsername());
                            if (grprole.equals("creator")) {
                                if (previousRole.equals("admin")) {
                                    options = new String[]{"View Profile", "Remove Admin", "Remove Participant", "Chat Privately"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (which == 1) {
                                                removeAdmin(users);
                                                dialog.dismiss();
                                            } else if (which == 2) {
                                                removeParticpant(users);
                                                dialog.dismiss();
                                            }

                                        }
                                    }).show();
                                } else if (previousRole.equals("participant")) {
                                    options = new String[]{"View Profile", "Make Admin", "Remove Participant", "Chat Privately"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (which == 1) {
                                                makeAdmin(users);
                                                dialog.dismiss();
                                            } else if (which == 2) {
                                                removeParticpant(users);
                                                dialog.dismiss();
                                            }

                                        }
                                    }).show();
                                }
                            } else if (grprole.equals("admin")) {
                                if (previousRole.equals("creator")) {
                                    options = new String[]{"View Profile", "Chat Privately"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (which == 0) {

                                            } else if (which == 1) {

                                            }

                                        }
                                    }).show();
                                } else if (previousRole.equals("participant")) {
                                    options = new String[]{"View Profile", "Make Admin", "Remove Participant", "Chat Privately"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 1) {
                                                makeAdmin(users);
                                                dialog.dismiss();
                                            } else if (which == 2) {
                                                removeParticpant(users);
                                                dialog.dismiss();
                                            }

                                        }
                                    }).show();
                                }
                            } else if (grprole.equals("participant")) {
                                options = new String[]{"View Profile", "Chat Privately"};
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }
                        } else {
                            Toast.makeText(context, "removing...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void makeAdmin(Users users) {
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("role", "admin");
        firestore.collection("groups").document(groupId).collection("Participants").document(users.getUserid()).update(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, users.getUsername() + " Is Now Admin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeParticpant(Users users) {
        firestore.collection("groups").document(groupId).collection("Participants").document(users.getUserid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, users.getUsername() + " Is Removed From the Room", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeAdmin(Users users) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "participant");
        firestore.collection("groups").document(groupId).collection("Participants").document(users.getUserid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, users.getUsername() + " is No More An Admin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckIfAlreadyExists(Users users, HolderParticipants holder) {
        firestore.collection("groups").document(groupId).collection("Participants").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    hisRole = "" + value.get("role");
                    if (hisRole.equals("creator")) {
                        holder.creator.setVisibility(View.VISIBLE);
                    } else if (hisRole.equals("participant")) {
                        holder.participant.setVisibility(View.VISIBLE);
                    } else if (hisRole.equals("admin")) {
                        holder.admin.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderParticipants extends RecyclerView.ViewHolder {
        TextView partiuser, admin, participant, creator;
        ImageView partipic, icongrp;

        LinearLayout PartiLinear;

        public HolderParticipants(@NonNull View itemView) {
            super(itemView);
            partipic = itemView.findViewById(R.id.partipic);
            admin = itemView.findViewById(R.id.admin);
            participant = itemView.findViewById(R.id.participants);
            creator = itemView.findViewById(R.id.creator);
            partiuser = itemView.findViewById(R.id.partiuser);
            //icongrp = itemView.findViewById(R.id.icongrp);
            PartiLinear = itemView.findViewById(R.id.PartiLinear);
        }
    }
}