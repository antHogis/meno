package com.github.anthogis.meno;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Expense {

    private Category category;

    private BigDecimal cost;

    private Date date;

    public Expense() {

    }

    public Expense(@NonNull Category category, @NonNull BigDecimal cost, @NonNull Date date) {
        this.category = category;
        this.cost = cost;
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
