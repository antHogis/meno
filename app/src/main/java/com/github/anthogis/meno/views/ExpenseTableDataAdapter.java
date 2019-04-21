package com.github.anthogis.meno.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.anthogis.meno.DateHelper;
import com.github.anthogis.meno.Expense;
import com.github.anthogis.meno.R;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * TableDataAdapter for ExpenseTable.
 *
 * TableDataAdapter for ExpenseTable, which shows data of Expenses in a three-column view of TextViews.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class ExpenseTableDataAdapter extends TableDataAdapter<Expense> {

    /**
     * The size of the text in the TextViews of the ExpenseTable.
     */
    private static final int TEXT_SIZE = 16;

    /**
     * Calls the super constructor with parameters Context, List< ? >
     * @param context the context of the TableDataAdapter.
     * @param data the data in the TableDataAdapter.
     */
    public ExpenseTableDataAdapter(Context context, List<Expense> data) {
        super(context, data);
    }

    /**
     * Sets the data for a cell in the ExpenseTable.
     *
     * Sets the data for a cell in the ExpenseTable.
     * Sets the ExpenseCategory of the Expense to a cell in column 0, the cost of the Expense to
     * a cell in column 1, and the date of the expense in column 2.
     *
     * @param rowIndex see documentation for de.codecrafters.tableview.TableDataAdapter.
     * @param columnIndex see documentation for de.codecrafters.tableview.TableDataAdapter.
     * @param parentView see documentation for de.codecrafters.tableview.TableDataAdapter.
     * @return see documentation for de.codecrafters.tableview.TableDataAdapter.
     */
    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Expense expense = getRowData(rowIndex);
        TextView renderedView = new TextView(getContext());
        String text = "";

        switch (columnIndex) {
            case 0:
                text = expense.getCategory().getName();
                break;
            case 1:
                text = expense.getCost().toString();
                break;
            case 2:
                text = DateHelper.stringOf(expense.getDate());
                break;
        }

        renderedView.setText(text);
        renderedView.setTextSize(TEXT_SIZE);
        renderedView.setTextColor(ContextCompat.getColor(getContext(), R.color.table_color_text));

        return renderedView;
    }
}
