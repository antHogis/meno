package com.github.anthogis.meno.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.anthogis.meno.Expense;
import com.github.anthogis.meno.R;

import java.util.Comparator;
import java.util.List;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

public class ExpenseTable extends SortableTableView<Expense> {
    public ExpenseTable(final Context context) {
        this(context, null);
    }

    public ExpenseTable(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

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

    private int expenseCategoryComparator(Expense e1,  Expense e2) {
        return e1.getCategory().getName().compareTo(e2.getCategory().getName());
    }

    private int expenseCostComparator(Expense e1, Expense e2) {
        return e1.getCost().compareTo(e2.getCost());
    }

    private int expenseDateComparator(Expense e1, Expense e2) {
        return e1.getDate().compareTo(e2.getDate());
    }
}
