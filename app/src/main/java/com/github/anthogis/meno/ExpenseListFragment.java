package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class ExpenseListFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_expense_list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_expense_list, container, false);

        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        for (Expense expense : databaseHelper.findAllExpenses()) {
            Log.d("MENO-DEBUG", expense.toString());
        }

        final String[][] DATA_TO_SHOW = { { "This", "is", "a", "test" },
                { "and", "a", "second", "test" } };

        TableView<String[]> testTable = (TableView<String[]>) view.findViewById(R.id.expenseTableView);
        testTable.setDataAdapter(new SimpleTableDataAdapter(view.getContext(), DATA_TO_SHOW));

        return view;
    }
}
