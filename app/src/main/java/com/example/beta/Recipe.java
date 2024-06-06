package com.example.beta;
import java.util.UUID;
import java.util.List;

/**
 * The type Recipe.
 */
public class Recipe {
    private String keyId;
    private String type;
    private String title;
    private List<String> ingredients;
    private List<String> instructions;
    private String uid;
    private String storageId;

    /**
     * Instantiates a new Recipe.
     */
    public Recipe()
    {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.keyId=uuidString;


    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Gets key id.
     *
     * @return the key id
     */
    public String getKeyId() {
        return keyId;
    }

    /**
     * Gets storage id.
     *
     * @return the storage id
     */
    public String getStorageId() {
        return storageId;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid()
    {
        return uid;
    }

    /**
     * Gets ingredients.
     *
     * @return the ingredients
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * Gets instructions.
     *
     * @return the instructions
     */
    public List<String> getInstructions() {
        return instructions;
    }

    /**
     * Sets key id.
     *
     * @param keyId the key id
     */
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Sets storage id.
     *
     * @param storageId the storage id
     */
    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets ingredients.
     *
     * @param ingredients the ingredients
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Sets instructions.
     *
     * @param instructions the instructions
     */
    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}

