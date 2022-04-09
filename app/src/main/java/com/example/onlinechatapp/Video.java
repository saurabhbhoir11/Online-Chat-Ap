package com.example.onlinechatapp;

import android.os.Bundle;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinechatapp.databinding.ActivityVideoBinding;

public class Video extends AppCompatActivity {
    ActivityVideoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        String video = getIntent().getStringExtra("video");
        binding.videoView.setVideoPath(video);
        MediaController mediaController=new MediaController(Video.this);
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.start();
    }
}