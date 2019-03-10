package com.github.anthogis.meno;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExpenseCategoryManager {

    public static ArrayAdapter<ExpenseCategory> getAdapter(View view) {
        ArrayAdapter<ExpenseCategory> adapter
                = new ArrayAdapter<>(view.getContext(), R.layout.adapter_expense_category);

        adapter.add(new ExpenseCategory("Foo"));
        adapter.add(new ExpenseCategory("Bar"));
        adapter.add(new ExpenseCategory("Biz"));

        return adapter;
    }

}
