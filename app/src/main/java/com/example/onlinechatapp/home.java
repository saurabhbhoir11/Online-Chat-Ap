package com.example.onlinechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.onlinechatapp.Adapter.FragmentsAdapter;
import com.example.onlinechatapp.databinding.ActivityHomeBinding;

public class home extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.viewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }
}