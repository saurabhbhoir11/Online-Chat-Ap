package com.example.onlinechatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.onlinechatapp.databinding.ActivityUsernameactivityBinding;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class usernameactivity extends AppCompatActivity {
    ActivityUsernameactivityBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityUsernameactivityBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        //getSupportActionBar().hide();

        binding.contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.username.getText().toString().isEmpty()){
                    Query usernameQuery = firestore.collection("Users").whereEqualTo("username",binding.username.getText().toString());
                    usernameQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for(DocumentSnapshot ds: value){
                                binding.progressBar.setVisibility(View.VISIBLE);
                                if(ds.exists()){
                                    binding.errorMsg.setVisibility(View.VISIBLE);
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.tickImg.setVisibility(View.GONE);
                                }
                                else{
                                    binding.errorMsg.setVisibility(View.GONE);
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.tickImg.setVisibility(View.VISIBLE);

                                    HashMap<String,Object> hashMap=new HashMap<>();
                                    hashMap.put("username",binding.username.getText().toString());

                                    firestore.collection("Users").document(auth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            startActivity(new Intent(usernameactivity.this,MainActivity.class));
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                else{
                    binding.username.setError("Username Cannot Be Empty");
                }
            }
        });
    }
}