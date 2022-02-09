package com.example.onlinechatapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinechatapp.databinding.ActivitySearchAcitvityBinding;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchAcitvity extends AppCompatActivity {

    ActivitySearchAcitvityBinding binding;
    FirebaseFirestore  mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= ActivitySearchAcitvityBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        ArrayList<Users>  list = new ArrayList<>();

        mUserDatabase = FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchAcitvity.this);
        binding.availableusers.setLayoutManager(layoutManager);
    }
}