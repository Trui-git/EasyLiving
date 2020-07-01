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

    // properties for todo
    private String task;
    private String where;
    private String time;

    // model for bill
    public Model(String name, String amount, String company, String daysLeft) {
        this.name = name;
        this.amount = amount;
        this.company = company;
        this.daysLeft = daysLeft;
    }

    // model for cart
    public Model(String category, String cartItemName, String createdDate, String quantity, String price, String grocery, String daysPassed) {
        this.category = category;
        this.cartItemName = cartItemName;
        this.createdDate = createdDate;
        this.quantity = quantity;
        this.price = price;
        this.grocery = grocery;
        this.daysPassed = daysPassed;
    }

    // model for todo
    public Model(String task, String where, String time) {
        this.task = task;
        this.where = where;
        this.time = time;
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

    // methode for todo
    public String getTask() {
        return task;
    }
    public String getWhere() {
        return where;
    }
    public String getTime() {
        return time;
    }
}