package com.github.anthogis.meno;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * A central class to the app, which represents an Expense the user wants to keep track of.
 *
 * An Expense contains basic information of a persons single Expense, like what sort of Expense,
 * that is ExpenseCategory the Expense belongs to, how much it's cost is, and the date when the
 * Expense occurred.
 *
 * @author Anton Höglund
 * @version 1.3
 * @since 1.0
 */
public class Expense implements Serializable {

    /**
     * The id of the expense, used in database identification.
     */
    private Integer id;

    /**
     * The category the expense is tagged with.
     */
    private ExpenseCategory category;

    /**
     * The cost of the Expense.
     */
    private BigDecimal cost;

    /**
     * The date when the expense occurred.
     */
    private Date date;

    /**
     * Empty constructor, does nothing.
     */
    public Expense() {}

    /**
     * Constructs an Expense.
     *
     * @param category the ExpenseCategory of the Expense.
     * @param cost the cost of the Expense.
     * @param date the date when the Expense occurred.
     * @throws IllegalArgumentException if one or more of the arguments was null.
     */
    public Expense(@NonNull ExpenseCategory category,
                   @NonNull BigDecimal cost,
                   @NonNull Date date) throws IllegalArgumentException {
        setCategory(category);
        setCost(cost);
        setDate(date);
    }

    /**
     * Returns the ExpenseCategory of the Expense.
     * @return the ExpenseCategory of the Expense.
     */
    public ExpenseCategory getCategory() {
        return category;
    }

    /**
     * Sets the ExpenseCategory of the Expense.
     * @param category the ExpenseCategory for the Expense.
     * @throws IllegalArgumentException if the ExpenseCategory provided was null.
     */
    public void setCategory(ExpenseCategory category) throws IllegalArgumentException{
        if (category == null) {
            throw new IllegalArgumentException("Expense does not allow null values");
        } else {
            this.category = category;
        }
    }

    /**
     * Returns the cost of the Expense.
     * @return the cost of the Expense.
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Sets the cost of the Expense.
     * @param cost the cost for the Expense.
     * @throws IllegalArgumentException if the provided cost was null.
     */
    public void setCost(BigDecimal cost) throws IllegalArgumentException {
        if (cost == null) {
            throw new IllegalArgumentException("Expense does not allow null values");
        } else {
            this.cost = cost;
        }
    }

    /**
     * Returns the date for the Expense.
     * @return the date for the Expense.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the Expense.
     *
     * @param date the date for the Expense.
     * @throws IllegalArgumentException if the given date was null.
     */
    public void setDate(Date date) throws IllegalArgumentException {
        if (date == null) {
            throw new IllegalArgumentException("Expense does not allow null values");
        } else {
            this.date = date;
        }
    }

    /**
     * Returns the id of the Expense.
     * @return the id of the Expense.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the Expense.
     * @param id the id for the Expense.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Creates a String representation of the Expense and its values, for debugging purposes.
     * @return the String representation of the Expense and its values.
     */
    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.toString())
                .append(" {")
                .append("ID:").append(id).append(", ")
                .append("Category:").append(category.getName()).append(", ")
                .append("Cost:").append(cost.toString()).append(", ")
                .append("Date:").append(DateHelper.stringOf(date))
                .append("}")
                .toString();
    }
}
