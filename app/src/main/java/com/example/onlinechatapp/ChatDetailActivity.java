package com.example.onlinechatapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate;
import com.bumptech.glide.Glide;
import com.example.onlinechatapp.Adapter.ChatAdapter;
import com.example.onlinechatapp.Notifications.MyFirebaseMessagingService;
import com.example.onlinechatapp.databinding.ActivityChatDetailBinding;
import com.example.onlinechatapp.models.Message_Model;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

public class ChatDetailActivity extends AppCompatActivity implements ScreenshotDetectionDelegate.ScreenshotDetectionListener {
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
    FusedLocationProviderClient mFusedLocationClient;

    int screenshot=0;

    String myusername;
    private static final int PERMISSION_ID = 40;
    int a = 1, b = 0;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009;

    boolean notify = false;
    private ScreenshotDetectionDelegate screenshotDetectionDelegate = new ScreenshotDetectionDelegate(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        getSupportActionBar().hide();

        checkReadExternalStoragePermission();

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

                firestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        myusername = String.valueOf(value.get("username"));
                    }
                });

                FirebaseFirestore.getInstance().collection("Users").document(receiverId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String token = String.valueOf(value.get("token"));
                        MyFirebaseMessagingService.senPushNotification(msg, myusername, token, time);
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
        } else if (index == 1) {
            Intent video = new Intent();
            video.setAction(Intent.ACTION_GET_CONTENT);
            video.setType("video/*");
            startActivityForResult(video, 30);
        } else if (index == 4) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ChatDetailActivity.this);
            getCurrentLocation();
        }
    }


    private void getCurrentLocation() {
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(ChatDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChatDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null && b == 0) {
                            finish();
                            startActivity(getIntent());
                        } else {

                            Toast.makeText(ChatDetailActivity.this, "Location Sent", Toast.LENGTH_SHORT).show();
                            String msg = binding.userMessage.getText().toString();
                            final Message_Model model = new Message_Model(msg, senderId);
                            Calendar ctime = Calendar.getInstance();
                            SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm:aa");
                            final String savetime = currenttime.format(ctime.getTime());
                            String timestamp = String.valueOf(System.currentTimeMillis());

                            model.setTimestamp(timestamp);
                            model.setMsg("$ncw$&nwcbwcwjdd!@cnwkcScwxj#5cjwc9qw8dw5cn");
                            model.setTime(savetime);
                            model.setLat(String.valueOf(location.getLatitude()));
                            model.setLon(String.valueOf(location.getLongitude()));
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

                            b = 1;
                        }
                    }
                });
            } else {
                Toast.makeText(ChatDetailActivity.this, "Please Turn On Your Location", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(ChatDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChatDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(ChatDetailActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Please give access to External Storage", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChatDetailActivity.this,home.class));
                }
                break;
            case PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    protected void onStart() {
        super.onStart();
        screenshotDetectionDelegate.startScreenshotDetection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        screenshotDetectionDelegate.stopScreenshotDetection();
    }

    @Override
    public void onScreenCaptured(String path) {
        if(screenshot==0) {
            Toast.makeText(this, "ScreenShot Was Captured", Toast.LENGTH_SHORT).show();
            String msg = binding.userMessage.getText().toString();
            final Message_Model model = new Message_Model(msg, senderId);
            Calendar ctime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm:aa");
            final String savetime = currenttime.format(ctime.getTime());
            String timestamp = String.valueOf(System.currentTimeMillis());

            model.setTimestamp(timestamp);
            model.setMsg("Chat screenshot was taken!!!");
            model.setTime(savetime);
            binding.userMessage.setText("");

            firestore.collection("chats").document(SenderRoom).collection(SenderRoom).add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    firestore.collection("chats").document(ReceiverRoom).collection(ReceiverRoom).add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            chatAdapter.notifyDataSetChanged();
                            screenshot=1;
                        }
                    });
                }
            });
        }

    }

    @Override
    public void onScreenCapturedWithDeniedPermission() {
        Toast.makeText(this, "NO SCREEN SHOT CAPTURED", Toast.LENGTH_SHORT).show();
    }

    private void checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission();
        }
    }

    private void requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
    }

}