package com.example.beta;



import java.util.ArrayList;
import java.util.List;


public class User {
    private String uid;
    private List<String> types;

    public User()
    {

    }

    public User(String uid)
    {
        this.uid=uid;
        this.types=new ArrayList<>();
        types.add("cakes");
        types.add("meat");
        types.add("salads");
        types.add("fish");
    }

    public User(String uid,List<String>types)
    {
        this.uid=uid;
        this.types=types;
    }


    public String getUid() {
        return uid;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

