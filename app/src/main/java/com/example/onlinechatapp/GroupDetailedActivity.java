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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
        binding = ActivityGroupDetailedBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        a = 1;
        /*binding.sender2.setImageResource(R.drawable.search_buttton);
        senderId = auth.getUid();
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        allList = new ArrayList<>();
        allhavelist = new ArrayList<>();
        allhavenotlist = new ArrayList<>();
        getParticipants();
        adapter2 = new ChatRoomAdapter(list, GroupDetailedActivity.this);
        binding.RoomRecycle.setAdapter(adapter2);

        namesAdapter = new NamesAdapter(list, GroupDetailedActivity.this);
        binding.namecycle.setAdapter(namesAdapter);

        binding.ChatRecycle2.onScrolled(0,100);
        binding.sender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.sendmsg2.toString();
                sendMessage(msg);
            }
        });
        binding.linearLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GroupDetailedActivity.this, Partici);
            }
        });*/
    }

    private void getParticipants() {
        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId);
        ref.child("Participants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    allList.add(snapshot1.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    private void showUsers() {
        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users users = snapshot1.getValue(Users.class);
                    for (String id : allList) {
                        if (users.getUserid().equals(id)) {
                            list.add(users);
                        }
                    }
                }
                adapter2.notifyDataSetChanged();
                namesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void HaveNotShow() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lists2.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users users = snapshot1.getValue(Users.class);
                    for (String id1 : allhavenotlist) {
                        if (users.getUserid().equals(id1)) {
                            lists2.add(users);
                        }
                    }
                }
                adapterhavenot.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void HaveShow() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lists.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users users = snapshot1.getValue(Users.class);
                    for (String id2 : allhavelist) {
                        if (users.getUserid().equals(id2)) {
                            lists.add(users);
                        }
                    }
                }
                adapterhavenot.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    private void loadgrpmsg() {
       /* groupChatList = new ArrayList<>();
        adapter = new GroupChatAdapter(groupChatList, GroupDetailActivity.this);
        binding.ChatRecycle2.setAdapter(adapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.child(groupId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    GroupChat grpmodel = snapshot1.getValue(GroupChat.class);
                    groupChatList.add(grpmodel);
                    binding.ChatRecycle2.smoothScrollToPosition(binding.ChatRecycle2.getAdapter().getItemCount());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendMessage(String msg) {
        String time = "" + System.currentTimeMillis();
        GroupChat grpmodel = new GroupChat(auth.getCurrentUser().getUid(), msg, time);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.child(groupId).child("Messages").child(time).setValue(grpmodel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                binding.sendmsg2.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupDetailActivity.this, "Message Failed To Send", Toast.LENGTH_SHORT).show();
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String groupTitle = "" + snapshot1.child("groupTitle").getValue();
                    String groupDesc = "" + snapshot1.child("groupDesc").getValue();
                    String groupIcon = "" + snapshot1.child("groupIcon").getValue();
                    String timestamp = "" + snapshot1.child("timestamp").getValue();
                    String createdBy = "" + snapshot1.child("createdBy").getValue();

                    binding.groupTitle.setText(groupTitle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
                                        String msg = binding.sendmsg2.getText().toString();
                                        String time = "" + System.currentTimeMillis();
                                        GroupChat model = new GroupChat(auth.getCurrentUser().getUid(), msg, time);
                                        model.setMsg("*Photo*");
                                        model.setImageUrl(filepath);
                                        binding.sendmsg2.setText("");


                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
                                        ref.child(groupId).child("Messages").child(time).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                binding.sendmsg2.setText("");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GroupDetailActivity.this, "Message Failed To Send", Toast.LENGTH_SHORT).show();
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
                                        String msg = binding.sendmsg2.getText().toString();
                                        String time = "" + System.currentTimeMillis();
                                        GroupChat model = new GroupChat(auth.getCurrentUser().getUid(), msg, time);
                                        model.setMsg("*Video*");
                                        model.setVideoUrl(filepath);
                                        binding.sendmsg2.setText("");
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
                                        ref.child(groupId).child("Messages").child(time).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                binding.sendmsg2.setText("");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GroupDetailActivity.this, "Message Failed To Send", Toast.LENGTH_SHORT).show();
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
        else if (requestCode == 90) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri selectaudio = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = firebaseStorage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    reference.putFile(selectaudio).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filepath = uri.toString();
                                        String msg = binding.sendmsg2.getText().toString();
                                        String time = "" + System.currentTimeMillis();
                                        GroupChat model = new GroupChat(auth.getCurrentUser().getUid(), msg, time);
                                        model.setMsg("*Audio*");
                                        model.setAudioUrl(filepath);

                                        binding.sendmsg2.setText("");
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
                                        ref.child(groupId).child("Messages").child(time).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                binding.sendmsg2.setText("");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(GroupDetailActivity.this, "Message Failed To Send", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String Rather = "" + snapshot1.child("rather").getValue();
                    if (Rather.equals("started")&& Role.equals("creator")) {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("rather","ends");
                        database.getReference("groups").child(groupId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                    }
                    else {
                        Toast.makeText(GroupDetailActivity.this, "Failed To Join", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("groups");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String Rather = "" + snapshot1.child("rather").getValue();
                    if (Rather.equals("started")&& Role.equals("creator")) {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("rather","ends");
                        database.getReference("groups").child(groupId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });

                    }
                    else {
                        Toast.makeText(GroupDetailActivity.this, "Failed To Join", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }*/
    }
}