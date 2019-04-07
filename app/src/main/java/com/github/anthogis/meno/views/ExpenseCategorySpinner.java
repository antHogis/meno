package com.github.anthogis.meno.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.anthogis.meno.ExpenseCategory;
import com.github.anthogis.meno.R;
import com.github.anthogis.meno.exceptions.InvalidCategoryException;

import java.util.List;

public class ExpenseCategorySpinner extends AppCompatSpinner {

    private ExpenseCategory selectedCategory;

    public ExpenseCategorySpinner(Context context) {
        this(context, null);
    }

    public ExpenseCategorySpinner(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.spinnerStyle);
    }

    public ExpenseCategorySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectedCategory = null;
        setOnItemSelectedListener(new OnExpenseCategorySelectedListener());
    }

    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        String categoryHint = getResources().getString(R.string.hint_category);
        expenseCategories.add(0, new ExpenseCategory(categoryHint));
        this.setAdapter(new ExpenseCategorySpinnerArrayAdapter(
                getContext(),
                R.layout.adapter_expense_category_small,
                expenseCategories
        ));
    }

    public ExpenseCategory getSelectedCategory() throws InvalidCategoryException {
        if (selectedCategory != null) {
            return selectedCategory;
        } else {
            throw new InvalidCategoryException();
        }
    }

    private class OnExpenseCategorySelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                selectedCategory = (ExpenseCategory) parent.getItemAtPosition(position);
            } else {
                selectedCategory = null;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ExpenseCategorySpinnerArrayAdapter extends ArrayAdapter<ExpenseCategory> {

        public ExpenseCategorySpinnerArrayAdapter(Context context, int resource, List<ExpenseCategory> objects) {
            super(context, resource, objects);
        }

        @Override
        public boolean isEnabled(int position) {
            return position != 0;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView textView = (TextView) view;
            textView.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
            return view;
        }
    }
}
