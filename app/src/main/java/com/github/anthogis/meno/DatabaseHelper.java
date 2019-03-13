package com.github.anthogis.meno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
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
            values.put(CategoryTable.COL_NAME, category.getName());
            getWritableDatabase().insertOrThrow(CategoryTable.TABLE_NAME,null, values);
        } else {
            throw new SQLException();
        }
    }

    public void add(Expense expense) throws SQLException {
        int categoryId = findCategoryIdByName(expense.getCategory().getName());

        ContentValues values = new ContentValues();
        values.put(ExpenseTable.COL_CATEGORY, categoryId);
        values.put(ExpenseTable.COL_COST, expense.getCost().toString());
        values.put(ExpenseTable.COL_DATE, DateHelper.stringOf(expense.getDate()));

        getWritableDatabase().insertOrThrow(ExpenseTable.TABLE_NAME,null, values);
    }

    public void deleteAllTableValues() {
        getWritableDatabase().delete(ExpenseTable.TABLE_NAME, null, null);
        getWritableDatabase().delete(CategoryTable.TABLE_NAME, null, null);
    }

    public List<Expense> findAllExpenses() {
        String sortOrder = ExpenseTable.COL_DATE + " DESC";

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
                int categoryIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_CATEGORY);
                int costIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_COST);
                int dateIndex = cursor.getColumnIndexOrThrow(ExpenseTable.COL_DATE);

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
        String sortOrder = CategoryTable.COL_NAME + " DESC";

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
            int index = cursor.getColumnIndex(CategoryTable.COL_NAME);
            categories.add(new ExpenseCategory(cursor.getString(index)));
        }
        cursor.close();

        return categories;
    }

    public int findCategoryIdByName(String name) throws SQLException {
        int id = -1;
        String[] columns = {CategoryTable._ID, CategoryTable.COL_NAME};
        String selection = CategoryTable.COL_NAME + "= ?";
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
            id = cursor.getInt(cursor.getColumnIndexOrThrow(CategoryTable._ID));
        }
        cursor.close();

        return id;
    }

    public String findCategoryNameById(int id) throws SQLException {
        String name = null;

        String[] columns = {CategoryTable._ID, CategoryTable.COL_NAME};
        String selection = CategoryTable._ID + "= ?";
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
            name = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.COL_NAME));
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
        return new StringBuilder()
                .append("CREATE TABLE ")
                .append(ExpenseTable.TABLE_NAME).append(" (")
                .append(ExpenseTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(ExpenseTable.COL_CATEGORY).append(" INTEGER,")
                .append(ExpenseTable.COL_COST).append(" TEXT,")
                .append(ExpenseTable.COL_DATE).append(" TEXT,")
                .append("FOREIGN KEY(").append(ExpenseTable.COL_CATEGORY).append(')')
                .append(" REFERENCES ").append(CategoryTable.TABLE_NAME)
                .append('(').append(CategoryTable._ID).append(')')
                .append(')')
                .toString();
    }

    private static String dropExpenseTableSql() {
        return "DROP TABLE IF EXISTS " + ExpenseTable.TABLE_NAME;
    }

    private static String createExpenseCategoryTableSql() {
        return new StringBuilder()
                .append("CREATE TABLE ")
                .append(CategoryTable.TABLE_NAME).append(" (")
                .append(CategoryTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(CategoryTable.COL_NAME).append(" TEXT UNIQUE")
                .append(')')
                .toString();
    }

    private static String dropExpenseCategoryTableSql() {
        return "DROP TABLE IF EXISTS " + CategoryTable.TABLE_NAME;
    }

    private static final class ExpenseTable implements BaseColumns {
        public static final String TABLE_NAME = "expenses";
        public static final String COL_CATEGORY = "category";
        public static final String COL_COST = "cost";
        public static final String COL_DATE = "date";
    }

    private static final class CategoryTable implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COL_NAME = "name";
    }
}
