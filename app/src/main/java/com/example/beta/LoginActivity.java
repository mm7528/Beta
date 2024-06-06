package com.example.beta;

import static com.example.beta.FBDB.fbuser;
import static com.example.beta.FBDB.mAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The type Login activity.
 */
public class LoginActivity extends AppCompatActivity {


    private Button login1;
    private EditText editTextPassword,editTextEmail;
    private BroadcastReceiver broadcastReceiver;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextEmail = (EditText) findViewById(R.id.email);

        login1 = (Button) findViewById(R.id.login);
        broadcastReceiver=new InternetReceiver();



        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

    }


    /**
     * the function logs in the  user,
     * if all fields are correct, else it creates an error message
     */
    private void loginUserAccount() {
        String email,password;
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());


        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login successful!!",
                                            Toast.LENGTH_LONG).show();
                                    fbuser=mAuth.getCurrentUser();
                                    Intent si = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(si);
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!! please check password and email",
                                                    Toast.LENGTH_LONG)
                                            .show();



                                }
                            }
                        });
    }


    /**
     * On click function that forwards the user to RegisterActivity
     *
     * @param view the view
     */
    public void register(View view) {
        Intent si = new Intent(LoginActivity.this,RegisterActivity.class);
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