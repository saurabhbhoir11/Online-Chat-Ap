package com.example.onlinechatapp.Notifications;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        storeToken(token);
    }

    private void storeToken(String token) {
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }

    public static void senPushNotification(final String body, final String title, final String fcmToken,final  String time) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject notificationJson = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    notificationJson.put("body", body+"     "+time);
                    notificationJson.put("title", title);
                    notificationJson.put("priority", "high");
                    dataJson.put("message",body);
                    dataJson.put("customId", "02");
                    dataJson.put("badge", 1);
                    dataJson.put("alert", "Alert");
                    json.put("notification", notificationJson);
                    json.put("data", dataJson);
                    json.put("to", fcmToken);
                    RequestBody body1 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                    Request request = new Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body1)
                            .addHeader("Authorization", "key=AAAA2WmWa8o:APA91bH6jKH-ht7pyKEvBaqVW7uSBSFf5PNL0-hnXSIyFGBoMLQokAfmB7WIAMpemmnH2vyDAzJ6U1ryLfa6d16rcVEo8231L-VuF5RHYeccROFj4cpo7CG5Jpr4Dko75Usar-4CPvms")
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.i("TAG","success:"+ finalResponse);
                } catch (Exception e) {
                    Log.i("TAG", "error:"+e.getMessage());
                }
                return null;
            }
        }.execute();
    }
}
