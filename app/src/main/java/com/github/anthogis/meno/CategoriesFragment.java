package com.github.anthogis.meno;

import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CategoriesFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private ListView categoryList;
    private EditText addCategoryField;
    private ArrayAdapter<ExpenseCategory> categoryArrayAdapter;

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
        addCategoryField = (EditText) view.findViewById(R.id.addNewCategoryText);
        Button addCategoryButton = (Button) view.findViewById(R.id.addCategoryButton);

        databaseHelper = new DatabaseHelper(view.getContext());
        categoryArrayAdapter
                = new ArrayAdapter<ExpenseCategory>(view.getContext(),
                        R.layout.adapter_expense_category_large);
        categoryArrayAdapter.addAll(databaseHelper.findAllCategories());
        categoryList.setAdapter(categoryArrayAdapter);

        addCategoryButton.setOnClickListener(this::onAddCategory);

        return view;
    }

    private void onAddCategory(View view) {
        String categoryName = addCategoryField.getText().toString();
        if (!categoryName.equals("")) {
            try {
                databaseHelper.add(new ExpenseCategory(categoryName));
                Toast.makeText(view.getContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), "Category already exists!", Toast.LENGTH_SHORT).show();
            }
        }

        categoryArrayAdapter.clear();
        categoryArrayAdapter.addAll(databaseHelper.findAllCategories());
    }
}
