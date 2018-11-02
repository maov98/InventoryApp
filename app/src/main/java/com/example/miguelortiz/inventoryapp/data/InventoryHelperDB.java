package com.example.miguelortiz.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.miguelortiz.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryHelperDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;


    public InventoryHelperDB(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_INVENTORY_TABLE =  "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.PRODUCT_NAME + " TEXT NOT NULL,"
                + InventoryEntry.PRICE + " INTEGER NOT NULL, "
                + InventoryEntry.QUANTITY + " INTEGER NOT NULL, "
                + InventoryEntry.SUPPLIER_NAME + " TEXT,"
                + InventoryEntry.SUPPLIER_PHONE_NUMBER + " TEXT,"
                + InventoryEntry.ISBN10 + " TEXT NOT NULL,"
                + InventoryEntry.ISBN13 + " TEXT,"
                + InventoryEntry.BOOK_FORMAT + " INTEGER NOT NULL DEFAULT 1);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
