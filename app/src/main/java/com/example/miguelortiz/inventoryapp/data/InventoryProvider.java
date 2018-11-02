package com.example.miguelortiz.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.miguelortiz.inventoryapp.R;
import com.example.miguelortiz.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryProvider extends ContentProvider {

    private InventoryHelperDB mDbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int INV = 100;
    private static final int INV_ID = 110;
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();



    static {

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.PATH_INVENTORY,INV);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.PATH_INVENTORY+"/#",INV_ID);

    }

    @Override
    public boolean onCreate() {

        mDbHelper = new InventoryHelperDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match) {

            case INV:
                cursor = db.query(InventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case INV_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.uriIllegarArgumentException)
                        + " " + uri);
        }
        return cursor;
    }


    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){

            case INV:
                return InventoryContract.CONTENT_LIST_TYPE;
            case INV_ID:
                return InventoryContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI "+ uri + "with match " + match);
        }

    }

    public Uri insert(Uri uri, ContentValues values) {
        boolean b = dataCheck(values);
        final int match = sUriMatcher.match(uri);
        if (b){

            switch (match) {
                case INV:
                    return insertInventory(uri, values);
                default:
                    throw new IllegalArgumentException("Insertion is not supported for " + uri);

            }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case INV:
                return db.delete(InventoryEntry.TABLE_NAME,selection,selectionArgs);

            case INV_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(InventoryEntry.TABLE_NAME,selection,selectionArgs);

            default:
                throw new IllegalArgumentException("Deletion is not supported for" + uri);

        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        dataCheck(values);
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INV:

                int rowUpdated = updateInventory(uri,values,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(InventoryEntry.CONTENT_URI,null);
                return rowUpdated;


            case INV_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdated = updateInventory(uri,values,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(InventoryEntry.CONTENT_URI,null);

                return rowUpdated;
            default:
                throw new IllegalArgumentException("Update is not supported for" + uri);
        }
    }

    private Uri insertInventory(Uri uri, ContentValues values) {

        long id;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        dataCheck(values);
        id = db.insert(InventoryEntry.TABLE_NAME, null, values);
        if(id == -1){
            Log.e(LOG_TAG, "Failed to inser row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri,id);
    }

    private int updateInventory(Uri uri,ContentValues values, String selection, String[] selectionArgs){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        dataCheck(values);
        if (values.size()==0){
            return 0;
        }
        else {
            int b = db.update(InventoryEntry.TABLE_NAME,values,selection,selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return b;

        }

    }

    private boolean dataCheck(ContentValues values){

        String itemName = "";
        String itemQuantity = "";
        String itemPrice = "";
        String itemIsbn = "";
        boolean i = true;

        if(values.containsKey(InventoryEntry.PRODUCT_NAME)){
            itemName = values.getAsString(InventoryEntry.PRODUCT_NAME);
        }
        if(values.containsKey(InventoryEntry.QUANTITY)){
            itemQuantity = values.getAsString(InventoryEntry.QUANTITY);
        }
        if(values.containsKey(InventoryEntry.PRICE)){
            itemPrice = values.getAsString(InventoryEntry.PRICE);
        }

        if(values.containsKey(InventoryEntry.ISBN10)){
            itemIsbn = values.getAsString(InventoryEntry.ISBN10);
        }

        if(itemName.equals("") || itemQuantity.equals("") || itemPrice.equals("") || itemIsbn.equals("")) {

            i = false;

        }
        return i;


    }
}
