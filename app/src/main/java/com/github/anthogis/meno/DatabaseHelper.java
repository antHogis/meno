package com.github.anthogis.meno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MenoApp.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void add(ExpenseCategory category) throws SQLException {
        if (!categoryExistsIgnoreCase(category)) {
            ContentValues values = new ContentValues();
            values.put(CategoryTable.COL_NAME.name, category.getName());
            getWritableDatabase().insertOrThrow(CategoryTable.TABLE_NAME,null, values);
        } else {
            throw new SQLException();
        }
    }

    public void add(Expense expense) throws SQLException {
        int categoryId = findCategoryIdByName(expense.getCategory().getName());

        ContentValues values = new ContentValues();
        values.put(ExpenseTable.COL_CATEGORY.name, categoryId);
        values.put(ExpenseTable.COL_COST.name, expense.getCost().toString());
        values.put(ExpenseTable.COL_DATE.name, DateHelper.stringOf(expense.getDate()));

        getWritableDatabase().insertOrThrow(ExpenseTable.TABLE_NAME,null, values);
    }

    public void deleteAllTableValues() {
        getWritableDatabase().delete(ExpenseTable.TABLE_NAME, null, null);
        getWritableDatabase().delete(CategoryTable.TABLE_NAME, null, null);
    }

    public List<Expense> findAllExpenses() {
        String sortOrder = ExpenseTable.COL_DATE.name + " DESC";

        Cursor cursor = getReadableDatabase().query(
                ExpenseTable.TABLE_NAME,
                null,
                null,
                null,
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

        try {
            while (cursor.moveToNext()) {
                int categoryIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_CATEGORY.name);
                int costIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_COST.name);
                int dateIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_DATE.name);

                String categoryString = findCategoryNameById(cursor.getInt(categoryIndex));
                String costString = cursor.getString(costIndex);
                String dateString = cursor.getString(dateIndex);

                ExpenseCategory category = new ExpenseCategory(categoryString);
                BigDecimal cost = new BigDecimal(costString);
                Date date = DateHelper.parse(dateString);

                expenses.add(new Expense(category, cost, date));
            }
        } catch (SQLException | ParseException e) {
            Toast.makeText(context,
                    context.getResources().getString(R.string.toast_expense_get_failure),
                    Toast.LENGTH_SHORT).show();
        } finally {
            cursor.close();
        }

        return expenses;
    }

    public List<ExpenseCategory> findAllCategories() {
        String sortOrder = CategoryTable.COL_NAME.name + " DESC";

        Cursor cursor = getReadableDatabase().query(
                CategoryTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<ExpenseCategory> categories = new ArrayList<>(cursor.getCount());

        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndex(CategoryTable.COL_NAME.name);
            categories.add(new ExpenseCategory(cursor.getString(index)));
        }
        cursor.close();

        return categories;
    }

    public int findCategoryIdByName(String name) throws SQLException {
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

        return id;
    }

    public String findCategoryNameById(int id) throws SQLException {
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

        return name;
    }

    public boolean categoryExistsIgnoreCase(ExpenseCategory category) {
        boolean exists = false;

        for (ExpenseCategory _category : findAllCategories()) {
            if (category.getName().equalsIgnoreCase(_category.getName())) {
                exists = true;
            }
        }

        return exists;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createExpenseCategoryTableSql());
        db.execSQL(createExpenseTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropExpenseTableSql());
        db.execSQL(dropExpenseCategoryTableSql());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

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

    private static String dropExpenseTableSql() {
        return "DROP TABLE IF EXISTS " + ExpenseTable.TABLE_NAME;
    }

    private static String createExpenseCategoryTableSql() {
        CategoryTable _ID = CategoryTable._ID;
        CategoryTable COL_NAME = CategoryTable.COL_NAME;

        return new StringBuilder()
                .append("CREATE TABLE ")
                    .append(CategoryTable.TABLE_NAME).append(" (")
                    .append(_ID.name).append(' ').append(_ID.dataType).append(',')
                    .append(COL_NAME.name).append(' ').append(COL_NAME.dataType)
                .append(')')
            .toString();
    }

    private static String dropExpenseCategoryTableSql() {
        return "DROP TABLE IF EXISTS " + CategoryTable.TABLE_NAME;
    }


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

    private enum CategoryTable {
        _ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT"),
        COL_NAME("name", "TEXT UNIQUE"),
        COL_DELETED("indexable", "INTEGER");

        final static String TABLE_NAME = "categories";
        String name;
        String dataType;

        CategoryTable(String name, String dataType) {
            this.name = name;
            this.dataType = dataType;
        }
    }
}
