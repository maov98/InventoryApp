package com.example.miguelortiz.inventoryapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguelortiz.inventoryapp.data.InventoryContract;

public class AddActivity extends AppCompatActivity {

    private EditText descriptionValue;
    private EditText quantityValue;
    private EditText priceValue;
    private EditText supplierNameValue;
    private EditText supplierPhoneValue;
    private EditText isbn10Value;
    private ContentValues values;
    private TextView message1View;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainActivity);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        quantityValue = findViewById(R.id.addQuantity);
        descriptionValue =  findViewById(R.id.addDescription);
        priceValue =  findViewById(R.id.addPrice);
        supplierNameValue =  findViewById(R.id.addSupplierName);
        supplierPhoneValue =  findViewById(R.id.addSupplierPhone);
        isbn10Value =  findViewById(R.id.addIsbn10);
        message1View = findViewById(R.id.addActivityMessage1);
        Button addInventory =  findViewById(R.id.addInventoryRecordButton);
        values = new ContentValues();
        setTitle(getString(R.string.addScreenTitle));

        addInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = descriptionValue.getText().toString().trim();
                String price = priceValue.getText().toString().trim();
                String quantity = quantityValue.getText().toString().trim();
                String supplierPhone = supplierPhoneValue.getText().toString().trim();
                String supplierName = supplierNameValue.getText().toString().trim();
                String isbn10 = isbn10Value.getText().toString().trim();
                values.put(InventoryContract.InventoryEntry.PRODUCT_NAME,description);
                values.put(InventoryContract.InventoryEntry.PRICE,price);
                values.put(InventoryContract.InventoryEntry.QUANTITY,quantity);
                values.put(InventoryContract.InventoryEntry.ISBN10,isbn10);
                values.put(InventoryContract.InventoryEntry.SUPPLIER_NAME,supplierName);
                values.put(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER,supplierPhone);

                Uri uri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI,values);

                if(uri == null){
                    Toast.makeText(getBaseContext(),getString(R.string.failedOperation),Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(),getString(R.string.successAddition),Toast.LENGTH_LONG).show();
                    descriptionValue.setText("");
                    priceValue.setText("");
                    quantityValue.setText("");
                    supplierNameValue.setText("");
                    isbn10Value.setText("");
                    supplierPhoneValue.setText("");
                    message1View.setVisibility(View.VISIBLE);
                    Context context = getApplicationContext();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = getCurrentFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                }
            }
        });
    }
}
