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

import com.example.onlinechatapp.databinding.ActivityLoginBinding;
import com.example.onlinechatapp.models.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 123;
    FirebaseAuth auth;
    private GoogleSignInClient mGoogleSigninClient;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {

            if (!auth.getCurrentUser().isEmailVerified() && auth.getCurrentUser().getPhoneNumber() == null) {
                Toast.makeText(this, "Please Verify Email First", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Login.this, home.class);
                startActivity(intent);
                finish();
            }
        }

        createRequest();


        binding.googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        binding.phoneLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, PhoneLoginActivity.class));
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signInWithEmailAndPassword(binding.email1.getText().toString(), binding.pass1.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (auth.getCurrentUser().isEmailVerified()) {
                            startActivity(new Intent(Login.this, home.class));
                        } else {
                            Toast.makeText(Login.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.clickToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
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
                                Users users=new Users();
                                users.setUserid(user.getUid());
                                GoogleSignInAccount signInAccount=GoogleSignIn.getLastSignedInAccount(Login.this);
                                if(signInAccount!=null){
                                    users.setName(signInAccount.getDisplayName());
                                    users.setMail(signInAccount.getEmail());
                                    if(signInAccount.getPhotoUrl()!=null){
                                        users.setProfilepic(String.valueOf(signInAccount.getPhotoUrl()));
                                    }
                                    Toast.makeText(Login.this, ""+user.getUid(), Toast.LENGTH_SHORT).show();
                                    firestore.collection("Users").document(user.getUid()).set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            firestore.collection("Users").document(user.getUid()).collection("username").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (value!=null){
                                                        startActivity(new Intent(Login.this, usernameactivity.class));
                                                    }
                                                    else{
                                                        startActivity(new Intent(Login.this, home.class));
                                                    }
                                                }
                                            });
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