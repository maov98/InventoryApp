package com.example.miguelortiz.inventoryapp;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import com.example.miguelortiz.inventoryapp.data.InventoryContract;

public class InventoryCursorAdaptor extends CursorAdapter {

    private LayoutInflater cursorInflater;


    public InventoryCursorAdaptor(Context context, Cursor c) {



        super(context, c,0);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.inventory_item_list_layout,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView priceView = view.findViewById(R.id.itemPrice);
        TextView descriptionView = view.findViewById(R.id.itemDescription);
        TextView quantityView = view.findViewById(R.id.itemQuantity);
        TextView isbnView = view.findViewById(R.id.itemISBN);
        TextView supplierView = view.findViewById(R.id.itemSupplier);
        TextView supplierPhoneView = view.findViewById(R.id.itemSupplierPhone);
        int priceColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.PRICE);
        int descriptionColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.QUANTITY);
        int isbnColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.ISBN10);
        int supplierColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.SUPPLIER_NAME);
        int supplierPhoneColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER);
        String price = cursor.getString(priceColumnIndex);
        String description = cursor.getString(descriptionColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);
        String supplier = cursor.getString(supplierColumnIndex);
        String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
        String isbn = cursor.getString(isbnColumnIndex);
        priceView.setText(price);
        descriptionView.setText(description);
        quantityView.setText(quantity);
        isbnView.setText(isbn);
        supplierView.setText(supplier);
        supplierPhoneView.setText(supplierPhone);
    }

}
