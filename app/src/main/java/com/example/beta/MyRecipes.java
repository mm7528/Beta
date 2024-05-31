package com.example.beta;

import static com.example.beta.AddRecipe.refRecipes;
import static com.example.beta.LoginActivity.fbDB;
import static com.example.beta.LoginActivity.fbuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyRecipes extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lVcustom;
    private ArrayList<String> names, ids;
    private ArrayList<StorageReference>images;
    private CustomAdapter customAdapter;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        refRecipes=fbDB.getReference("Recipes");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        initAll();

        customAdapter = new CustomAdapter(this);
        lVcustom.setOnItemClickListener(this);


    }

    private void initAll(){
        lVcustom = findViewById(R.id.lv);
        names=new ArrayList<>();
        ids = new ArrayList<>();
        images=new ArrayList<>();
        refRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the user data and extract names
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String uid =snapshot.child("uid").getValue(String.class);
                    if(uid.equals(fbuser.getUid()))
                    {
                        String userName = snapshot.child("title").getValue(String.class);
                        names.add(userName);
                        String keyIds = snapshot.child("keyId").getValue(String.class);
                        ids.add(keyIds);
                        String storageId = snapshot.child("storageId").getValue(String.class);
                        imageRef=storageRef.child(storageId);
                        images.add(imageRef);
                        customAdapter.setImages(images);
                        customAdapter.setStringsList(names);
                        lVcustom.setAdapter(customAdapter);
                    }



                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyRecipes.this, "an error occurred please try again.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent si = new Intent(MyRecipes.this,ShowRecipe.class);
        si.putExtra("keyId",ids.get(position));
        startActivity(si);

    }

}