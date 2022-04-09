package com.example.onlinechatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onlinechatapp.Adapter.GroupAdapter;
import com.example.onlinechatapp.CreateGroup;
import com.example.onlinechatapp.R;
import com.example.onlinechatapp.databinding.FragmentGroupBinding;
import com.example.onlinechatapp.models.GroupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupFrag extends Fragment {
    FragmentGroupBinding binding;
    FirebaseAuth auth;
    private ArrayList<GroupModel> GroupList;
    FirebaseFirestore firestore;
    private GroupAdapter groupAdapter;

    public GroupFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGroupBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        binding.crtgrpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGroup.class);
                startActivity(intent);
            }
        });
        loadGroups();

        return binding.getRoot();
    }

    private void loadGroups() {
        GroupList = new ArrayList<>();
        firestore.collection("groups").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                GroupList.clear();
                assert value != null;
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Toast.makeText(getContext(), "" + snapshot.get("timestamp"), Toast.LENGTH_SHORT).show();
                    firestore.collection("groups").document(String.valueOf(snapshot.get("timestamp"))).collection("Participants").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (DocumentSnapshot snapshot1 : value.getDocuments()) {
                                if (snapshot1.get("uid").equals(auth.getCurrentUser().getUid())) {
                                    GroupModel model = snapshot.toObject(GroupModel.class);
                                    GroupList.add(model);
                                    groupAdapter = new GroupAdapter(getContext(), GroupList);
                                    binding.grpList.setAdapter(groupAdapter);
                                    groupAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

                }
            }
        });

    }
}