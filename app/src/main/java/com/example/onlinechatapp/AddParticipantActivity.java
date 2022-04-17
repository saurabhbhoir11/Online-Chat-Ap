package com.example.onlinechatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.onlinechatapp.Adapter.AddPartiAdapter;
import com.example.onlinechatapp.Adapter.SearchAdapter;
import com.example.onlinechatapp.databinding.ActivityAddParticipantBinding;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddParticipantActivity extends AppCompatActivity {
    ActivityAddParticipantBinding binding;
    FirebaseFirestore database;
    String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddParticipantBinding.inflate(getLayoutInflater());
        groupId = getIntent().getStringExtra("groupId");
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        ArrayList<Users> list = new ArrayList<>();

        database = FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(AddParticipantActivity.this);
        binding.addParti.setLayoutManager(layoutManager);
        AddPartiAdapter adapter = new AddPartiAdapter(list, AddParticipantActivity.this, groupId);
        binding.addParti.setAdapter(adapter);

        binding.seacrhppl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString());
            }

            private void searchUsers(String s) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Query query = database.collection("Users").orderBy("username").startAt(s).endAt(s + "\uf8ff");
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        assert value != null;
                        for (DocumentSnapshot value1 : value.getDocuments()) {
                            Users users = value1.toObject(Users.class);

                            assert users != null;
                            assert firebaseUser != null;

                            if (!users.getUserid().equals(firebaseUser.getUid())) {
                                list.add(users);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        database.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                list.clear();
                for (DocumentSnapshot dataSnapshot : value.getDocuments()) {
                    Users users = dataSnapshot.toObject(Users.class);
                    users.setUserid(dataSnapshot.getId());
                    if (!users.getUserid().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(users);
                    }

                }
                adapter.notifyDataSetChanged();
            }
        });

        return;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}