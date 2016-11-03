package me.henrylai.inventory.data;

/**
 * Created by Starwater on 11/3/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "Inventory";
    // Inventory table name
    private static final String TABLE_ITEMS = "items";
    // Items Table Columns names
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String CONDITION = "condition";
    private static final String DESCRIPTION = "description";

    public SQLiteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT NOT NULL UNIQUE,"
                + VALUE + " REAL,"
                + CONDITION + " TEXT,"
                + DESCRIPTION + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new item
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, item.getmName()); // Item Name
        values.put(VALUE, item.getmValue()); // Item Value
        values.put(CONDITION, item.getmCondition()); // Item Value
        values.put(DESCRIPTION, item.getmDescription()); // Item Value

        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one item
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS, new String[]{KEY_ID,
                NAME, VALUE, CONDITION, DESCRIPTION}, KEY_ID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Double.parseDouble(cursor.getString(2)), cursor.getString(3), cursor.getString(4));
        // return item
        return item;
    }

    // Getting All Items from DB
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setmId(Integer.parseInt(cursor.getString(0)));
                item.setmName(cursor.getString(1));
                item.setmValue(Double.parseDouble(cursor.getString(2)));
                item.setmCondition(cursor.getString(3));
                item.setmDescription(cursor.getString(4));
                // Adding item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        // return item list
        return itemList;
    }

    // Getting item Count
    public int getItemCount() {
        String countQuery = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    // Deleting an item
    public void deleteItem(Item deletedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(deletedItem.getmId()) });
        db.close();
    }

    // Updating an item
    public int updateItem(Item updatedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, updatedItem.getmName());
        values.put(VALUE, updatedItem.getmValue());
        values.put(CONDITION, updatedItem.getmCondition());
        values.put(DESCRIPTION, updatedItem.getmDescription());
        // updating row
        return db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(updatedItem.getmId())});
    }
}