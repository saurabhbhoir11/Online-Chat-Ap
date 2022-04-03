package com.example.onlinechatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.onlinechatapp.Adapter.ChatAdapter;
import com.example.onlinechatapp.Notifications.MyFirebaseMessagingService;
import com.example.onlinechatapp.databinding.ActivityChatDetailBinding;
import com.example.onlinechatapp.models.Message_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ramotion.circlemenu.CircleMenuView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ChatAdapter chatAdapter;
    FirebaseStorage firebaseStorage;

    String senderId;
    String receiverId;
    String username;
    String profile_pic;
    String SenderRoom, ReceiverRoom;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

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

        binding.fUsername.setText(username);
        Glide.with(this).load(profile_pic).override(70, 70).placeholder(R.drawable.user).into(binding.dP);

        binding.menuOps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.circleMenu.setVisibility(View.VISIBLE);

                CountDownTimer countDownTimer = new CountDownTimer(10, 10) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        binding.circleMenu.open(true);
                    }
                }.start();
            }
        });

        binding.circleMenu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationStart(view, buttonIndex);
                buttonClicked(buttonIndex);
                binding.circleMenu.setVisibility(View.GONE);
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                super.onMenuCloseAnimationEnd(view);
                binding.circleMenu.setVisibility(View.GONE);
            }
        });


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
                notify = true;
                String msg = String.valueOf(binding.userMessage.getText());
                Message_Model message_model = new Message_Model(msg, senderId);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                String time = simpleDateFormat.format(calendar.getTime());
                String timestamp = String.valueOf(System.currentTimeMillis());
                message_model.setTimestamp(timestamp);
                message_model.setTime(time);

                FirebaseFirestore.getInstance().collection("Users").document(receiverId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String token = String.valueOf(value.get("token"));
                        MyFirebaseMessagingService.senPushNotification(msg, "username", token);
                    }
                });

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
                                notify = false;
                            }
                        });
                    }
                });
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
                                        String msg = binding.userMessage.getText().toString();
                                        final Message_Model model = new Message_Model(msg, senderId);
                                        Calendar ctime = Calendar.getInstance();
                                        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm:aa");
                                        final String savetime = currenttime.format(ctime.getTime());
                                        String timestamp = String.valueOf(System.currentTimeMillis());

                                        model.setTimestamp(timestamp);
                                        model.setMsg("$2y$10$39cSefzbHNYvvwTmQpmN2OTZ7jfX.vWd7QeSqgs9pRRWKU7zF7txm");
                                        model.setTime(savetime);
                                        model.setImageUrl(filepath);
                                        binding.userMessage.setText("");

                                        firestore.collection("chats").document(SenderRoom).collection(SenderRoom).add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                firestore.collection("chats").document(ReceiverRoom).collection(ReceiverRoom).add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        chatAdapter.notifyDataSetChanged();
                                                    }
                                                });
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
                    StorageReference ref = firebaseStorage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    ref.putFile(selectvideo).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filepath = uri.toString();
                                        String msg = binding.userMessage.getText().toString();
                                        final Message_Model model = new Message_Model(msg, senderId);
                                        Calendar ctime = Calendar.getInstance();
                                        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm:aa");
                                        final String savetime = currenttime.format(ctime.getTime());
                                        String timestamp = String.valueOf(System.currentTimeMillis());

                                        model.setTimestamp(timestamp);
                                        model.setMsg("$2y$10$4S0nmurvLkIkLbjnUZMrOu/IWViv87UzRB2v5hcBVzbGDUkw.3D..");
                                        model.setTime(savetime);
                                        model.setImageUrl(filepath);
                                        binding.userMessage.setText("");

                                        firestore.collection("chats").document(SenderRoom).collection(SenderRoom).add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                firestore.collection("chats").document(ReceiverRoom).collection(ReceiverRoom).add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        chatAdapter.notifyDataSetChanged();
                                                    }
                                                });
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