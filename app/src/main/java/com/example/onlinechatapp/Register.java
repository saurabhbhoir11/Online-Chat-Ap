package com.example.onlinechatapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinechatapp.databinding.ActivityRegisterBinding;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    int a = 1;
    int b=0;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private GoogleSignInClient mGoogleSigninClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //Phone SignIn
        binding.phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, PhoneLoginActivity.class));
            }
        });

        //Google-SignIn
        createRequest();


        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //SignIn with Email and Password
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.email.getText().toString().isEmpty()) {
                    binding.email.setError("Please enter an email");
                    return;
                } else if (binding.password.getText().length() < 6) {
                    binding.password.setError("The Password must be at least 6 characters");
                    return;
                } else if (!binding.password.getText().toString().equals(binding.confPass.getText().toString())) {
                    binding.confPass.setError("The Passwords does not match");
                    return;
                } else {
                    auth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = auth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Register.this, "Verification Link sent to " + binding.email.getText(), Toast.LENGTH_SHORT).show();
                                        Users users = new Users(binding.name.getText().toString(), binding.email.getText().toString(), binding.password.getText().toString());
                                        users.setUserid(user.getUid());

                                        firestore.collection("Users").document(user.getUid()).set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(Register.this, usernameactivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });


        binding.clickToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });
    }

    private void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("933779368906-p5vlkrrt0mbas6q8lcde1ipcjdv7b84o.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSigninClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void signIn() {
        Intent intent = mGoogleSigninClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult();
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Users users = new Users();
                            users.setUserid(user.getUid());
                            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(Register.this);
                            if (signInAccount != null) {
                                firestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        for (DocumentSnapshot snapshot : value) {
                                            if (auth.getCurrentUser().getUid().equals(snapshot.get("userid"))) {
                                                b = 1;
                                                break;
                                            }
                                        }
                                        if (b != 1) {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("name", signInAccount.getDisplayName());
                                            hashMap.put("mail", signInAccount.getEmail());
                                            hashMap.put("userid", user.getUid());
                                            hashMap.put("username",null);
                                            if (signInAccount.getPhotoUrl() != null) {
                                                hashMap.put("profilepic", signInAccount.getPhotoUrl().toString());
                                            }
                                            firestore.collection("Users").document(user.getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    startActivity(new Intent(Register.this, usernameactivity.class));
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(Register.this, "This Email Is Already Registered", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this,Login.class));
                                            finish();
                                        }
                                    }

                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }
}