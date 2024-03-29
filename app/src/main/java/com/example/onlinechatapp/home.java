package com.example.onlinechatapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.onlinechatapp.Adapter.FragmentsAdapter;
import com.example.onlinechatapp.Notifications.SharedPrefManager;
import com.example.onlinechatapp.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

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

        //Toast.makeText(this,"hello"+ SharedPrefManager.getInstance(this).getToken(), Toast.LENGTH_SHORT).show();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("token",token);
                        FirebaseFirestore.getInstance().collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                String msg = token;
                                Log.d(TAG, msg);
                            }
                        });
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