package com.example.beta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class User {
    String uid;
    List<String> types;
    Boolean isConnected;

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

