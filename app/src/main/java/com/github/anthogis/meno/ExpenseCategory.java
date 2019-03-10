package com.github.anthogis.meno;

public class ExpenseCategory {
    private String name;

    public ExpenseCategory() {
    }

    public ExpenseCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
