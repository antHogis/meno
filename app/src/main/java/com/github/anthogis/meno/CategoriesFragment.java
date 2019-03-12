package com.github.anthogis.meno;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class CategoriesFragment extends Fragment {

    private List<ExpenseCategory> categories;
    private DatabaseHelper databaseHelper;
    private ListView categoryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_categories);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categoryList = (ListView) view.findViewById(R.id.categoryList);

        databaseHelper = new DatabaseHelper(view.getContext());
        categories = databaseHelper.findAllCategories();
        ArrayAdapter<ExpenseCategory> categoryArrayAdapter
                = new ArrayAdapter<ExpenseCategory>(view.getContext(),
                        R.layout.adapter_expense_category_large);
        categoryArrayAdapter.addAll(categories);
        categoryList.setAdapter(categoryArrayAdapter);

        return view;
    }
}
