package com.github.anthogis.meno.exceptions;

/**
 * An exception thrown when an ExpenseCategory is attempted to be deleted when it is required for an existing Expense.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class CategoryReferencedException extends Exception {
}
