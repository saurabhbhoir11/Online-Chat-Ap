package com.example.onlinechatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String username="",phone="",bio="",dob="",profile="";
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };


            binding.date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!binding.editDob.getText().toString().equals("")){
                        binding.date.setClickable(false);
                        Toast.makeText(EditProfileActivity.this, "You cannot change DOB", Toast.LENGTH_SHORT).show();
                    }
                    else
                    new DatePickerDialog(EditProfileActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });



        ShowCurrentInfo();
        binding.saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.editUsername.getText().toString().equals(username)){

                }
                if(!binding.editBio.getText().toString().equals(bio)){
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("Bio",binding.editBio.getText().toString());
                    firestore.collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfileActivity.this, "Edits Saved - Bio", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(!binding.editPhone.getText().toString().equals(phone)){
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("PhoneNumber",binding.editPhone.getText().toString());
                    firestore.collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfileActivity.this, "Edits Saved - Phone", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(!binding.editDob.getText().toString().equals(dob)){
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("DOB",binding.editDob.getText().toString());
                    firestore.collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfileActivity.this, "Edits Saved - DOB", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                startActivity(new Intent(EditProfileActivity.this,home.class));
            }
        });
    }

    private void updateLabel() {
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.getDefault());
        binding.editDob.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void ShowCurrentInfo() {
        firestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.editUsername.setText(""+value.get("username"));
                username=""+value.get("username");
                if(value.get("PhoneNumber")!=null){
                    binding.editPhone.setText(""+value.get("PhoneNumber"));
                    phone=""+value.get("PhoneNumber");
                }
                if(value.get("Bio")!=null){
                    binding.editBio.setText(""+value.get("Bio"));
                    bio=""+value.get("Bio");
                }
                if(value.get("DOB")!=null){
                    binding.editDob.setText(""+value.get("DOB"));
                    dob=""+value.get("DOB");
                }
                if(value.get("profilepic")!=null){
                    Glide.with(EditProfileActivity.this).load(""+value.get("profilepic")).into(binding.editProf);
                    profile=""+value.get("profilepic");
                }
            }
        });
    }

}