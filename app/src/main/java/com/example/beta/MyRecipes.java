package com.example.beta;

import static com.example.beta.AddRecipe.refRecipes;

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

import java.util.ArrayList;

public class MyRecipes extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener
{

    private ListView lVcustom;
    private ArrayList<String> names, types;
    private int[] pics;
    Intent gi;
    //private com.example.beta.CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        gi= getIntent();
        Toast.makeText(this, "line 34", Toast.LENGTH_SHORT).show();
        //initAll();
        //customAdapter = new CustomAdapter(this, pics, names);
        //lVcustom.setAdapter(customAdapter);
        //lVcustom.setOnItemClickListener(this);
        //lVcustom.setOnItemLongClickListener(this);
    }

    private void initAll(){
        lVcustom = findViewById(R.id.lv);

        refRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //names.clear();
                // Iterate through the user data and extract names
                Toast.makeText(MyRecipes.this, "line 50", Toast.LENGTH_SHORT).show();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userName = snapshot.child("title").getValue(String.class);
                    names.add(userName);
                }
                //adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyRecipes.this, "an error occurred please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}