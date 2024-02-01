package com.example.beta;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.beta.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.*;

public class Manager {
    public List<User> registeredUsers;
    public List<String>userId;


    public Manager()
    {
        FirebaseDatabase fbDB;
        List<String>uids=new ArrayList<>();
        fbDB=FirebaseDatabase.getInstance();
        DatabaseReference refUsers;
        refUsers = fbDB.getReference("Users/uid");
        /*ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String uid = ds.child("uid").getValue(String.class);
                    System.out.println(uid);
                    uids.add(uid);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        this.userId=uids;*/
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public User getUser(String uid)
    {
        for(int i=0;i<registeredUsers.size();i++)
        {
            if(registeredUsers.get(i).uid.equals(uid))
            {
                return registeredUsers.get(i);
            }
        }
        return null;
    }

    public String getAllUid()
    {
        String users="";
        for(int i=0;i<this.userId.size();i++)
        {
           users= userId.get(i) + ", ";
        }
        return users;
    }
}
