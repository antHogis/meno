package com.github.anthogis.meno;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Expense implements Serializable {

    private ExpenseCategory category;

    private BigDecimal cost;

    private Date date;

    public Expense() {

    }

    public Expense(@NonNull ExpenseCategory category,
                   @NonNull BigDecimal cost,
                   @NonNull Date date) throws IllegalArgumentException {
        setCategory(category);
        setCost(cost);
        setDate(date);
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) throws IllegalArgumentException{
        if (category == null) {
            throw new IllegalArgumentException("Expense does not allow null values");
        } else {
            this.category = category;
        }
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) throws IllegalArgumentException {
        if (cost == null) {
            throw new IllegalArgumentException("Expense does not allow null values");
        } else {
            this.cost = cost;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) throws IllegalArgumentException {
        if (date == null) {
            throw new IllegalArgumentException("Expense does not allow null values");
        } else {
            this.date = date;
        }
    }
}
