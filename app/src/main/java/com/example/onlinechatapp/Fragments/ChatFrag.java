package com.example.onlinechatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinechatapp.Adapter.InboxAdapter;
import com.example.onlinechatapp.databinding.FragmentChatBinding;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatFrag extends Fragment {
    FragmentChatBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    InboxAdapter inboxAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public ChatFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        inboxAdapter= new InboxAdapter(list,getContext());
        binding.inboxlist.setAdapter(inboxAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.inboxlist.setLayoutManager(layoutManager);
        getUserDetails();

        //updateToken(FirebaseInstanceId.getInstance().getToken());

        return binding.getRoot();
    }

    /*private void updateToken(String token){
        Token token1=new Token(token);
        firestore.collection("Tokens").document(auth.getCurrentUser().getUid()).set(token1);
    }*/

    private void getUserDetails() {
        firestore.collection("friends").document(auth.getCurrentUser().getUid()).collection("userid").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                list.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Users users = snapshot.toObject(Users.class);
                    users.setUserid(String.valueOf(snapshot.get("uid")));
                    list.add(users);
                }
                inboxAdapter.notifyDataSetChanged();
            }
        });
    }

}