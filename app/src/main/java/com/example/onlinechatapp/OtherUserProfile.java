package com.example.onlinechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.databinding.ActivityOtherUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class OtherUserProfile extends AppCompatActivity {
    ActivityOtherUserProfileBinding binding;
    FirebaseUser firebaseuser;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firestore=FirebaseFirestore.getInstance();
        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

        setContentView(binding.getRoot());

        String username = getIntent().getStringExtra("username");
        String profile = getIntent().getStringExtra("profile");
        String bio2 = getIntent().getStringExtra("bio");
        final String receiveId = getIntent().getStringExtra("userId");

        firestore.collection("friends").document(receiveId).collection("userid").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.userFollowers.setText(String.valueOf(value.size()));
            }
        });

        getSupportActionBar().setTitle("User Profile");

        binding.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherUserProfile.this,ChatDetailActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("profile",profile);
                intent.putExtra("userId",receiveId);
                startActivity(intent);
            }
        });


        binding.username.setText(username);
        if (bio2!=null) {
            binding.bio.setText(bio2);
        } else {
            binding.bio.setVisibility(View.GONE);
            binding.bioHead.setVisibility(View.GONE);
        }
        Glide.with(this).load(profile).placeholder(R.drawable.user).into(binding.dp);

    }
}