package com.example.onlinechatapp.Fragments;

import com.example.onlinechatapp.Notifications.MyResponse;
import com.example.onlinechatapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:apllication/json",
            "Autharization:key=AAAA2WmWa8o:APA91bH6jKH-ht7pyKEvBaqVW7uSBSFf5PNL0-hnXSIyFGBoMLQokAfmB7WIAMpemmnH2vyDAzJ6U1ryLfa6d16rcVEo8231L-VuF5RHYeccROFj4cpo7CG5Jpr4Dko75Usar-4CPvms"
    })

@POST("fcm/send")
    Call<MyResponse> sendNotification (@Body Sender body)

}
