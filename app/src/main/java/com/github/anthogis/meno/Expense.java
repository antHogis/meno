package com.github.anthogis.meno;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {

    private Category category;

    private BigDecimal cost;

    private LocalDate date;

    public Expense() {

    }

    public Expense(@NonNull Category category, @NonNull BigDecimal cost, @NonNull LocalDate date) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
