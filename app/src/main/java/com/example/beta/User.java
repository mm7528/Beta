package com.example.beta;



import java.util.ArrayList;
import java.util.List;


/**
 * The type User.
 */
public class User {
    private String uid;
    private List<String> types;

    /**
     * Instantiates a new User.
     */
    public User()
    {

    }

    /**
     * Instantiates a new User.
     *
     * @param uid the uid
     */
    public User(String uid)
    {
        this.uid=uid;
        this.types=new ArrayList<>();
        types.add("cakes");
        types.add("meat");
        types.add("salads");
        types.add("fish");
    }

    /**
     * Instantiates a new User.
     *
     * @param uid   the uid
     * @param types the types
     */
    public User(String uid,List<String>types)
    {
        this.uid=uid;
        this.types=types;
    }


    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Gets types.
     *
     * @return the types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Sets types.
     *
     * @param types the types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}

