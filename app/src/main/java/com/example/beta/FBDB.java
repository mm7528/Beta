package com.example.beta;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * The type Fbdb.
 */
public class FBDB {
    /**
     * The constant mAuth - an instance of Firebase authentication.
     */
    public static FirebaseAuth mAuth=FirebaseAuth.getInstance();;
    /**
     * The constant fbuser - the user reference of the Firebase authentication.
     */
    public static FirebaseUser fbuser= mAuth.getCurrentUser();
    /**
     * The constant fbDB -  an instance of Firebase database.
     */
    public static FirebaseDatabase fbDB=FirebaseDatabase.getInstance();
    /**
     * The constant refUsers- a reference to the users in Firebase database.
     */
    public static DatabaseReference refUsers= fbDB.getReference("Users");

    /**
     * The constant refRecipes - a reference to the recipes in Firebase database.
     */
    public static DatabaseReference refRecipes=fbDB.getReference("Recipes");

    /**
     * The constant storage- a storage instance.
     */
    public static FirebaseStorage storage= FirebaseStorage.getInstance();
    /**
     * The constant storageReference- a storage reference.
     */
    public static StorageReference storageReference= storage.getReference();;


}
