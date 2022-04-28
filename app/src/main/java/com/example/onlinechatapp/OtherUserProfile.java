package com.example.onlinechatapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.databinding.ActivityOtherUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class OtherUserProfile extends AppCompatActivity {
    ActivityOtherUserProfileBinding binding;
    FirebaseFirestore database;
    FirebaseUser firebaseuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        binding = ActivityOtherUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

        setContentView(binding.getRoot());

        String username = getIntent().getStringExtra("username");
        String profile = getIntent().getStringExtra("profile");
        String bio2 = getIntent().getStringExtra("bio");
        final String receiveId = getIntent().getStringExtra("userid");

        getSupportActionBar().setTitle(username);

        binding.username.setText(username);
        if(!bio2.equals("null")) {
            binding.bio.setText(bio2);
        }
        else {
            binding.bio.setVisibility(View.GONE);
            binding.bioHead.setVisibility(View.GONE);
        }
        Glide.with(this).load(profile).placeholder(R.drawable.user).into(binding.dp);

    }
}