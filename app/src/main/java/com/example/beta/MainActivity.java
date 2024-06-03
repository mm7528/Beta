package com.example.beta;

import static com.example.beta.FBDB.mAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastReceiver=new InternetReceiver();


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

    public void scan(View view) {
        Intent si = new Intent(MainActivity.this,ScanRecipe.class);
        startActivity(si);
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