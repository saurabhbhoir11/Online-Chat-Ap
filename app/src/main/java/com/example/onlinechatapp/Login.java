package com.example.onlinechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.onlinechatapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();

        /*auth.signInWithEmailAndPassword(binding.email.getText().toString,binding.pass.getText().toString).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(auth.getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(Login.this,MainActivity.class));
                }
                else{
                    Toast.makeText(Login.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        binding.clickToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });


    }
}