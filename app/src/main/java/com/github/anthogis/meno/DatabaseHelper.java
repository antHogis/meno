package com.github.anthogis.meno;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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

    public void add(ExpenseCategory category) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.COL_NAME, category.getName());

        getWritableDatabase().insert(CategoryTable.TABLE_NAME,null, values);
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
                .append("FOREIGN KEY(").append(ExpenseTable.COL_CATEGORY)
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
                .append(CategoryTable.COL_NAME).append(" TEXT")
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
