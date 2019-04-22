package com.github.anthogis.meno.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.anthogis.meno.Expense;
import com.github.anthogis.meno.R;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

/**
 * A graphical sortable table for Expenses.
 *
 * @author Anton Höglund
 * @version 1.3
 * @since 1.0
 */
public class ExpenseTable extends SortableTableView<Expense> {

    /**
     * Constructs an ExpenseTable with constructor Context, AttributeSet
     * @param context see documentation for de.codecrafters.tableview.SortableTableView.
     */
    public ExpenseTable(final Context context) {
        this(context, null);
    }

    /**
     * Constructs an ExpenseTable with constructor Context, Attributeset, int
     * @param context see documentation for de.codecrafters.tableview.SortableTableView.
     * @param attributes see documentation for de.codecrafters.tableview.SortableTableView.
     */
    public ExpenseTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    /**
     * Constructs an ExpenseTable with super constructor, sets headers, columns, and row coloring.
     *
     * Constructs an ExpenseTable with super constructor with params Context, AttributeSet, int.
     * Sets headers, column comparators, and row coloring for the table.
     *
     * @param context see documentation for de.codecrafters.tableview.SortableTableView.
     * @param attributes see documentation for de.codecrafters.tableview.SortableTableView.
     * @param styleAttributes see documentation for de.codecrafters.tableview.SortableTableView.
     */
    public ExpenseTable(final Context context,
                        final AttributeSet attributes,
                        final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(
                getContext(),
                R.string.table_header_category,
                R.string.table_header_cost,
                R.string.table_header_date);
        headerAdapter.setTextColor(ContextCompat.getColor(getContext(), R.color.iceWhite));
        setHeaderAdapter(headerAdapter);

        setColumnComparator(0, this::expenseCategoryComparator);
        setColumnComparator(1, this::expenseCostComparator);
        setColumnComparator(2, this::expenseDateComparator);

        final int rowColorEven = ContextCompat.getColor(getContext(), R.color.table_color_even);
        final int rowColorOdd  = ContextCompat.getColor(getContext(), R.color.table_color_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders
                .alternatingRowColors(rowColorEven, rowColorOdd));
    }

    /**
     * Implementation of Comparator for comparing the ExpenseCategories of two Expenses.
     *
     * @param e1 the first Expense to be compared.
     * @param e2 the second Expense to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    private int expenseCategoryComparator(Expense e1,  Expense e2) {
        return e1.getCategory().getName().compareTo(e2.getCategory().getName());
    }

    /**
     * Implementation of Comparator for comparing the Costs of two Expenses.
     *
     * @param e1 the first Expense to be compared.
     * @param e2 the second Expense to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    private int expenseCostComparator(Expense e1, Expense e2) {
        return e1.getCost().compareTo(e2.getCost());
    }

    /**
     * Implementation of Comparator for comparing the Dates of two Expenses.
     *
     * @param e1 the first Expense to be compared.
     * @param e2 the second Expense to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    private int expenseDateComparator(Expense e1, Expense e2) {
        return e1.getDate().compareTo(e2.getDate());
    }
}
