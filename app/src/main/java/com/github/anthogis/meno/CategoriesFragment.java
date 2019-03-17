package com.github.anthogis.meno;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.anthogis.meno.views.ButtonState;
import com.github.anthogis.meno.views.StatefulButton;

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
        categoryList.setOnItemLongClickListener(this::onCategoryLongClick);

        addCategoryButton.setOnClickListener(this::onAddCategory);

        return view;
    }

    private void onAddCategory(View view) {
        String categoryName = addCategoryField.getText().toString();
        String toastMessage;

        if (!categoryName.equals("")) {
            try {
                databaseHelper.add(new ExpenseCategory(categoryName));
                reloadAdapter();
                toastMessage =  getString(R.string.toast_category_add_success);
            } catch (SQLException e) {
                toastMessage = getString(R.string.toast_category_add_failure_duplicate);
            }
        } else {
            toastMessage = getString(R.string.toast_category_add_failure_empty);
        }

        Toast.makeText(view.getContext(),
                toastMessage,
                Toast.LENGTH_SHORT).show();
    }

    private boolean onCategoryLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();

        EditCategoryDialog dialog = new EditCategoryDialog();
        Bundle argBundle = new Bundle();
        argBundle.putString("CategoryName", categoryArrayAdapter.getItem(position).getName());
        dialog.setArguments(argBundle);
        dialog.show(getActivity().getSupportFragmentManager(), "foo_bar");

        return true;
    }

    private void reloadAdapter() {
        categoryArrayAdapter.clear();
        categoryArrayAdapter.addAll(databaseHelper.findAllCategories());
    }

    private void deleteCategory(ExpenseCategory category) {

    }

    public static class EditCategoryDialog extends AppCompatDialogFragment {
        private static String categoryName;
        private static StatefulButton executeEditButton;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_edit_category, container);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getDialog().setTitle(getString(R.string.title_rename_delete_category));

            EditText renameCategoryField = (EditText) view.findViewById(R.id.renameCategoryText);
            Button cancelButton = (Button) view.findViewById(R.id.dialog_edit_category_cancel);
            executeEditButton = (StatefulButton) view.findViewById(R.id.dialog_edit_category_execute);

            categoryName = getArguments().getString("CategoryName");
            renameCategoryField.setText(categoryName);
            renameCategoryField.addTextChangedListener(new DialogTextWatcher());

            cancelButton.setOnClickListener((v) -> dismiss());
            executeEditButton.setOnClickListener(this::onExecuteEdit);
        }

        private void onExecuteEdit(View view) {
            String toastMessage;

            if (executeEditButton.getState().equals(ButtonState.DELETE)) {
                toastMessage = "Category deleted";

                dismiss();
            } else {
                toastMessage = "Category renamed";
                dismiss();
            }

            Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }

        private static class DialogTextWatcher implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();

                if (text.equals(categoryName) || text.equals("")) {
                    executeEditButton.setState(ButtonState.DELETE);
                } else {
                    executeEditButton.setState(ButtonState.RENAME);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        }
    }
}