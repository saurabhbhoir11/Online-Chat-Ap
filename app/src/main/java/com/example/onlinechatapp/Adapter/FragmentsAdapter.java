package com.example.onlinechatapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.onlinechatapp.Fragments.ChatFrag;
import com.example.onlinechatapp.Fragments.GroupFrag;
import com.example.onlinechatapp.Fragments.ProfileFrag;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:return new ChatFrag();
            case 1:return new GroupFrag();
            case 2:return new ProfileFrag();
        }
        return new ChatFrag();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title= null;
        if(position==0){

            title="CHATS";
        }
        if(position==1){

            title="GROUPS";
        }
        if(position==2){

            title="PROFILE";
        }
        return title;
    }
}

