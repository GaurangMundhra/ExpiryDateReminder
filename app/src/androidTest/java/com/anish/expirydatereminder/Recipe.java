package com.anish.expirydatereminder;
public class Recipe {
    private int id;
    private String name;
    private String ingredients;
    private String preparation;

    // Constructors
    public Recipe() {
    }

    public Recipe(String name, String ingredients, String preparation) {
        this.name = name;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }
}

