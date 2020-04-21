package com.anura.anuramotors;

import androidx.annotation.NonNull;

import com.anura.anuramotors.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUsers;
    private List<User> UserRegs = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<User> UserRegs, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public DatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceUsers = mDatabase.getReference("UserRegs");//asa
    }
    public void readUsers(final DataStatus dataStatus){
        mReferenceUsers.child("UserRegs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserRegs.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot KeyNode: dataSnapshot.getChildren()){
                    keys.add(KeyNode.getKey());
                    User u = KeyNode.getValue(User.class);
                    UserRegs.add(u);
                }
                dataStatus.DataIsLoaded(UserRegs,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }
    }

