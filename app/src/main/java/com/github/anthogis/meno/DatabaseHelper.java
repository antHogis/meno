package com.github.anthogis.meno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.github.anthogis.meno.exceptions.CategoryReferencedException;
import com.github.anthogis.meno.exceptions.SimilarCategoryExistsException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DatabaseHelper is a helper class to access an SQLite database.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * The name of the database used.
     */
    private static final String DATABASE_NAME = "MenoApp.db";

    /**
     * The current version of the database used.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The context where this class is instantiated.
     */
    private Context context;


    /**
     * TODO javadoc
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Adds a category to a table in the database, if it does not already exist.
     *
     * Adds a category to a table by the name specified in inner enum
     * CategoryTable.TABLE_NAME, which exists in the database. Only adds
     * the name of the category to the table, if the table doesn't already
     * include a category of the same name, regardless of the case of the name.
     *
     * @param category the category to add to the table in the database.
     * @throws SQLException if a category by the same name already exists in the table.
     */
    public void add(ExpenseCategory category) throws SimilarCategoryExistsException {
        if (!categoryExistsIgnoreCase(category)) {
            ContentValues values = new ContentValues();
            values.put(CategoryTable.COL_NAME.name, category.getName());
            getWritableDatabase().insertOrThrow(CategoryTable.TABLE_NAME,null, values);
        } else {
            throw new SimilarCategoryExistsException();
        }
    }

    /**
     * Adds an expense to a table in the database.
     *
     * Adds an expense to table by the name specified in inner enum
     * ExpenseTable.TABLE_NAME, which exists in the database. Attributes
     * a foreign key to the id of the category to the expense.
     *
     * @param expense the expense to add to the table in the database.
     * @throws SQLException if the expense could not be added, or if the name
     * of the category in the expense could not be found from the table of categories.
     */
    public void add(Expense expense) throws SQLException {
        int categoryId = findCategoryIdByName(expense.getCategory().getName());

        ContentValues values = new ContentValues();
        values.put(ExpenseTable.COL_CATEGORY.name, categoryId);
        values.put(ExpenseTable.COL_COST.name, expense.getCost().toString());
        values.put(ExpenseTable.COL_DATE.name, DateHelper.stringOf(expense.getDate()));

        getWritableDatabase().insertOrThrow(ExpenseTable.TABLE_NAME,null, values);
    }

    /**
     * Convenience method to delete all values in all tables of the database.
     */
    public void deleteAllTableValues() {
        getWritableDatabase().delete(ExpenseTable.TABLE_NAME, null, null);
        getWritableDatabase().delete(CategoryTable.TABLE_NAME, null, null);
    }

    /**
     * Deletes the given expense by it's id
     * @param expense the expense to delete.
     */
    public void deleteExpense(Expense expense) {
        String whereClause = ExpenseTable._ID.name + "= ?";
        String[] whereArgs = {expense.getId().toString()};
        getWritableDatabase().delete(ExpenseTable.TABLE_NAME, whereClause, whereArgs);
    }

    /**
     * Deletes a category.
     *
     * Deletes a category if no expense in the table of expenses
     * has a reference to it's ID. Otherwise an exception is thrown.
     *
     * @param category the category to delete.
     * @throws CategoryReferencedException if the category is referenced in the table of expenses.
     */
    public void deleteCategory(ExpenseCategory category) throws CategoryReferencedException {
        Cursor cursor;

        //Find id of category
        int categoryId = findCategoryIdByName(category.getName());

        //Find all expenses with reference to category id
        String[] columns = {ExpenseTable.COL_CATEGORY.name};
        String selection = ExpenseTable.COL_CATEGORY.name + "= ?";
        String[] selectionArgs = {"" + categoryId};
        cursor = getReadableDatabase().query(
                ExpenseTable.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean categoryReferenced = cursor.getCount() > 0;
        cursor.close();

        if (categoryReferenced) {
            throw new CategoryReferencedException();
        } else {
            String whereClause = CategoryTable._ID.name + "= ?";
            String[] whereArgs = {"" + categoryId};
            getWritableDatabase().delete(
                    CategoryTable.TABLE_NAME,
                    whereClause,
                    whereArgs);
        }
    }

    /**
     * TODO javadoc
     * @param category
     * @param newName
     * @throws SimilarCategoryExistsException
     */
    public void renameCategory(ExpenseCategory category, String newName)
            throws SimilarCategoryExistsException {
        if (!categoryExistsIgnoreCase(new ExpenseCategory(newName))) {
            int categoryIndex = findCategoryIdByName(category.getName());

            String whereClause = CategoryTable._ID.name + "= ?";
            String[] whereArgs = {"" + categoryIndex};
            ContentValues values = new ContentValues();
            values.put(CategoryTable.COL_NAME.name, newName);

            getWritableDatabase().update(
                    CategoryTable.TABLE_NAME,
                    values,
                    whereClause,
                    whereArgs);
        } else {
            throw new SimilarCategoryExistsException();
        }
    }

    /**
     * Retrieves all expenses from the table of expenses in the database.
     *
     * Retrieves all expenses from the table of expenses in the database,
     * arranged descending by date. Displays a Toast in the context of
     * the instantiation of this class if an expense could not be retrieved
     * from the table.
     *
     * @return the expenses from the database.
     */
    public List<Expense> findAllExpenses() {
        String sortOrder = ExpenseTable.COL_DATE.name + " DESC";
        return findAllExpensesHelper(sortOrder, null, null);
    }

    /**
     * Retrieves all expenses from the table of expenses in the database where the date string starts with given argument.
     *
     * @param dateStartString the string that the date should start with.
     * @return the filtered expenses from the database.
     */
    public List<Expense> findAllExpensesWhereDateStartsWith(String dateStartString) {
        String sortOrder = ExpenseTable.COL_DATE.name + " DESC";
        String selection = ExpenseTable.COL_DATE.name + " LIKE ?";
        String[] selectionArgs = {dateStartString + '%'};

        return findAllExpensesHelper(sortOrder, selection, selectionArgs);
    }

    /**
     * Retrieves all categories form the table of categories in the database.
     *
     * @return the categories from the database.
     */
    public List<ExpenseCategory> findAllCategories() {
        String sortOrder = CategoryTable.COL_NAME.name + " ASC";
        String[] columns = {CategoryTable.COL_NAME.name};

        Cursor cursor = getReadableDatabase().query(
                CategoryTable.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<ExpenseCategory> categories = new ArrayList<>(cursor.getCount());

        while(cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(CategoryTable.COL_NAME.name);

            ExpenseCategory category = new ExpenseCategory();
            category.setName(cursor.getString(nameIndex));
            categories.add(category);
        }
        cursor.close();

        return categories;
    }

    /**
     * Helper method for retrieving expenses.
     *
     * @param sortOrder the sort order of the expenses.
     * @param selection the where clause for the query
     * @param selectionArgs the arguments for the selection
     * @return the expenses from the query.
     */
    private List<Expense> findAllExpensesHelper(String sortOrder,
                                                String selection,
                                                String[] selectionArgs) {
        Cursor cursor = getReadableDatabase().query(
                ExpenseTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<Expense> expenses;

        if (cursor.getCount() >= 0) {
            expenses = new ArrayList<>(cursor.getCount());
        } else {
            expenses = new ArrayList<>(0);
        }

        while (cursor.moveToNext()) {
            try {
                int idIndex = cursor.getColumnIndexOrThrow(ExpenseTable._ID.name);
                int categoryIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_CATEGORY.name);
                int costIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_COST.name);
                int dateIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_DATE.name);

                int id = cursor.getInt(idIndex);
                String categoryString = findCategoryNameById(cursor.getInt(categoryIndex));
                String costString = cursor.getString(costIndex);
                String dateString = cursor.getString(dateIndex);

                ExpenseCategory category = new ExpenseCategory(categoryString);
                BigDecimal cost = new BigDecimal(costString);
                Date date = DateHelper.parse(dateString);

                Expense expense = new Expense(category, cost, date);
                expense.setId(id);
                expenses.add(expense);
            } catch (SQLException | ParseException e) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.toast_expense_get_failure),
                        Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();

        return expenses;
    }

    /**
     * Retrieves the ID of a category from the table of categories.
     *
     * @param name the name of the category, of which the ID in the table is retrieved.
     * @return -1 if an ID could not be found for the category, else the ID of the category.
     * @throws SQLException if the column ID could not be found in the table, or if a
     * category could not be found by the name provided.
     */
    private int findCategoryIdByName(String name) throws SQLException {
        int id = -1;
        String[] columns = {CategoryTable._ID.name, CategoryTable.COL_NAME.name};
        String selection = CategoryTable.COL_NAME.name + "= ?";
        String[] selectionArgs = {name};

        Cursor cursor = getReadableDatabase().query(
                CategoryTable.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(CategoryTable._ID.name));
        }
        cursor.close();

        if (id == -1) {
            throw new SQLException();
        }

        return id;
    }

    /**
     * Retrieves the name of a category by ID, from the table of categories.
     *
     * @param id the id of the category to retrieve.
     * @return the name of the category.
     * @throws SQLException if the column name could not be found in the table, or if a
     * category could not be found by the id provided.
     */
    private String findCategoryNameById(int id) throws SQLException {
        String name = null;

        String[] columns = {CategoryTable._ID.name, CategoryTable.COL_NAME.name};
        String selection = CategoryTable._ID.name + "= ?";
        String[] selectionArgs = {"" + id};

        Cursor cursor = getReadableDatabase().query(
                CategoryTable.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.COL_NAME.name));
        }
        cursor.close();

        if (name == null) {
            throw new SQLException();
        }

        return name;
    }

    /**
     * Checks if a given category already exists in the table of categories,
     * regardless of the case of the name.
     *
     * @param category the category which is looked for in the table of categories.
     * @return true if a category by the same name exists, regardless of case, false
     * if a category by the same name does not exist
     */
    private boolean categoryExistsIgnoreCase(ExpenseCategory category) {
        boolean exists = false;

        for (ExpenseCategory _category : findAllCategories()) {
            if (category.getName().equalsIgnoreCase(_category.getName())) {
                exists = true;
            }
        }

        return exists;
    }

    /**
     * Lifecycle method of super class SQLiteOpenHelper.
     *
     * Lifecycle method of super class SQLiteOpenHelper, called when
     * the database is created for the first time
     * @param db the database to create.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createExpenseCategoryTableSql());
        db.execSQL(createExpenseTableSql());
    }

    /**
     * Lifecycle method of super class SQLiteOpenHelper.
     *
     * Lifecycle method of super class SQLiteOpenHelper. called when
     * the database needs to be upgraded.
     *
     * @param db the database.
     * @param oldVersion the old database version.
     * @param newVersion the new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropExpenseTableSql());
        db.execSQL(dropExpenseCategoryTableSql());
        onCreate(db);
    }

    /**
     * Lifecycle method of super class SQLiteOpenHelper.
     *
     * Lifecycle method of super class SQLiteOpenHelper. called when
     * the database needs to be downgraded.
     *
     * @param db the database.
     * @param oldVersion the old database version.
     * @param newVersion the new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Creates SQL statement string for creating table of expenses.
     *
     * @return the SQL statement String
     */
    private static String createExpenseTableSql() {
        ExpenseTable _ID = ExpenseTable._ID;
        ExpenseTable CATEGORY = ExpenseTable.COL_CATEGORY;
        ExpenseTable COST = ExpenseTable.COL_COST;
        ExpenseTable DATE = ExpenseTable.COL_DATE;

        return new StringBuilder()
                .append("CREATE TABLE ").append(ExpenseTable.TABLE_NAME).append(" (")
                    .append(_ID.name).append(' ').append(_ID.dataType).append(',')
                    .append(CATEGORY.name).append(' ').append(CATEGORY.dataType).append(',')
                    .append(COST.name).append(' ').append(COST.dataType).append(',')
                    .append(DATE.name).append(' ').append(DATE.dataType).append(',')
                    .append("FOREIGN KEY(").append(CATEGORY.name).append(')')
                    .append(" REFERENCES ").append(CategoryTable.TABLE_NAME)
                        .append('(').append(CategoryTable._ID.name).append(')')
                .append(')')
            .toString();
    }

    /**
     * Creates SQL statement string for dropping table of expenses.
     *
     * @return the SQL statement String
     */
    private static String dropExpenseTableSql() {
        return "DROP TABLE IF EXISTS " + ExpenseTable.TABLE_NAME;
    }

    /**
     * Creates SQL statement string for creating table of categories.
     *
     * @return the SQL statement String
     */
    private static String createExpenseCategoryTableSql() {
        CategoryTable _ID = CategoryTable._ID;
        CategoryTable COL_NAME = CategoryTable.COL_NAME;

        return new StringBuilder()
                .append("CREATE TABLE ").append(CategoryTable.TABLE_NAME).append(" (")
                    .append(_ID.name).append(' ').append(_ID.dataType).append(',')
                    .append(COL_NAME.name).append(' ').append(COL_NAME.dataType)
                .append(')')
            .toString();
    }

    /**
     * Creates SQL statement string for dropping table of categories.
     *
     * @return the SQL statement String
     */
    private static String dropExpenseCategoryTableSql() {
        return "DROP TABLE IF EXISTS " + CategoryTable.TABLE_NAME;
    }

    /**
     * Contains values for defining a table of expenses in the database.
     */
    private enum ExpenseTable {
        _ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT"),
        COL_CATEGORY("category","INTEGER"),
        COL_COST("cost","TEXT"),
        COL_DATE("date","TEXT")
        ;

        final static String TABLE_NAME = "expenses";
        String name;
        String dataType;

        ExpenseTable(String name, String dataType) {
            this.name = name;
            this.dataType = dataType;
        }
    }

    /**
     * Contains values for defining a table of ExpenseCategories in the database.
     */
    private enum CategoryTable {
        _ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT"),
        COL_NAME("name", "TEXT UNIQUE");

        final static String TABLE_NAME = "categories";
        String name;
        String dataType;

        CategoryTable(String name, String dataType) {
            this.name = name;
            this.dataType = dataType;
        }
    }
}
