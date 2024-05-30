package com.example.beta;

import static com.example.beta.AddRecipe.refRecipes;
import static com.example.beta.LoginActivity.fbDB;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShowRecipe extends AppCompatActivity {
    private TextView instructions,ingredients,title;
    private ImageView iv;
    private TextToSpeech textToSpeech;
    private Button btn;
    private Intent gi;
    private String  recipeTitle, rIngredients, rInstructions, storageId;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageRef;

   // @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);
        refRecipes=fbDB.getReference("Recipes");
        instructions=(TextView) findViewById(R.id.textView8);
        ingredients =(TextView) findViewById(R.id.textView7);
        title = (TextView) findViewById(R.id.textView6);
        btn =(Button)findViewById(R.id.read);
        iv=(ImageView) findViewById(R.id.imageView2);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        gi=getIntent();
        String id =gi.getStringExtra("keyId");


        refRecipes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Iterate through the user data and extract names
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String keyId = snapshot.child("keyId").getValue(String.class);
                    if(keyId.equals(id))
                    {
                        recipeTitle=snapshot.child("title").getValue(String.class);
                        title.setText(recipeTitle);
                        List<String> recipeIngredients=(List<String>) snapshot.child("ingredients").getValue();
                        rIngredients=lstToStr(recipeIngredients);
                        ingredients.setText(rIngredients);
                        List<String> recipeInstructions=(List<String>) snapshot.child("instructions").getValue();
                        rInstructions=lstToStr(recipeInstructions);
                        instructions.setText(rInstructions);
                        storageId=snapshot.child("storageId").getValue(String.class);
                        imageRef=storageRef.child(storageId);
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ShowRecipe.this).load(uri).into(iv);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ShowRecipe.this, "failed to download recipe image", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowRecipe.this, "an error occurred please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                }

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "please enter text", Toast.LENGTH_LONG).show();
                }
                else if(textToSpeech.isSpeaking())
                {
                    textToSpeech.stop();
                }
                else
                {
                    String speak=title.getText().toString()+"\ningredients\n"+ingredients.getText().toString()+"\ninstructions\n"+instructions.getText().toString();
                    textToSpeech.speak(speak,TextToSpeech.QUEUE_FLUSH,null,null);


                }
            }
        });
    }

    private String lstToStr(List<String> lst)
    {
        String str="";
        for(int i=0;i<lst.size();i++)
        {
            str+=lst.get(i);
            str+="\n";
        }
        return str;
    }

}