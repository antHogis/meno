package com.github.anthogis.meno;

import java.io.Serializable;

public class ExpenseCategory implements Serializable {
    private String name;
    private int id;
    private boolean deleted;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return name;
    }
}
