package com.github.anthogis.meno.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
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

/**
 * A Spinner for selecting ExpenseCategories.
 *
 * A Spinner for selecting ExpenseCategories. Implemented in order to allow a "hint" to the
 * category, that is an element in the Spinner that cannot be selected.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class ExpenseCategorySpinner extends AppCompatSpinner {

    /**
     * The selected category.
     */
    private ExpenseCategory selectedCategory;

    /**
     * Calls constructor with params Context, AttributeSet.
     *
     * @param context see Android Documentation for AppCompatSpinner.
     */
    public ExpenseCategorySpinner(Context context) {
        this(context, null);
    }

    /**
     * Calls constructor with params Context, AttributeSet, int.
     *
     * @param context see Android Documentation for AppCompatSpinner.
     * @param attrs see Android Documentation for AppCompatSpinner.
     */
    public ExpenseCategorySpinner(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.spinnerStyle);
    }

    /**
     * Calls super constructor with params Context, AttributeSet, int. Sets selectedCategory to null and sets OnItemSelectedListener.
     *
     * @param context see Android Documentation for AppCompatSpinner.
     * @param attrs see Android Documentation for AppCompatSpinner.
     * @param defStyleAttr see Android Documentation for AppCompatSpinner.
     */
    public ExpenseCategorySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectedCategory = null;
        setOnItemSelectedListener(new OnExpenseCategorySelectedListener());
    }

    /**
     * Sets the ExpenseCategory data to be displayed in this Spinner.
     * @param expenseCategories the data.
     */
    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        String categoryHint = getResources().getString(R.string.hint_category);
        expenseCategories.add(0, new ExpenseCategory(categoryHint));
        this.setAdapter(new ExpenseCategorySpinnerArrayAdapter(
                getContext(),
                R.layout.adapter_expense_category_small,
                expenseCategories
        ));
    }

    /**
     * Returns the selected ExpenseCategory.
     * @return the selected ExpenseCategory.
     * @throws InvalidCategoryException if no ExpenseCategory has been selected (the default selected/first value is meant to be a hint, and does not count as selected)
     */
    public ExpenseCategory getSelectedCategory() throws InvalidCategoryException {
        if (selectedCategory != null) {
            return selectedCategory;
        } else {
            throw new InvalidCategoryException();
        }
    }

    /**
     * Listener for listening to selection of items (ExpenseCategories) in this Spinner.
     */
    private class OnExpenseCategorySelectedListener implements AdapterView.OnItemSelectedListener {

        /**
         * Sets the attribute selectedCategory of parent class ExpenseCategorySpinner when an item is selected, if it is not the first one.
         * @param parent see Android documentation for AdapterView.OnItemSelectedListener.
         * @param view see Android documentation for AdapterView.OnItemSelectedListener.
         * @param position see Android documentation for AdapterView.OnItemSelectedListener.
         * @param id see Android documentation for AdapterView.OnItemSelectedListener.
         */
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                selectedCategory = (ExpenseCategory) parent.getItemAtPosition(position);
            } else {
                selectedCategory = null;
            }
        }

        /**
         * Mandatory method to implement, does nothing.
         * @param parent see Android documentation for AdapterView.OnItemSelectedListener.
         */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /**
     * An ArrayAdapter containing the ExpenseCategory data for this Spinner.
     */
    private class ExpenseCategorySpinnerArrayAdapter extends ArrayAdapter<ExpenseCategory> {

        /**
         * Calls super constructor with params Context, int, List< ? >
         * @param context see Android documentation for ArrayAdapter.
         * @param resource see Android documentation for ArrayAdapter.
         * @param objects see Android documentation for ArrayAdapter.
         */
        public ExpenseCategorySpinnerArrayAdapter(Context context, int resource, List<ExpenseCategory> objects) {
            super(context, resource, objects);
        }

        /**
         * Returns true if the item at the specified position is not the first one.
         * @param position the position of the item.
         * @return true if the item at the specified position is not the first one.
         */
        @Override
        public boolean isEnabled(int position) {
            return position != 0;
        }

        /**
         * Returns the Spinner View, after editing the text color.
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView,ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            if (position == 0) {
                ((TextView) view).setTextColor(ResourcesCompat.getColor(getResources(), R.color.hintColor, null));
            } else {
                ((TextView) view).setTextColor(Color.BLACK);
            }

            return view;
        }

        /**
         * Returns the drop down view of the ArrayAdapter. Alters the color of the first TextView in the drop down view to gray.
         * @param position see Android documentation for ArrayAdapter.
         * @param convertView see Android documentation for ArrayAdapter.
         * @param parent see Android documentation for ArrayAdapter.
         * @return the drop down view.
         */
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView textView = (TextView) view;
            textView.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
            return view;
        }
    }
}
