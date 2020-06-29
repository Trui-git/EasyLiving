package com.trios.EasyLiving;

public class Model {

    // properties for bill
    private String name;
    private String amount;
    private String company;
    private String daysLeft;

    // properties for cart
    private String category;
    private String cartItemName;
    private String createdDate;
    private String quantity;
    private String price;
    private String grocery;
    private String daysPassed;


    public Model(String name, String amount, String company, String daysLeft) {
        this.name = name;
        this.amount = amount;
        this.company = company;
        this.daysLeft = daysLeft;
    }
    /*
            db.execSQL("CREATE TABLE IF NOT EXISTS tblCart" +
                "(cartID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "category VARCHAR, name VARCHAR, createdDate VARCHAR, quantity NUMERIC," +
                "price NUMERIC, grocery VARCHAR, status CHAR(1))");

     */

    public Model(String category, String cartItemName, String createdDate, String quantity, String price, String grocery, String daysPassed) {
        this.category = category;
        this.cartItemName = cartItemName;
        this.createdDate = createdDate;
        this.quantity = quantity;
        this.price = price;
        this.grocery = grocery;
        this.daysPassed = daysPassed;
    }

    // methodes for bill
    public String getName() {
        return name;
    }
    public String getAmount() {
        return amount;
    }
    public String getCompany() {
        return company;
    }
    public String getDaysLeft() {
        return daysLeft;
    }

    // methodes for cart
    public String getCategory() {
        return category;
    }
    public String getCartItemName() {
        return cartItemName;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    public String getQuantity() {
        return quantity;
    }
    public String getPrice() {
        return price;
    }
    public String getGrocery() {
        return grocery;
    }
    public String getDaysPassed() {
        return daysPassed;
    }
}