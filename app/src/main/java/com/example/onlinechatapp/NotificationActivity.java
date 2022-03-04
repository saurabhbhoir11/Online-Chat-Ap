package com.example.onlinechatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.onlinechatapp.Adapter.NotificationAdapter;
import com.example.onlinechatapp.databinding.ActivityNotificationBinding;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        ArrayList<Users> list = new ArrayList<>();


        firestore.collection("notifications").document(auth.getCurrentUser().getUid()).collection("userid").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Users users = snapshot.toObject(Users.class);
                    users.setUserid(snapshot.get("uid").toString());
                    if (snapshot.get("status").equals("0")) {
                        list.add(users);
                    }
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationActivity.this);
                binding.NotificationList.setLayoutManager(layoutManager);
                NotificationAdapter adapter = new NotificationAdapter(list, NotificationActivity.this);
                binding.NotificationList.setAdapter(adapter);
            }
        });
    }
}