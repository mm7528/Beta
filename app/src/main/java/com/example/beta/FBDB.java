package com.example.beta;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBDB {
    public static FirebaseAuth mAuth=FirebaseAuth.getInstance();;
    /**
     * The constant fbuser.
     */
    public static FirebaseUser fbuser= mAuth.getCurrentUser();
    /**
     * The constant fbDB.
     */
    public static FirebaseDatabase fbDB=FirebaseDatabase.getInstance();
    /**
     * The constant refUsers.
     */
    public static DatabaseReference refUsers= fbDB.getReference("Users");

    public static DatabaseReference refRecipes=fbDB.getReference("Recipes");

    public static FirebaseStorage storage= FirebaseStorage.getInstance();
    public static StorageReference storageReference= storage.getReference();;


}
