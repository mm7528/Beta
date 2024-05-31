package com.example.beta;



import java.util.ArrayList;
import java.util.List;


public class User {
    private String uid;
    private List<String> types;
    private Boolean isConnected;

    public User()
    {

    }

    public User(String uid, Boolean isConnected)
    {
        this.uid=uid;
        this.types=new ArrayList<>();
        types.add("cakes");
        types.add("meat");
        types.add("salads");
        types.add("fish");
        this.isConnected=isConnected;
    }

    public User(String uid, Boolean isConnected,List<String>types)
    {
        this.uid=uid;
        this.types=types;
        this.isConnected=isConnected;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public String getUid() {
        return uid;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

