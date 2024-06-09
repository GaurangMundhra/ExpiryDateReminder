package com.anish.expirydatereminder;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "itemsDatabase";
    private static final int DB_VERSION = 8; // Updated version to 8
    public static final String TABLE_NAME = "itemsTable";
    public static final String ID_COL = "id";
    public static final String NAME_COL = "itemName";
    public static final String MONTH_COL = "month";
    public static final String YEAR_COL = "year";
    public static final String CATEGORY_COL = "category";
    public static final String DATE_COL = "date";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + MONTH_COL + " INTEGER,"
                + YEAR_COL + " INTEGER,"
                + DATE_COL + " INTEGER,"
                + CATEGORY_COL + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 8) {
            boolean categoryColumnExists = false;
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String columnName = cursor.getString(cursor.getColumnIndex("name"));
                    if (CATEGORY_COL.equals(columnName)) {
                        categoryColumnExists = true;
                        break;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();

            if (!categoryColumnExists) {
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + CATEGORY_COL + " TEXT DEFAULT ''");
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add custom downgrade handling if needed
        // For example, you might migrate data to preserve it during downgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public void addNewItem(ItemModel object) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, object.getItem());
        values.put(MONTH_COL, object.getMonth());
        values.put(YEAR_COL, object.getYear());
        values.put(DATE_COL, object.getDate());
        values.put(CATEGORY_COL, object.getCategory());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<ItemModel> getAllItems() {
        List<ItemModel> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(NAME_COL));
                int monthIndex = cursor.getColumnIndex(MONTH_COL);
                int yearIndex = cursor.getColumnIndex(YEAR_COL);
                int dateIndex = cursor.getColumnIndex(DATE_COL);
                int categoryIndex = cursor.getColumnIndex(CATEGORY_COL);

                // Check if column index is valid
                if (monthIndex != -1 && yearIndex != -1 && dateIndex != -1 && categoryIndex != -1) {
                    int month = cursor.getInt(monthIndex);
                    int year = cursor.getInt(yearIndex);
                    int date = cursor.getInt(dateIndex);
                    String category = cursor.getString(categoryIndex);

                    ItemModel item = new ItemModel(name, month, year, date, category);
                    items.add(item);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return items;
    }

    public void deleteRow(ItemModel obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, NAME_COL + "=?", new String[]{obj.getItem()});
        db.close();
    }
}
