package com.example.beta;

import static com.example.beta.FBDB.fbDB;
import static com.example.beta.FBDB.fbuser;
import static com.example.beta.FBDB.refRecipes;
import static com.example.beta.FBDB.refUsers;
import static com.example.beta.FBDB.storageReference;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Recipe recipe;
    private EditText title,ingredients,instructions;
    private List<String> options;
    private String uid,str;
    private String recipeTitle,recipeIngredients,recipeInstructions;
    private boolean connected;
    private Intent gi;
    private Spinner spin;
    private String[] temp;
    private List<String>lines;
    private AlertDialog.Builder adb;
    private final int OPEN_GALLERY_CODE = 1234;
    private Button upload;
    private Uri imageUri;
    private StorageReference imageRef;
    private BroadcastReceiver broadcastReceiver;
    private ActivityResultLauncher<String> mGetContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipe = new Recipe();
        title = (EditText) findViewById(R.id.title);
        ingredients = (EditText) findViewById(R.id.ingredients);
        instructions = (EditText) findViewById(R.id.instructions);
        spin =(Spinner) findViewById(R.id.spinner);
        upload=(Button)findViewById(R.id.button9);
        broadcastReceiver=new InternetReceiver();

        //in order to check
        imageRef=null;

        //checking if recipe was scanned
        gi= getIntent();
        recipeTitle=gi.getStringExtra("title");
        if(recipeTitle!=null){
            recipeIngredients=gi.getStringExtra("ingredients");
            recipeInstructions=gi.getStringExtra("instructions");
            title.setText(recipeTitle);
            ingredients.setText(recipeIngredients);
            instructions.setText(recipeInstructions);
        }
        uid = fbuser.getUid();
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
                        spin.setOnItemSelectedListener(AddRecipe.this);

                        ArrayAdapter<String> adp = new ArrayAdapter<String>(AddRecipe.this,
                                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,options);
                        spin.setAdapter(adp);

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRecipe.this, "failed to connect to database, please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                imageUri=o;
                uploadPicture();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGalleryPermission();

            }


        });

    }

    private void checkGalleryPermission() {
        if(ContextCompat.checkSelfPermission(AddRecipe.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)

        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddRecipe.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // This is Case 4.

                ActivityCompat.requestPermissions(AddRecipe.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                } ,OPEN_GALLERY_CODE);
            } else {
                // This is Case 3. Request for permission here
                ActivityCompat.requestPermissions(AddRecipe.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                } ,OPEN_GALLERY_CODE);
            }

        }
        else {
            mGetContent.launch("image/*");
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mGetContent.launch("image/*");
            // This is Case 2 (Permission is now granted)
        } else {
            // This is Case 1 again as Permission is not granted by user

            //Now further we check if used denied permanently or not
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddRecipe.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // case 4 User has denied permission but not permanently

                ActivityCompat.requestPermissions(AddRecipe.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                } ,OPEN_GALLERY_CODE);

            } else {
                // case 5. Permission denied permanently.
                // You can open Permission setting's page from here now.
                Toast.makeText(this, "cant open gallery", Toast.LENGTH_SHORT).show();
            }

        }
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
            if(imageUri==null||imageRef==null)
            {
                recipe.setStorageId("recipeDefault.png");
            }


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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private void uploadPicture() {
        String id =fbuser.getUid();
        final String randomKey = UUID.randomUUID().toString();
        String storageRef=id+"/"+"recipePhotos"+"/"+randomKey;
        imageRef = storageReference.child(storageRef);
        recipe.setStorageId(storageRef);
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "image uploaded", Toast.LENGTH_LONG).show();
                    }
                })      .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private List<String> arrToList(String[] arr)
    {
        List<String>lines=new ArrayList<>();
        for(int i=0;i<arr.length;i++)
        {
            lines.add(arr[i]);
        }
        return lines;
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