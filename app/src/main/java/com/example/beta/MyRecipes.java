package com.example.beta;

import static com.example.beta.FBDB.refRecipes;
import static com.example.beta.FBDB.fbuser;
import static com.example.beta.FBDB.refUsers;
import static com.example.beta.FBDB.storageReference;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type My recipes.
 */
public class MyRecipes extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {

    private ListView lVcustom;
    private ArrayList<String> names, ids, recipeTypes,filteredIds;
    private ArrayList<StorageReference>images;
    private CustomAdapter customAdapter;
    private StorageReference imageRef;
    private List<String> options;
    private Spinner spinner;
    private BroadcastReceiver broadcastReceiver;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        spinner = (Spinner) findViewById(R.id.spin2);
        broadcastReceiver=new InternetReceiver();
        initAll();

        customAdapter = new CustomAdapter(this);
        lVcustom.setOnItemClickListener(this);

        refUsers.child(fbuser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                        options.add(0,"All");
                        spinner.setOnItemSelectedListener( MyRecipes.this);

                        ArrayAdapter<String> adp = new ArrayAdapter<String>(MyRecipes.this,
                                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,options);
                        spinner.setAdapter(adp);

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyRecipes.this, "failed to connect to database, please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * the function initiates all the custom adapter of the listView
     */
    private void initAll(){
        lVcustom = findViewById(R.id.lv);
        names=new ArrayList<>();
        ids = new ArrayList<>();
        images=new ArrayList<>();
        recipeTypes=new ArrayList<>();
        filteredIds=new ArrayList<>();
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
                        filteredIds=ids;
                        String storageId = snapshot.child("storageId").getValue(String.class);
                        imageRef=storageReference.child(storageId);
                        String type =snapshot.child("type").getValue(String.class);
                        recipeTypes.add(type);
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

    /**
     *
     * @param parent The AdapterView where the click happened.
     * @param view The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked.
     * checks which recipe from the list was chosen and starts new activity that shows this recipe
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent si = new Intent(MyRecipes.this,ShowRecipe.class);
        si.putExtra("keyId",filteredIds.get(position));
        startActivity(si);

    }

    /**
     * the function creates a filter for the listView by
     * searching for the recipes from the specific type
     *
     * @param type the type of the recipe
     */
    public void filter(String type) {
        ArrayList<Integer> indexes=new ArrayList<>();
        customAdapter = new CustomAdapter(this);
        lVcustom.setOnItemClickListener(this);
        ArrayList<String> names1=new ArrayList<>();
        ArrayList<StorageReference>images1=new ArrayList<>();
        for(int i=0;i<recipeTypes.size();i++)
        {
            if(recipeTypes.get(i).equals(type))
            {
                indexes.add(i);
            }
        }
        for(int i=0;i<indexes.size();i++)
        {
            int index=indexes.get(i);
            names1.add(i,names.get(index));
            images1.add(i,images.get(index));
            filteredIds.set(i,ids.get(index));
        }
        customAdapter.setImages(images1);
        customAdapter.setStringsList(names1);
        lVcustom.setAdapter(customAdapter);

    }


    /**
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     *
     * the function checks which spinner item was chosen and act accordingly
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        customAdapter = new CustomAdapter(this);
        if(options.get(position).equals("All"))
        {
            filteredIds=ids;
            customAdapter.setImages(images);
            customAdapter.setStringsList(names);
            lVcustom.setAdapter(customAdapter);
        }
        else
        {
            filter(options.get(position));
        }

    }

    /**
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}