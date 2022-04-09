package com.example.onlinechatapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.databinding.ActivityPhotoViewBinding;

public class PhotoView extends AppCompatActivity {
    ActivityPhotoViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        String image= getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(binding.mainimg);
    }
}