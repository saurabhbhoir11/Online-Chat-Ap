package com.example.onlinechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.onlinechatapp.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();


        auth.createUserWithEmailAndPassword(binding.email.getText().toString(),binding.pass.getText().toString());


    }
}