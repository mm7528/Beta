package com.example.beta;

import static com.example.beta.LoginActivity.fbDB;
import static com.example.beta.LoginActivity.fbuser;
import static com.example.beta.LoginActivity.refUsers;

import static java.nio.file.Paths.get;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Recipe recipe = new Recipe();
    private EditText title,ingredients,instructions;
    public List<String> options;
    private String uid,str;
    private boolean connected;
    private Intent gi;
    private Spinner spin;
    private String[] temp;
    private List<String>lines;
    private AlertDialog.Builder adb;
    public static DatabaseReference refRecipes;
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
        refRecipes= fbDB.getReference("Recipes");
        adb=new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("enter the type of recipe");
        EditText et = new EditText(this);
        et.setHint("add type");
        adb.setView(et);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                str = et.getText().toString();
                recipe.setType(str);
                dialog.dismiss();

            }
        });
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
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
                        connected = (Boolean) ((Map<?, ?>) task.getResult().getValue()).get("connected");
                        options.add("other");
                        Toast.makeText(AddRecipe.this, String.valueOf(options), Toast.LENGTH_SHORT).show();
                        spin.setOnItemSelectedListener(AddRecipe.this);

                        ArrayAdapter<String> adp = new ArrayAdapter<String>(AddRecipe.this,
                                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,options);
                        spin.setAdapter(adp);

                    }
                    Toast.makeText(AddRecipe.this, String.valueOf(task.getResult().getValue()), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void newRecipe(View view) {
        String t = title.getText().toString();
        String ing = ingredients.getText().toString();
        String ins = instructions.getText().toString();
        if(t==null || ing == null || ins==null)
        {
            Toast.makeText(this, "some fields are empty please fill the up!", Toast.LENGTH_SHORT).show();
        }
        else{
            recipe.setTitle(t);
            temp = ing.split("\n");
            lines = arrToList(temp);
            recipe.setIngredients(lines);
            temp = ins.split("\n");
            lines = arrToList(temp);
            recipe.setInstructions(lines);
            recipe.setUid(uid);


            refRecipes.child(recipe.getKeyId()).setValue(recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                        Toast.makeText(AddRecipe.this, "successfully uploaded recipe", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddRecipe.this, "failed to connect to database", Toast.LENGTH_SHORT).show();
                }
            });


            refUsers.child(uid).removeValue();
            int s = options.size()-1;
            List<String> l = new ArrayList<>(options);
            l.set(s,str);
            User usr = new User(uid,connected,l);
            refUsers.child(uid).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AddRecipe.this, "successfully uploaded user", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddRecipe.this, "failed to connect to database", Toast.LENGTH_SHORT).show();
                }
            });




        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(options.get(position).equals("other"))
        {
            adb.show();

        }
        recipe.setType(options.get(position));
        Toast.makeText(this, options.get(position), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private List<String> arrToList(String[] arr)
    {
        List<String>lines=new ArrayList<>();
        for(int i=0;i<arr.length;i++)
        {
            lines.add(arr[i]);
            Toast.makeText(this, arr[i], Toast.LENGTH_SHORT).show();
        }
        return lines;
    }
}