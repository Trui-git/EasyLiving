package com.trios.EasyLiving;

public class MedicineModel {
    private String name;
    private String purpose;
    private String instructions;

    public MedicineModel(String name, String purpose, String instructions) {
        this.name = name;
        this.purpose = purpose;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }
    public String getPurpose() {
        return purpose;
    }
    public String getInstructions() {
        return instructions;
    }
}
