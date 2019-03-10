package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;

public class AddExpenseFragment extends Fragment {

    private EditText categoryField;
    private EditText costField;
    private Button addExpenseButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_add_expense);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        categoryField = (EditText) view.findViewById(R.id.editCategoryText);
        costField = (EditText) view.findViewById(R.id.editCostText);
        addExpenseButton = (Button) view.findViewById(R.id.addExpenseButton);

        addExpenseButton.setOnClickListener(this::onAddExpenseClicked);

        return view;
    }
    
    private void onAddExpenseClicked(View view) {
        boolean addable = false;
        String toastMessage = "";
        String category;
        BigDecimal cost;

        try {
            if ((category = categoryField.getText().toString()).equals("")) {
                throw new EmptyFieldException();
            }

            cost = new BigDecimal(costField.getText().toString());

            addable = true;
        } catch (NumberFormatException e) {
            toastMessage = getResources().getString(R.string.toast_expense_add_failure_invalid_cost);
        } catch (EmptyFieldException e) {
            toastMessage = getResources().getString(R.string.toast_expense_add_failure_empty_fields);
        }

        if (addable) {
            //Implement call to persist Expense
            toastMessage = getResources().getString(R.string.toast_expense_add_success);
        }

        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }
}
