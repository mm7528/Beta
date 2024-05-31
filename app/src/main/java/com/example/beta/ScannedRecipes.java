package com.example.beta;

import static com.example.beta.LoginActivity.fbuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.LinkedList;

import java.util.function.Consumer;

public class ScannedRecipes extends AppCompatActivity {

    private ImageView iv;
    private Button prev,next;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference listRef;
    private String path;
    private LinkedList<StorageReference> pics;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_recipes);
        iv = (ImageView) findViewById(R.id.imageView);
        prev = (Button) findViewById(R.id.prev);
        next =(Button) findViewById(R.id.next);
        storage = FirebaseStorage.getInstance();
        storageRef=storage.getReference();
        path=fbuser.getUid()+"/"+"scanned/";
        listRef=storageRef.child(path);
        pics = new LinkedList<>();
        index=0;
        prev.setEnabled(false);
        next.setEnabled(!(index==pics.size()-1));

        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                listResult.getItems().forEach(new Consumer<StorageReference>() {
                    @Override
                    public void accept(StorageReference storageReference) {
                        pics.add(storageReference);
                        if(pics.size()==1)
                        {
                            pics.get(index).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(ScannedRecipes.this).load(uri).into(iv);

                                }
                            });
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScannedRecipes.this, "an error occurred while showing the images, please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void prev(View view) {
        index--;
        pics.get(index).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ScannedRecipes.this).load(uri).into(iv);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScannedRecipes.this, "an error occurred while showing the images, please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        checkButtons();

    }

    private void checkButtons() {
        prev.setEnabled(index!=0);
        next.setEnabled(!(index==pics.size()-1));
    }

    public void next(View view) {
        index++;
        pics.get(index).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ScannedRecipes.this).load(uri).into(iv);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScannedRecipes.this, "an error occurred while showing the images, please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        checkButtons();
    }
}