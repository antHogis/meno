package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.anthogis.meno.views.ExpenseTable;
import com.github.anthogis.meno.views.ExpenseTableDataAdapter;

import java.util.List;

import de.codecrafters.tableview.TableView;

/**
 * A fragment which displays the individual Expenses stored in the app.
 *
 * @author Anton Höglund
 * @version 1.3
 * @since 1.0
 */
public class ExpenseListFragment extends Fragment {

    /**
     * Calls onCreate in superclass, and sets the title of MainActivity.
     * @param savedInstanceState see Android documentation for Fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_expense_list);
    }

    /**
     * Sets the content for this Fragment.
     *
     * Sets the layout for this Fragment, and sets the DataAdapter for the ExpenseTable
     * in the layout.
     *
     * @param inflater see Android documentation for Fragment
     * @param container see Android documentation for Fragment
     * @param savedInstanceState see Android documentation for Fragment
     * @return the view created by the inflater.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_expense_list, container, false);

        List<Expense> expenseList = ((MenoApplication) getActivity().getApplication())
                .getDatabaseHelper().findAllExpenses();

        TableView<Expense> expenseTable = (ExpenseTable) view.findViewById(R.id.expenseTableView);
        expenseTable.setDataAdapter(new ExpenseTableDataAdapter(view.getContext(), expenseList));

        return view;
    }
}
