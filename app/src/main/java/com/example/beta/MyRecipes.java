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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecipes extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener
{

    private ListView lVcustom;
    public static ArrayList<String> names, ids;
    private int[] pics;
    private Intent gi;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        refRecipes=fbDB.getReference("Recipes");
        gi= getIntent();
        initAll();

        customAdapter = new CustomAdapter(this, pics, names);
        lVcustom.setOnItemClickListener(this);
        lVcustom.setOnItemLongClickListener(this);


    }

    private void initAll(){
        lVcustom = findViewById(R.id.lv);
        names=new ArrayList<>();
        ids = new ArrayList<>();
        refRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //names.clear();
                // Iterate through the user data and extract names
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String uid =snapshot.child("uid").getValue(String.class);
                    if(uid.equals(fbuser.getUid()))
                    {
                        String userName = snapshot.child("title").getValue(String.class);
                        names.add(userName);
                        String keyIds = snapshot.child("keyId").getValue(String.class);
                        ids.add(keyIds);
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


    //FUNCTION IN NEED OF FIXING
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long i) {
        String id =ids.get(position);
        Query keyIdQuery = refRecipes.orderByChild("keyId").equalTo(id);

        keyIdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                    idSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyRecipes.this, "something failed while trying to delete item", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }
}