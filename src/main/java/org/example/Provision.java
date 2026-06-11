package org.example;

/**
 * Класс провианта — запасы, хранящиеся в пещере.
 */
public class Provision {
    private String name;
    private String lore;
    private int quantity;
    private int nutritionValue;

    public Provision(String name, String lore, int quantity, int nutritionValue) {
        this.name = name;
        this.lore = lore;
        this.quantity = quantity;
        this.nutritionValue = nutritionValue;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getNutritionValue() {
        return nutritionValue;
    }

    public String getLoreDescription() {
        return "📦 " + name + "\n   " + lore + "\n   Количество: " + quantity + ", Питательность: " + nutritionValue;
    }
}
