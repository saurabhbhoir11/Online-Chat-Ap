package com.example.onlinechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.onlinechatapp.Adapter.FragmentsAdapter;
import com.example.onlinechatapp.databinding.ActivityHomeBinding;

public class home extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().setElevation(0);


        binding.viewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        binding.tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        binding.tabLayout.setTabTextColors(Color.rgb(235, 250, 250),Color.WHITE);

        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,SearchAcitvity.class));
            }
        });
    }
}