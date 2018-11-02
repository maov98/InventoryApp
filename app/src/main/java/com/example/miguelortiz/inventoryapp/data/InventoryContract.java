package com.example.miguelortiz.inventoryapp.data;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {

    private InventoryContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.miguelortiz.inventoryapp";
    public static final String PATH_INVENTORY = "Inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +
            "/" + PATH_INVENTORY;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +
            "/" + PATH_INVENTORY;

    public static final class InventoryEntry implements BaseColumns {


        public static final String TABLE_NAME = "Inventory";
        public static final String _ID = BaseColumns._ID;
        public static final String PRODUCT_NAME = "Product_Name";
        public static final String PRICE = "Price";
        public static final String QUANTITY = "Quantity_in_Stock";
        public static final String SUPPLIER_NAME = "Supplier_Name";
        public static final String SUPPLIER_PHONE_NUMBER = "Supplier_Phone_Number";
        public static final String BOOK_FORMAT = "Book_Format";
        public static final String ISBN10 = "ISBN_10";
        public static final String ISBN13 = "ISBN_13";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);


    }


}