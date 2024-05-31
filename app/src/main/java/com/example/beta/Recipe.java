package com.example.beta;
import java.util.UUID;
import java.util.List;

public class Recipe {
    private String keyId;
    private String type;
    private String title;
    private List<String> ingredients;
    private List<String> instructions;
    private String uid;
    private String storageId;

    public Recipe()
    {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.keyId=uuidString;


    }

    public Recipe(String type,String title,String uid,String storageId,List<String>ingredients,List<String>instructions)
    {
        this.type=type;
        this.title=title;
        this.uid=uid;
        this.storageId=storageId;
        this.ingredients=ingredients;
        this.instructions=instructions;
    }

    public Recipe(String keyId, String uid,String storageId){
        this.keyId=keyId;
        this.uid=uid;
        this.storageId=storageId;
    }

    public String getType()
    {
        return type;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getStorageId() {
        return storageId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getUid()
    {
        return uid;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}

