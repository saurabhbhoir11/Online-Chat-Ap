package com.example.onlinechatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.Adapter.ChatRoomAdapter;
import com.example.onlinechatapp.Adapter.GroupChatAdapter;
import com.example.onlinechatapp.Adapter.NamesAdapter;
import com.example.onlinechatapp.databinding.ActivityGroupDetailedBinding;
import com.example.onlinechatapp.models.GroupChat;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GroupDetailedActivity extends AppCompatActivity {


    ActivityGroupDetailedBinding binding;
    String groupId, GroupDesc, GroupIcon;
    FirebaseAuth auth;
    FirebaseFirestore database;
    FirebaseStorage firebaseStorage;
    String senderId;
    GroupChatAdapter adapter;

    int a;
    ArrayList<Users> list = new ArrayList<>();
    ArrayList<String> allList = new ArrayList<>();

    ChatRoomAdapter adapter2;
    NamesAdapter namesAdapter;
    float count1, count2, totalcount, countskip, perc1, perc2;
    private ArrayList<GroupChat> groupChatList;
    String Role;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityGroupDetailedBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        a = 1;
        senderId = auth.getUid();
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        allList = new ArrayList<>();
        getParticipants();

        loadGroupInfo();
        loadgrpmsg();

        namesAdapter = new NamesAdapter(list, GroupDetailedActivity.this);
        binding.nameCycle.setAdapter(namesAdapter);

        binding.chatRecycle.onScrolled(0, 100);
        binding.sendBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.sendMsg2.getText().toString();
                sendMessage(msg);
            }
        });
        binding.linearTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(GroupDetailedActivity.this, ParticipantActivity.class);
                intent1.putExtra("groupId",groupId);
                startActivity(intent1);

            }
        });
    }

    private void getParticipants() {
        database.collection("groups").document(groupId).collection("Participants").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allList.clear();
                for (DocumentSnapshot snapshot1 : value.getDocuments()) {
                    allList.add(snapshot1.getId());
                }
                showUsers();
            }
        });
    }

    private void showUsers() {
        database.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Users users = snapshot.toObject(Users.class);
                    for (String id : allList) {
                        if (users.getUserid().equals(id)) {
                            list.add(users);
                        }
                    }
                }
            }
        });

        //adapter2.notifyDataSetChanged();
        namesAdapter.notifyDataSetChanged();

    }

    private void loadgrpmsg() {
        groupChatList = new ArrayList<>();
        adapter = new GroupChatAdapter(groupChatList, GroupDetailedActivity.this);
        binding.chatRecycle.setAdapter(adapter);
        database.collection("groups").document(groupId).collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                groupChatList.clear();
                for (DocumentSnapshot snapshot1 : value.getDocuments()) {
                    GroupChat grpmodel = snapshot1.toObject(GroupChat.class);
                    groupChatList.add(grpmodel);
                    binding.chatRecycle.smoothScrollToPosition(binding.chatRecycle.getAdapter().getItemCount());
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void sendMessage(String msg) {
        String time = "" + System.currentTimeMillis();
        GroupChat grpmodel = new GroupChat(auth.getCurrentUser().getUid(), msg, time);

        database.collection("groups").document(groupId).collection("Messages").document(time).set(grpmodel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                binding.sendMsg2.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupDetailedActivity.this, "Message Failed To Send", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buttonClicked(int index) {
        Toast.makeText(this, "Clicked" + index, Toast.LENGTH_SHORT).show();
        if (index == 0) {
            Intent image = new Intent();
            image.setAction(Intent.ACTION_GET_CONTENT);
            image.setType("image/*");
            startActivityForResult(image, 20);
        }
        if (index == 1) {
            Intent video = new Intent();
            video.setAction(Intent.ACTION_GET_CONTENT);
            video.setType("video/*");
            startActivityForResult(video, 30);
        }
        if (index == 4) {
            Intent cont = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(cont, 40);
        }
        if (index == 2) {
            Intent aud = new Intent();
            aud.setAction(Intent.ACTION_GET_CONTENT);
            aud.setType("audio/*");
            startActivityForResult(aud, 90);
        }
        if (index == 3) {

        }
    }

    private void loadGroupInfo() {
        Toast.makeText(this, ""+groupId, Toast.LENGTH_SHORT).show();
        database.collection("groups").document(groupId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String groupTitle = "" + value.get("groupTitle");
                String groupDesc = "" + value.get("groupDesc");
                String groupIcon = "" + value.get("groupIcon");
                String timestamp = "" + value.get("timestamp");
               // String createdBy = "" + value.get("createdBy").toString();

                binding.groupTitle.setText(groupTitle);
                Glide.with(GroupDetailedActivity.this).load(groupIcon).placeholder(R.drawable.user).into(binding.groupIcon);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri selectimage = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference ref = firebaseStorage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    ref.putFile(selectimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filepath = uri.toString();
                                        String msg = binding.sendMsg2.getText().toString();
                                        String time = "" + System.currentTimeMillis();
                                        GroupChat model = new GroupChat(auth.getCurrentUser().getUid(), msg, time);
                                        model.setMsg("*Photo*");
                                        model.setImageUrl(filepath);
                                        binding.sendMsg2.setText("");


                                        database.collection("groups").document(groupId).collection("Messages").document(time).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                binding.sendMsg2.setText("");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GroupDetailedActivity.this, "Message Failed To Send", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        } else if (requestCode == 30) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri selectvideo = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = firebaseStorage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    reference.putFile(selectvideo).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filepath = uri.toString();
                                        String msg = binding.sendMsg2.getText().toString();
                                        String time = "" + System.currentTimeMillis();
                                        GroupChat model = new GroupChat(auth.getCurrentUser().getUid(), msg, time);
                                        model.setMsg("*Video*");
                                        model.setVideoUrl(filepath);
                                        binding.sendMsg2.setText("");
                                        database.collection("groups").document(groupId).collection("Messages").document(time).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                binding.sendMsg2.setText("");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GroupDetailedActivity.this, "Message Failed To Send", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }

    }

}