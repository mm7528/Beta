package com.example.beta;

import static com.example.beta.FBDB.mAuth;
import static com.example.beta.FBDB.refUsers;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The type Register activity.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button signup;
    private User user;
    private EditText editTextPassword,editTextEmail,confirmPassword;
    private BroadcastReceiver broadcastReceiver;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signup = (Button) findViewById(R.id.signIn);
        editTextPassword = (EditText) findViewById(R.id.password1);
        editTextEmail = (EditText) findViewById(R.id.email1);
        confirmPassword=(EditText) findViewById(R.id.password2);
        broadcastReceiver=new InternetReceiver();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

    }

    /**
     * the function registers the new user and creates a new user account
     * if all fields are correct, else it creates an error message
     */
    private void registerNewUser()
    {
        String email, password,confirm;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        confirm=confirmPassword.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        else if (!confirm.equals(password)) {
            Toast.makeText(getApplicationContext(),
                            "Password and confirmation don't match, try again please ",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //fbuser=mAuth.getCurrentUser();
                            String uid=mAuth.getUid();
                            user=new User(uid);
                            refUsers.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent si = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(si);

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(
                                                    getApplicationContext(),
                                                    "Registration failed!!"
                                                            + " Please try again later",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        } else {

                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    /**
     * an On click that forwards the user to the LoginActivity
     *
     * @param view the view
     */
    public void login(View view) {
        Intent si = new Intent(RegisterActivity.this,LoginActivity.class);
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