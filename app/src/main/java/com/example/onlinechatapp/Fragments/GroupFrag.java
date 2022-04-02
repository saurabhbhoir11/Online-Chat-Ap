package com.example.onlinechatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.onlinechatapp.CreateGroup;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.databinding.FragmentGroupBinding;
import com.example.onlinechatapp.models.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupFrag extends Fragment {
    FragmentGroupBinding binding;
    FirebaseAuth auth;

    private ArrayList<GroupModel> GroupList;
    private GroupAdapter groupAdapter;

    public GroupFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
        auth= FirebaseAuth.getInstance();

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), CreateGroup.class);
                startActivity(intent);
            }
        });
        loadGroups();

        return binding.getRoot();
    }

    private void loadGroups(){
        GroupList =new ArrayList<>();
        CollectionReference ref= FirebaseFirestore.getInstance().collection("groups");
        ref.add(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                GroupList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.child("Participants").child(auth.getCurrentUser().getUid()).exists()){
                        GroupModel model= snapshot1.getValue(GroupModel.class);
                        GroupList.add(model);
                    }
                }
                groupAdapter=new GroupAdapter(getContext(),GroupList);
                binding.grplist.setAdapter(groupAdapter);
                groupAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });
    }
}