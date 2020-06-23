package com.example.EasyLiving;

public class Model {

    private String name;
    private String amount;
    private String company;
    private String daysLeft;

    public Model(String name, String amount, String company, String daysLeft) {
        this.name = name;
        this.amount = amount;
        this.company = company;
        this.daysLeft = daysLeft;
    }

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
}