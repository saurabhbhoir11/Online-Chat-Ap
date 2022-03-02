package com.example.onlinechatapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinechatapp.Adapter.InboxAdapter;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.databinding.FragmentChatBinding;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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


        return binding.getRoot();
    }
}