package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ExpenseListFragment extends Fragment {

    private TableView<Expense> expenseTable;
    private List<Expense> expenseList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_expense_list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_expense_list, container, false);

        expenseList = new DatabaseHelper(view.getContext()).findAllExpenses();
        SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(
                view.getContext(),
                R.string.table_header_category,
                R.string.table_header_cost,
                R.string.table_header_date);
        headerAdapter.setTextColor(ContextCompat.getColor(view.getContext(), R.color.iceWhite));

        expenseTable = (TableView<Expense>) view.findViewById(R.id.expenseTableView);
        expenseTable.setDataAdapter(new ExpenseTableDataAdapter(view.getContext(), expenseList));
        expenseTable.setHeaderAdapter(headerAdapter);

        return view;
    }
}
