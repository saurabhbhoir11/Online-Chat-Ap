package com.example.onlinechatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.Adapter.ChatAdapter;
import com.example.onlinechatapp.databinding.ActivityChatDetailBinding;
import com.example.onlinechatapp.models.Message_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ChatAdapter chatAdapter;

    String senderId;
    String receiverId;
    String username;
    String profile_pic;
    String SenderRoom, ReceiverRoom;

    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        senderId = auth.getCurrentUser().getUid();
        receiverId = getIntent().getStringExtra("userId");

        username = getIntent().getStringExtra("username");
        profile_pic = getIntent().getStringExtra("profile");

        binding.fUsername.setText(username);

        Glide.with(this).load(profile_pic).override(100, 100).placeholder(R.drawable.user).into(binding.dP);

        SenderRoom = senderId + receiverId;
        ReceiverRoom = receiverId + senderId;


        final ArrayList<Message_Model> message_models = new ArrayList<>();
        chatAdapter = new ChatAdapter(message_models, ChatDetailActivity.this);
        binding.chatlist.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatDetailActivity.this);
        binding.chatlist.setLayoutManager(layoutManager);


        firestore.collection("chats").document(SenderRoom).collection(SenderRoom).orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                message_models.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Message_Model model = snapshot.toObject(Message_Model.class);
                    message_models.add(model);
                    binding.chatlist.smoothScrollToPosition(binding.chatlist.getAdapter().getItemCount());
                }
                chatAdapter.notifyDataSetChanged();
            }
        });


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify=true;
                String msg = String.valueOf(binding.userMessage.getText());
                Message_Model message_model = new Message_Model(msg, senderId);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                String time = simpleDateFormat.format(calendar.getTime());
                String timestamp = String.valueOf(System.currentTimeMillis());
                message_model.setTimestamp(timestamp);
                message_model.setTime(time);

                binding.userMessage.setText("");

                firestore.collection("friends").document(auth.getCurrentUser().getUid()).collection("userid").document(receiverId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.exists()) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("uid", senderId);
                            hashMap.put("status", "0");

                            firestore.collection("notifications").document(receiverId).collection("userid").document(senderId).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ChatDetailActivity.this, "Chat Request Sent Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                firestore.collection("chats").document(SenderRoom).collection(SenderRoom).add(message_model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        firestore.collection("chats").document(ReceiverRoom).collection(ReceiverRoom).add(message_model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                chatAdapter.notifyDataSetChanged();
                                notify=false;
                            }
                        });
                    }
                });
            }
        });
    }
}