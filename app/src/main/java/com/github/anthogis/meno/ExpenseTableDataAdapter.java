package com.github.anthogis.meno;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ExpenseTableDataAdapter extends TableDataAdapter<Expense> {
    private static final int TEXT_SIZE = 16;

    public ExpenseTableDataAdapter(Context context, List<Expense> data) {
        super(context, data);
    }

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

        return renderedView;
    }
}
