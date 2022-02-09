package com.example.onlinechatapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinechatapp.Adapter.SearchAdapter;
import com.example.onlinechatapp.databinding.ActivitySearchAcitvityBinding;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class SearchAcitvity extends AppCompatActivity {

    ActivitySearchAcitvityBinding binding;
    FirebaseFirestore mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySearchAcitvityBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        ArrayList<Users> list = new ArrayList<>();

        mUserDatabase = FirebaseFirestore.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchAcitvity.this);
        binding.availableusers.setLayoutManager(layoutManager);
        SearchAdapter adapter = new SearchAdapter(list, SearchAcitvity.this);
        binding.availableusers.setAdapter(adapter);
        binding.searchname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            private void searchUsers(String s) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Query query = mUserDatabase.collection("Users").orderBy("username").startAt(s).endAt(s + "\uf8ff");
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
                            SearchAdapter adapter = new SearchAdapter(list, SearchAcitvity.this);
                            binding.availableusers.setAdapter(adapter);
                        }

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        mUserDatabase.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                list.clear();
                assert value != null;
                for (DocumentSnapshot value2 : value.getDocuments()) {
                    Users users = value2.toObject(Users.class);
                    users.setUserid(value2.getData().toString());
                    if (!users.getUserid().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}