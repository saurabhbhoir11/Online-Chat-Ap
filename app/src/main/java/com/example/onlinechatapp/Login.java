package com.example.onlinechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        if (auth.getCurrentUser() != null) {

            if(!auth.getCurrentUser().isEmailVerified()&&auth.getCurrentUser().getPhoneNumber()==null) {
                Toast.makeText(this, "Please Verify Email First", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(Login.this, home.class);
                startActivity(intent);
                finish();
            }
        }


        binding.phoneLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,PhoneLoginActivity.class));
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signInWithEmailAndPassword(binding.email1.getText().toString(),binding.pass1.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(Login.this,home.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.clickToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });


    }
}