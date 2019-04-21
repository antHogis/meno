package com.github.anthogis.meno;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.anthogis.meno.exceptions.EmptyFieldException;
import com.github.anthogis.meno.exceptions.InvalidCategoryException;
import com.github.anthogis.meno.views.ExpenseCategorySpinner;

import java.math.BigDecimal;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * A fragment in which the user can create a new Expense.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class AddExpenseFragment extends Fragment {

    /**
     * Spinner in the layout of the Fragment for selecting an ExpenseCategory for the Expense.
     */
    private ExpenseCategorySpinner categorySpinner;

    /**
     * An input field in the layout of the Fragment for setting the cost of the Expense.
     */
    private EditText costField;

    /**
     * An input field in the layout of the Fragment for setting the date of an Expense.
     */
    private EditText dateField;

    /**
     * The DatabaseHelper used to persist Expenses.
     */
    private DatabaseHelper databaseHelper;

    /**
     * Calls superclass onCreate and sets the title of the Activity this fragment is contained in.
     *
     * @param savedInstanceState see Android documentation for Fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_add_expense);
    }

    /**
     * Creates the content and sets listeners for this fragment.
     *
     * @param inflater see Android documentation for Fragment.
     * @param container see Android documentation for Fragment.
     * @param savedInstanceState see Android documentation for Fragment.
     * @return the view for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        categorySpinner = (ExpenseCategorySpinner) view.findViewById(R.id.categorySpinner);
        costField = (EditText) view.findViewById(R.id.editCostText);
        dateField = (EditText) view.findViewById(R.id.editDateText);
        Button addExpenseButton = (Button) view.findViewById(R.id.addExpenseButton);

        addExpenseButton.setOnClickListener(this::onAddExpenseClicked);
        dateField.setOnFocusChangeListener(this::onDateFocus);
        dateField.setOnClickListener(this::onDateClicked);

        databaseHelper = ((MenoApplication) getActivity().getApplication()).getDatabaseHelper();
        categorySpinner.setExpenseCategories(databaseHelper.findAllCategories());

        return view;
    }

    /**
     * Implementation of OnFocusChangeListener for dateField, opens a dialog for setting the date for an Expense.
     * @param view see Android documentation for View.OnFocusChangeListener
     * @param hasFocus see Android documentation for View.OnFocusChangeListener
     */
    private void onDateFocus(View view, boolean hasFocus) {
        if (hasFocus) {
            DialogFragment dateFragment = new MyDatePickerFragment();
            dateFragment.show(getActivity().getSupportFragmentManager(),
                    getResources().getString(R.string.id_date_picker));
        }
    }

    /**
     * Implementation of OnClickListener for dateField, calls onDateFocus.
     * @param view see Android Documentation for View.OnClickListener.
     */
    private void onDateClicked(View view) {
        onDateFocus(view, true);
    }

    /**
     * Implementation of OnClickListener for the button clicked when the user wants to save the Expense.
     *
     * Implementation of OnClickListener for the button clicked when the user wants to save the Expense.
     * Persist the Expense with DatabaseHelper if all the data of the Expense is valid, otherwise
     * the user is informed of the error via Toast.
     * @param view see Android documentation of View.OnClickListener.
     */
    private void onAddExpenseClicked(View view) {
        boolean addable = false;
        String toastMessage = "";

        ExpenseCategory category = null;
        BigDecimal cost = null;
        Date date       = null;
        Expense expense = null;

        try {
            category = categorySpinner.getSelectedCategory();
            cost = new BigDecimal(costField.getText().toString());
            String dateText = dateField.getText().toString();

            if (dateText.equals("")) {
                date = DateHelper.now();
            } else {
                date = DateHelper.parse(dateField);
            }

            expense = new Expense(category, cost, date);
            addable = true;
        } catch (NumberFormatException e) {
            toastMessage = getResources().getString(R.string.toast_expense_add_failure_invalid_cost);
        } catch (EmptyFieldException e) {
            toastMessage = getResources().getString(R.string.toast_expense_add_failure_empty_fields);
        } catch (ParseException e) {
            toastMessage = getResources().getString(R.string.toast_expense_add_failure_invalid_date);
        } catch (InvalidCategoryException e) {
            toastMessage = getResources().getString(R.string.toast_expense_add_failure_invalid_category);
        }

        if (addable) {
            databaseHelper.add(expense);
            toastMessage = getResources().getString(R.string.toast_expense_add_success);
            hideSoftKeyboard();
            ((MenoApplication) getActivity().getApplication()).vibrateSuccess();
        } else {
            ((MenoApplication) getActivity().getApplication()).vibrateError();
        }

        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Hides the keyboard displayed in the UI.
     */
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * A dialog for picking the date for the new Expense from a calendar.
     */
    public static class MyDatePickerFragment extends DialogFragment {

        /**
         * Sets the pre-selected date for the calendar, and the listener for when the date is picked.
         * @param savedInstanceState see Android documentation for DialogFragment.onCreateDialog.
         * @return see Android documentation for DialogFragment.onCreateDialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this::dateSetAction, year, month, day);
        }

        /**
         * Implementation of DatePickerDialog.OnDateSetListener, adds the date picked in a string format to dateField.
         * @param view see Android Documentation of DatePickerDialog.OnDateSetListener.
         * @param year see Android Documentation of DatePickerDialog.OnDateSetListener.
         * @param month see Android Documentation of DatePickerDialog.OnDateSetListener.
         * @param day see Android Documentation of DatePickerDialog.OnDateSetListener.
         */
        private void dateSetAction(DatePicker view, int year, int month, int day) {
            EditText dateField = (EditText) getActivity().findViewById(R.id.editDateText);
            String date = DateHelper.stringOf(year, month, day);
            dateField.setText(date);
        }
    }

}
