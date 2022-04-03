package com.example.onlinechatapp.Notifications;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME="fcmsharedpref";
    private static final String KEY_ACCESS_TOKEN="token";

    private static Context context1;
    private static SharedPrefManager mInstance;

    private SharedPrefManager(Context context){
        context1=context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance==null){
            mInstance=new SharedPrefManager(context);
        }
            return mInstance;
    }
    public  boolean storeToken(String token){
        SharedPreferences sharedPreferences=context1.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;
    }

    public String getToken(){
        SharedPreferences sharedPreferences= context1.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);

    }
}
