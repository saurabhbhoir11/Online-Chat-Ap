package com.example.onlinechatapp.Notifications;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String refresh_token= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            updateToken(refresh_token);
        }
    }

    private void updateToken(String refresh_token) {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Token token=new Token(refresh_token);
        FirebaseFirestore.getInstance().collection("Tokens").document(firebaseUser.getUid()).set(token);
    }
}
