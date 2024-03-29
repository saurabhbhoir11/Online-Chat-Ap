package com.example.onlinechatapp.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.EditProfileActivity;
import com.example.onlinechatapp.Login;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileFrag extends Fragment {
    FragmentProfileBinding binding;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;
    FirebaseAuth auth;


    public ProfileFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        firebaseStorage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Logging Out...");
                CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        pd.show();
                    }

                    @Override
                    public void onFinish() {
                        pd.dismiss();
                        Intent intent = new Intent(getContext(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }.start();

            }
        });

        firestore.collection("friends").document(auth.getCurrentUser().getUid()).collection("userid").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.userFollowers.setText(String.valueOf(value.size()));
            }
        });

        firestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Glide.with(getContext()).load("" + value.get("profilepic")).centerCrop().placeholder(R.drawable.user).into(binding.profileImage);
                binding.profileName.setText("" + value.get("username"));
                if (String.valueOf(value.get("Bio")).equals("null")) {
                    binding.userBio.setText("Write a bio to let others know about you");
                    binding.userBio.setTextColor(Color.GRAY);
                } else {
                    binding.userBio.setText("" + value.get("Bio"));
                    binding.userBio.setTextColor(Color.BLACK);
                }

                binding.editProf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), EditProfileActivity.class);
                        intent.putExtra("username", String.valueOf(value.get("username")));
                        startActivity(intent);
                    }
                });
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 33) {
            if (data.getData() != null) {
                Uri sfile = data.getData();
                binding.profileImage.setImageURI(sfile);

                final StorageReference reference = firebaseStorage.getReference("profilepic")
                        .child(auth.getCurrentUser().getUid());

                reference.putFile(sfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("profilepic", uri.toString());

                                firestore.collection("Users").document(auth.getCurrentUser().getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Profile Picture Set Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Something Went Wrong, Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}