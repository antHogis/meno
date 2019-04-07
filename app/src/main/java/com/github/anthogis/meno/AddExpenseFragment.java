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

public class AddExpenseFragment extends Fragment {

    private ExpenseCategorySpinner categorySpinner;
    private EditText costField;
    private EditText dateField;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_add_expense);
    }

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

    private void onDateFocus(View view, boolean hasFocus) {
        if (hasFocus) {
            DialogFragment dateFragment = new MyDatePickerFragment();
            dateFragment.show(getActivity().getSupportFragmentManager(),
                    getResources().getString(R.string.id_date_picker));
        }
    }

    private void onDateClicked(View view) {
        onDateFocus(view, true);
    }
    
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

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public static class MyDatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this::dateSetAction, year, month, day);
        }

        private void dateSetAction(DatePicker view, int year, int month, int day) {
            EditText dateField = (EditText) getActivity().findViewById(R.id.editDateText);
            String date = DateHelper.stringOf(year, month, day);
            dateField.setText(date);
        }
    }

}
