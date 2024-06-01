package com.example.beta;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    private Button signup;
    private FirebaseAuth mAuth;
    /**
     * The constant fbuser.
     */
    public static FirebaseUser fbuser;
    /**
     * The constant fbDB.
     */
    public static FirebaseDatabase fbDB;
    /**
     * The constant refUsers.
     */
    public static DatabaseReference refUsers;
    private User user;
    private EditText editTextPassword,editTextEmail,confirmPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signup = (Button) findViewById(R.id.signIn);
        editTextPassword = (EditText) findViewById(R.id.password1);
        editTextEmail = (EditText) findViewById(R.id.email1);
        confirmPassword=(EditText) findViewById(R.id.password2);

        mAuth =FirebaseAuth.getInstance();
        fbDB=FirebaseDatabase.getInstance();
        refUsers = fbDB.getReference("Users");
        fbuser = mAuth.getCurrentUser();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

    }

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
                            fbuser=mAuth.getCurrentUser();
                            String uid=mAuth.getUid();
                            user=new User(uid,false);
                            refUsers.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),
                                                        "Registration successful!",
                                                        Toast.LENGTH_LONG)
                                                .show();
                                        Intent si = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(si);

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "failed to connect to database", Toast.LENGTH_SHORT).show();
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

    public void login(View view) {
        Intent si = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(si);
    }
}