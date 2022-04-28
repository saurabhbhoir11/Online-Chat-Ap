package com.example.onlinechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.onlinechatapp.databinding.ActivitySplashScreenBinding;

import java.util.Random;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.loadingBar.playAnimation();
        binding.loadingBar.loop(true);

        CountDownTimer countDownTimer=new CountDownTimer(3000,3000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreenActivity.this,Login.class));
                finish();
            }
        }.start();
    }
}