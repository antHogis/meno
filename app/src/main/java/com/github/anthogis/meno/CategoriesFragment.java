package com.github.anthogis.meno;

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

import com.github.anthogis.meno.exceptions.CategoryReferencedException;
import com.github.anthogis.meno.exceptions.SimilarCategoryExistsException;
import com.github.anthogis.meno.views.ButtonState;
import com.github.anthogis.meno.views.StatefulButton;

/**
 * A fragment in which the user can create, update, and delete Categories to be used for Expenses.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class CategoriesFragment extends Fragment {

    /**
     * The DatabaseHelper used to persist categories.
     */
    private DatabaseHelper databaseHelper;

    /**
     * The input field for adding categories.
     */
    private EditText addCategoryField;

    /**
     * ArrayAdapter for displaying array data in a graphical list.
     */
    private ArrayAdapter<ExpenseCategory> categoryArrayAdapter;

    /**
     * An action to run a vibration when a user-facilitated operation is not successful.
     */
    private Runnable vibrateError;

    /**
     * An action to run a vibration when a user-facilitated operation is successful.
     */
    private Runnable vibrateSuccess;

    /**
     * Calls superclass onCreate and sets the title of the Activity this fragment is contained in.
     *
     * @param savedInstanceState see Android documentation for Fragment.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_categories);
    }

    /**
     * Creates the content and sets listeners for this fragment.
     *
     * @param inflater see Android documentation for Fragment.
     * @param container see Android documentation for Fragment.
     * @param savedInstanceState see Android documentation for Fragment.
     * @return the view for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ListView categoryList = (ListView) view.findViewById(R.id.categoryList);
        addCategoryField = (EditText) view.findViewById(R.id.addNewCategoryText);
        Button addCategoryButton = (Button) view.findViewById(R.id.addCategoryButton);

        databaseHelper = ((MenoApplication) getActivity().getApplication()).getDatabaseHelper();
        categoryArrayAdapter
                = new ArrayAdapter<ExpenseCategory>(view.getContext(),
                        R.layout.adapter_expense_category_large);
        categoryArrayAdapter.addAll(databaseHelper.findAllCategories());
        categoryList.setAdapter(categoryArrayAdapter);
        categoryList.setOnItemLongClickListener(this::onCategoryLongClick);

        addCategoryButton.setOnClickListener(this::onAddCategory);

        vibrateError = ((MenoApplication) getActivity().getApplication())::vibrateError;
        vibrateSuccess = ((MenoApplication) getActivity().getApplication())::vibrateSuccess;
        return view;
    }

    /**
     * Implementation of OnClickListener.
     *
     * Implementation of onClickListener, used in button that's clicked when the user wants
     * to add a category.
     *
     * @param view the view that invoked this method.
     */
    private void onAddCategory(View view) {
        String categoryName = addCategoryField.getText().toString();
        String toastMessage;
        boolean successful = false;

        if (!categoryName.equals("")) {
            try {
                databaseHelper.add(new ExpenseCategory(categoryName));
                reloadAdapter();
                toastMessage =  getString(R.string.toast_category_add_success);
                successful = true;
            } catch (SimilarCategoryExistsException e) {
                toastMessage = getString(R.string.toast_category_add_failure_duplicate);
            }
        } else {
            toastMessage = getString(R.string.toast_category_add_failure_empty);
        }

        if (successful) {
            vibrateSuccess.run();
        } else {
            vibrateError.run();
        }

        Toast.makeText(view.getContext(),
                toastMessage,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Implementation of OnItemLongClickListener.
     *
     * Implementation of OnItemLongClickListener for long clicking items in ListView of categories.
     * Opens a dialog for options to edit or delete the category.
     *
     * @param parent See Android documentation for AdapterView.OnItemLongClickListener.
     * @param view See Android documentation for AdapterView.OnItemLongClickListener.
     * @param position See Android documentation for AdapterView.OnItemLongClickListener.
     * @param id See Android documentation for AdapterView.OnItemLongClickListener.
     * @return See Android documentation for AdapterView.OnItemLongClickListener.
     */
    private boolean onCategoryLongClick(AdapterView<?> parent, View view, int position, long id) {
        EditCategoryDialog dialog = new EditCategoryDialog();
        Bundle argBundle = new Bundle();
        argBundle.putString("CategoryName", categoryArrayAdapter.getItem(position).getName());
        dialog.setArguments(argBundle);
        dialog.setTargetFragment(this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "foo_bar");

        return true;
    }

    /**
     * Reloads the data of the ListView containing ExpenseCategories.
     *
     * Reloads the data in the ArrayAdapter connected to the ListView which displays
     * ExpenseCategories, by clearing the adapter and fetching it again from the database.
     */
    private void reloadAdapter() {
        categoryArrayAdapter.clear();
        categoryArrayAdapter.addAll(databaseHelper.findAllCategories());
    }

    /**
     * Deletes an ExpenseCategory from the database if it's not used in any Expense.
     *
     * @param category the ExpenseCategory to delete.
     */
    private void deleteCategory(ExpenseCategory category) {
        String toastMessage;
        int toastLength;

        try {
            databaseHelper.deleteCategory(category);
            reloadAdapter();
            toastMessage = getString(R.string.toast_category_delete_success);
            toastLength = Toast.LENGTH_SHORT;
            vibrateSuccess.run();
        } catch (CategoryReferencedException e) {
            toastMessage = getString(R.string.toast_category_delete_failure_referenced);
            toastLength = Toast.LENGTH_LONG;
            vibrateError.run();
        }

        Toast.makeText(getContext(), toastMessage, toastLength).show();
    }

    /**
     * Renames an ExpenseCategory in the database.
     *
     * @param category the ExpenseCategory to rename.
     * @param newName the new name of the ExpenseCategory.
     */
    private void renameCategory(ExpenseCategory category, String newName) {
        String toastMessage;
        int toastLength;

        try {
            databaseHelper.renameCategory(category, newName);
            reloadAdapter();
            toastMessage = getString(R.string.toast_category_rename_success);
            toastLength = Toast.LENGTH_SHORT;
            vibrateSuccess.run();
        } catch (SimilarCategoryExistsException e) {
            toastMessage = getString(R.string.toast_category_rename_failure_duplicate);
            toastLength = Toast.LENGTH_LONG;
            vibrateError.run();
        }

        Toast.makeText(getContext(), toastMessage, toastLength).show();
    }

    /**
     * A dialog which allows the user to rename or delete an ExpenseCategory.
     */
    public static class EditCategoryDialog extends AppCompatDialogFragment {
        private static String categoryName;
        private static StatefulButton executeEditButton;
        private static EditText renameCategoryField;

        /**
         * Sets the layout for the dialog.
         *
         * @param inflater see Android documentation for Fragment.onCreateView.
         * @param container see Android documentation for Fragment.onCreateView.
         * @param savedInstanceState see Android documentation for Fragment.onCreateView.
         * @return see Android documentation for Fragment.onCreateView.
         */
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_edit_category, container);
        }

        /**
         * Sets the content and listeners for the Dialog.
         *
         * @param view see Android documentation for Fragment.onViewCreated.
         * @param savedInstanceState view see Android documentation for Fragment.onViewCreated.
         */
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getDialog().setTitle(getString(R.string.title_rename_delete_category));

            renameCategoryField = (EditText) view.findViewById(R.id.renameCategoryText);
            executeEditButton = (StatefulButton) view.findViewById(R.id.dialog_edit_category_execute);
            Button cancelButton = (Button) view.findViewById(R.id.dialog_edit_category_cancel);


            categoryName = getArguments().getString("CategoryName");
            renameCategoryField.setText(categoryName);
            renameCategoryField.addTextChangedListener(new DialogTextWatcher());

            cancelButton.setOnClickListener((v) -> dismiss());
            executeEditButton.setOnClickListener(this::onExecuteEdit);
        }

        /**
         * Implementation of OnClickListener for a button when the user executes an edit for an ExpenseCategory.
         *
         * Implementation of OnClickListener for a button when the user executes an edit for an ExpenseCategory.
         * Calls back to the parent class (CategoriesFragment) of this inner class, and updates the
         * or deletes the ExpenseCategory. The ExpenseCategory is attempted to be deleted if the
         * user does not edit the name, and is edited if the user does.
         *
         * @param view mandatory parameter of OnClickListener, not used.
         */
        private void onExecuteEdit(View view) {
            CategoriesFragment target = ((CategoriesFragment) getTargetFragment());

            if (executeEditButton.getState().equals(ButtonState.DELETE)) {
                target.deleteCategory(new ExpenseCategory(categoryName));
                dismiss();
            } else if(executeEditButton.getState().equals(ButtonState.RENAME))  {
                target.renameCategory(
                        new ExpenseCategory(categoryName),
                        renameCategoryField.getText().toString());
                dismiss();
            }

        }

        /**
         * Watches for changes in the dialog text field for editing the ExpenseCategory name.
         */
        private static class DialogTextWatcher implements TextWatcher {

            /**
             * Mandatory method to implement in TextWatcher, does nothing.
             * @param s see Android documentation for TextWatcher.beforeTextChanged.
             * @param start see Android documentation for TextWatcher.beforeTextChanged.
             * @param count see Android documentation for TextWatcher.beforeTextChanged.
             * @param after see Android documentation for TextWatcher.beforeTextChanged.
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            /**
             * Called upon change in the dialog text field for editing the ExpenseCategory name.
             * @param s see Android documentation for TextWatcher.onTextChanged.
             * @param start see Android documentation for TextWatcher.onTextChanged.
             * @param before see Android documentation for TextWatcher.onTextChanged.
             * @param count see Android documentation for TextWatcher.onTextChanged.
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();

                if (text.equals(categoryName) || text.equals("")) {
                    executeEditButton.setState(ButtonState.DELETE);
                } else {
                    executeEditButton.setState(ButtonState.RENAME);
                }
            }

            /**
             * Mandatory method to implement in TextWatcher, does nothing.
             * @param s see Android documentation for TextWatcher.afterTextChanged
             */
            @Override
            public void afterTextChanged(Editable s) {}
        }
    }
}