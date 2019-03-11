package com.github.anthogis.meno;

import java.io.Serializable;

public class ExpenseCategory implements Serializable {
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
