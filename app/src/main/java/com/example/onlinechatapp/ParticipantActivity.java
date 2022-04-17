package com.example.onlinechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.Adapter.ParticipantAdapter;
import com.example.onlinechatapp.databinding.ActivityParticipantBinding;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivity extends AppCompatActivity {

    ActivityParticipantBinding binding;
    String groupId, grprole = "";
    FirebaseAuth auth;
    ArrayList<Users> list = new ArrayList<>();
    private List<String> allList;
    ParticipantAdapter adapter;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityParticipantBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        allList = new ArrayList<>();
        groupId = getIntent().getStringExtra("groupId");
        loadGroupInfo();
        getParticipants();

        if (grprole.equals("participant")) {
            binding.floatingActionButton3.setVisibility(View.GONE);
        } else {
            binding.floatingActionButton3.setVisibility(View.VISIBLE);
        }

        adapter = new ParticipantAdapter(list, ParticipantActivity.this, groupId, grprole);
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);

        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantActivity.this, AddParticipantActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });
    }

    private void getParticipants() {
        Toast.makeText(this, ""+groupId, Toast.LENGTH_SHORT).show();
        firestore.collection("groups").document(groupId).collection("Participants").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allList.clear();
                for (DocumentSnapshot snapshot1 : value.getDocuments()) {
                    allList.add(snapshot1.getId());
                }
                showUsers();
            }
        });
    }

    private void showUsers() {
        firestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                list.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Users users = snapshot.toObject(Users.class);
                    for (String id : allList) {
                        if (users.getUserid().equals(id)) {
                            list.add(users);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadGroupInfo() {
        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String groupId = "" + snapshot1.child("groupId").getValue();
                    String groupTitle = "" + snapshot1.child("groupTitle").getValue();
                    String groupDesc = "" + snapshot1.child("groupDesc").getValue();
                    String groupIcon = "" + snapshot1.child("groupIcon").getValue();
                    String timestamp = "" + snapshot1.child("timestamp").getValue();
                    String createdBy = "" + snapshot1.child("createdBy").getValue();

                    binding.collapsebar.setTitle(groupTitle);
                    binding.collapsebar.setSubtitle(groupDesc);
                    binding.collapsebar.setExpandedTitleTextColor(getColor(R.color.white));
                    binding.collapsebar.setExpandedSubtitleTextColor(getColor(R.color.white));
                    binding.collapsebar.setCollapsedSubtitleTextColor(getColor(R.color.white));
                    binding.collapsebar.setCollapsedTitleTextColor(getColor(R.color.white));
                    Glide.with(ParticipantsActivity.this).load(groupIcon).placeholder(R.drawable.finalgroup).into(binding.icongrp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("groups");
        ref2.child(groupId).child("Participants").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    grprole = "" + snapshot.child("role").getValue();
                    if(grprole.equals("participant")){
                        binding.floatingActionButton3.setVisibility(View.GONE);
                    }
                    else {
                        binding.floatingActionButton3.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}