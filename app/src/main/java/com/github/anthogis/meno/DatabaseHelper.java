package com.github.anthogis.meno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MenoApp.db";
    private static final int DATABSE_VESION = 1;

    private static final String CREATE_EXPENSE_TABLE_SQL  = createExpenseTableSql();
    private static final String DROP_EXPENSE_TABLE_SQL    = dropExpenseTableSql();
    private static final String CREATE_CATEGORY_TABLE_SQL = createExpenseCategoryTableSql();
    private static final String DROP_CATEGORY_TABLE_SQL   = dropExpenseCategoryTableSql();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void add(ExpenseCategory category) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.COL_NAME, category.getName());
        getWritableDatabase().insertOrThrow(CategoryTable.TABLE_NAME,null, values);
    }

    public List<ExpenseCategory> findAllCategories() {
        String[] projection = {CategoryTable._ID, CategoryTable.TABLE_NAME};
        String sortOrder = CategoryTable.COL_NAME + " ASC";

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
