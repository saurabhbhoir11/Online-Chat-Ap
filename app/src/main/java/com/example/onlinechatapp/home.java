package com.example.onlinechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.onlinechatapp.Adapter.FragmentsAdapter;
import com.example.onlinechatapp.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {
    ActivityHomeBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        getSupportActionBar().setElevation(0);

        binding.floatingActionButton3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                auth.signOut();
                startActivity(new Intent(home.this,Login.class));
                return false;
            }
        });

        binding.viewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        binding.tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        binding.tabLayout.setTabTextColors(Color.rgb(235, 250, 250), Color.WHITE);

        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, SearchAcitvity.class));
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.noti:
                startActivity(new Intent(home.this,NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}