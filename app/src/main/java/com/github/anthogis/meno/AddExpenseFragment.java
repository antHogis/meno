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

public class AddExpenseFragment extends Fragment implements View.OnClickListener {

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addExpenseButton:
                onAddExpenseClicked(view);
                break;
        }
    }

    private void onAddExpenseClicked(View view) {
        boolean addable = false;
        String toastMessage;

        if (addable) {
            toastMessage = "foo";
        } else {
            toastMessage = "bar";
        }

        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }
}
