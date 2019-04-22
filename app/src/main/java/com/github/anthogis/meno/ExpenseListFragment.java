package com.github.anthogis.meno;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
     * TODO javadoc
     */
    private DatabaseHelper databaseHelper;

    /**
     * TODO javadoc
     */
    private TableView<Expense> expenseTable;

    /**
     * TODO javadoc
     */
    private Expense selectedExpense = null;

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
        databaseHelper = ((MenoApplication) getActivity().getApplication()).getDatabaseHelper();

        expenseTable = (ExpenseTable) view.findViewById(R.id.expenseTableView);
        expenseTable.addDataClickListener(this::onTableRowClick);
        reloadTableData();

        return view;
    }

    private void reloadTableData() {
        expenseTable.setDataAdapter(new ExpenseTableDataAdapter(getContext(),
                databaseHelper.findAllExpenses()));
    }

    /**
     * TODO javadoc
     * @param rowIndex
     * @param clickedExpense
     */
    private void onTableRowClick(final int rowIndex, final Expense clickedExpense) {
        selectedExpense = clickedExpense;

        String expenseInfo = new StringBuilder()
                .append(getResources().getString(R.string.table_header_category))
                .append(": ")
                .append(clickedExpense.getCategory().getName())
                .append('\n')
                .append(getResources().getString(R.string.table_header_cost))
                .append(": ")
                .append(clickedExpense.getCost().toString())
                .append('\n')
                .append(getResources().getString(R.string.table_header_date))
                .append(": ")
                .append(DateHelper.stringOf(clickedExpense.getDate()))
                .toString();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setTitle(R.string.title_delete_expense_dialog)
                .setMessage(expenseInfo)
                .setPositiveButton(R.string.button_delete, this::deleteTableClickListener)
                .setNegativeButton(R.string.button_cancel, this::deleteTableClickListener)
                .show();
    }

    /**
     * TODO javadoc
     * @param dialog
     * @param which
     */
    private void deleteTableClickListener(DialogInterface dialog, int which) {
        boolean dismiss = false;
        boolean reload = false;

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                databaseHelper.deleteExpense(selectedExpense);
                reload = true;
                dismiss = true;
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dismiss = true;
                break;
        }

        selectedExpense = null;

        if (reload) {
            reloadTableData();
        }

        if (dismiss) {
            dialog.dismiss();
        }
    }
}
