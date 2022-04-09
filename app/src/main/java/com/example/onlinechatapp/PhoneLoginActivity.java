
package com.example.onlinechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinechatapp.databinding.ActivityPhoneLoginBinding;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
    ActivityPhoneLoginBinding binding;
    FirebaseAuth auth;
    String mVerificationId;
    FirebaseFirestore firestore;
    String otp;
    FirebaseUser user;
    int a=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPhoneLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();


        binding.countCode.registerCarrierNumberEditText(binding.editTextPhone);

        binding.otpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.otpBtn.getText().equals("SEND OTP")) {
                    binding.phoneInput.setVisibility(View.GONE);
                    binding.otpValidate.setVisibility(View.VISIBLE);
                    binding.otpBtn.setText("VALIDATE");
                    String mobileno = binding.countCode.getFullNumberWithPlus().replace(" ", "");

                    initiateOtp(mobileno);
                } else {
                    otp = binding.no1.getText().toString() +
                            binding.no2.getText().toString() +
                            binding.no3.getText().toString() +
                            binding.no4.getText().toString() +
                            binding.no5.getText().toString() +
                            binding.no6.getText().toString();
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });
    }

    private void initiateOtp(String mob_no) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(mob_no)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mVerificationId = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(PhoneLoginActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    Toast.makeText(PhoneLoginActivity.this, "SMS Quota Exceeded", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    Users users=new Users();
                    users.setUserid(user.getUid());


                    firestore.collection("Users").document(user.getUid()).collection("username").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value!=null){
                                a=0;
                            }
                        }
                    });

                    firestore.collection("Users").document(user.getUid()).set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if(a==0) {
                                startActivity(new Intent(PhoneLoginActivity.this, usernameactivity.class));
                            }
                            else{
                                startActivity(new Intent(PhoneLoginActivity.this, home.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(PhoneLoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}