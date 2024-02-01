package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Intent gi;

    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        gi = getIntent();
        uid = LoginActivity.fbuser.getUid();

    }

    public void showRecipes(View view) {
        Intent si = new Intent(MainActivity.this,MyRecipes.class);
        startActivity(si);

    }

    public void addRecipe(View view) {
        Intent si = new Intent(MainActivity.this,AddRecipe.class);
        startActivity(si);
    }

    public void scannedRecipes(View view) {
        Intent si = new Intent(MainActivity.this,ScannedRecipes.class);
        startActivity(si);
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent si = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(si);
    }
}