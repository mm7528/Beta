package com.example.beta;

import static com.example.beta.LoginActivity.fbDB;
import static com.example.beta.LoginActivity.fbuser;
import static com.example.beta.LoginActivity.refUsers;

import com.example.beta.Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Recipe recipe = new Recipe();
    EditText title,ingredients,instructions;
    List<String> options;
    String uid;
    Intent gi;
    Spinner spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        title = (EditText) findViewById(R.id.title);
        ingredients = (EditText) findViewById(R.id.ingredients);
        instructions = (EditText) findViewById(R.id.instructions);
        spin =(Spinner) findViewById(R.id.spinner);
        gi= getIntent();
        uid = fbuser.getUid();
        //options=getUser(uid).getTypes();
        //options.add("Other");
        refUsers.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    if(task.getResult().getValue() instanceof java.util.Map)
                    {
                       options= (List<String>) ((Map<?, ?>) task.getResult().getValue()).get("types");
                        Toast.makeText(AddRecipe.this, "true", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(AddRecipe.this, String.valueOf(task.getResult().getValue()), Toast.LENGTH_LONG).show();
                }
            }
        });
        options.add("Other");
    }

    public void newRecipe(View view) {
        String t = title.getText().toString();
        recipe.setTitle(t);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}