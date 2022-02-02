package com.example.onlinechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.onlinechatapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.sendEmailVerification();
                            Toast.makeText(Register.this, "Verification Link sent to "+binding.email.getText() , Toast.LENGTH_SHORT).show();
                            user = auth.getCurrentUser();

                            startActivity(new Intent(Register.this, MainActivity.class));

                        }
                    }
                });
            }
        });


        binding.clickToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });
    }
}