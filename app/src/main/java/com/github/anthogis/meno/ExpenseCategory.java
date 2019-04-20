package com.github.anthogis.meno;

import java.io.Serializable;

/**
 * Depicts the category of an Expense.
 *
 * @author Anton Höglund
 * @version 1.3
 * @since 1.0
 */
public class ExpenseCategory implements Serializable {

    /**
     * The name of the category.
     */
    private String name;

    /**
     * The id of the category in database use.
     */
    private int id;

    /**
     * Empty constructor, does nothing.
     */
    public ExpenseCategory() {
    }

    /**
     * Constructor for setting the name of the ExpenseCategory.
     * @param name the name that the ExpenseCategory is set to.
     */
    public ExpenseCategory(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this ExpenseCategory.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this ExpenseCategory.
     * @param name the name that the ExpenseCategory is set to.name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id of this ExpenseCategory
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of this ExpenseCategory.
     * @param id the new id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of this ExpenseCategory.
     * @return the name of this ExpenseCategory.
     */
    @Override
    public String toString() {
        return name;
    }
}
