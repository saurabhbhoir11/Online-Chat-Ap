package com.example.onlinechatapp;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinechatapp.Adapter.ChatRoomAdapter;
import com.example.onlinechatapp.Adapter.GroupChatAdapter;
import com.example.onlinechatapp.Adapter.NamesAdapter;
import com.example.onlinechatapp.databinding.ActivityGroupDetailedBinding;
import com.example.onlinechatapp.models.GroupChat;
import com.example.onlinechatapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailedActivity extends AppCompatActivity {


    ActivityGroupDetailedBinding binding;
    String groupId,GroupDesc,GroupIcon;
    FirebaseAuth auth;
    FirebaseFirestore database;
    FirebaseStorage firebaseStorage;
    String senderId;
    GroupChatAdapter adapter;

    int a, stickindex;
    ArrayList<Users> list = new ArrayList<>();
    ArrayList<Users> lists = new ArrayList<>();
    ArrayList<Users> lists2 = new ArrayList<>();
    private List<String> allList;
    private List<String> allhavelist;
    private List<String> allhavenotlist;
    ChatRoomAdapter adapter2;
    NamesAdapter namesAdapter;
    float count1, count2, totalcount, countskip, perc1, perc2;
    private ArrayList<GroupChat> groupChatList;
    String Role;

    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detailed);
    }
}