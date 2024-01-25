package com.example.beta;

import com.example.beta.Manager;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Recipe recipe = new Recipe();
    EditText title,ingredients,instructions;
    List<String> options;
    String uid;
    Intent gi;
    Spinner spin;
    Manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        title = (EditText) findViewById(R.id.title);
        ingredients = (EditText) findViewById(R.id.ingredients);
        instructions = (EditText) findViewById(R.id.instructions);
        spin =(Spinner) findViewById(R.id.spinner);
        manager=new Manager();
        gi= getIntent();
        uid =gi.getExtras().getString("userId","1");
        //options=getUser(uid).getTypes();
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