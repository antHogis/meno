package com.github.anthogis.meno.exceptions;

/**
 * An exception thrown when an ExpenseCategory attributed to an Expense is invalid.
 *
 * An exception thrown when a category attributed to an Expense is invalid. This would mean that
 * an ExpenseCategory attempted to be attributed to an Expense does not exist in the table
 * of ExpenseCategories in the database.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class InvalidCategoryException extends RuntimeException {
}
