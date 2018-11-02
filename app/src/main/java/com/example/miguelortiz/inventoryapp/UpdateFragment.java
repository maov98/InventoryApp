package com.example.miguelortiz.inventoryapp;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miguelortiz.inventoryapp.data.InventoryContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment {

    private EditText descriptionValueView;
    private EditText quantityValueView;
    private EditText priceValueView;
    private EditText supplierNameValueView;
    private EditText supplierPhoneValueView;
    private EditText isbn10ValueView;
    private Bundle bundle;
    private Intent intent;
    private String price;
    private String quantity;
    private String description;
    private String isbn;
    private String supplier;
    private String phone;
    private String noData;
    private long _id = 0;
    Context context;

    public UpdateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        bundle = getArguments();
        quantityValueView = (EditText) view.findViewById(R.id.editQuantity);
        descriptionValueView = (EditText) view.findViewById(R.id.editDescription);
        priceValueView = (EditText) view.findViewById(R.id.editPrice);
        supplierNameValueView = (EditText) view.findViewById(R.id.editSupplierName);
        supplierPhoneValueView = (EditText) view.findViewById(R.id.editSupplierPhone);
        isbn10ValueView = (EditText) view.findViewById(R.id.editIsbn10);
        Button updateInventoryButton = (Button) view.findViewById(R.id.updateInventoryRecordButton);
        noData = getString(R.string.noAvailableData);

        if (bundle !=null) {

            price = bundle.getString(getString(R.string.price));
            quantity = bundle.getString(getString(R.string.quantity));
            description = bundle.getString(getString(R.string.description));
            isbn = bundle.getString(getString(R.string.isbn));
            supplier = bundle.getString(getString(R.string.supplier));
            phone = bundle.getString(getString(R.string.supplierPhone));
            _id = Long.parseLong(bundle.getString(getString(R.string._id)));
        }

        descriptionValueView.setText(description);
        priceValueView.setText(price);
        quantityValueView.setText(quantity);
        isbn10ValueView.setText(isbn);
        if(supplier!=null){
            supplierNameValueView.setText(supplier);
        }else{
            supplierNameValueView.setText(noData);
        }
        if(phone!=null){
            supplierPhoneValueView.setText(phone);
        }else{
            supplierPhoneValueView.setText(noData);
        }

        updateInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int flag = 0;
                price = priceValueView.getText().toString();
                quantity = quantityValueView.getText().toString();
                description = descriptionValueView.getText().toString();
                isbn = isbn10ValueView.getText().toString();
                supplier = supplierNameValueView.getText().toString();
                phone = supplierPhoneValueView.getText().toString().trim();
                ContentValues contentValues = new ContentValues();
                contentValues.put(InventoryContract.InventoryEntry.PRICE,price);
                contentValues.put(InventoryContract.InventoryEntry.PRODUCT_NAME,description);
                contentValues.put(InventoryContract.InventoryEntry.ISBN10,isbn);
                contentValues.put(InventoryContract.InventoryEntry.QUANTITY,quantity);
                contentValues.put(InventoryContract.InventoryEntry.SUPPLIER_NAME,supplier);
                contentValues.put(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER,phone);

                if(!price.equals("") && !quantity.equals("") && !description.equals("") && !isbn.equals("")){

                    Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,_id);
                    int rowupdated = getContext().getContentResolver().update(currentItemUri,contentValues,null,null);
                    if (rowupdated!=0){
                        flag = 1;
                       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updatedInvRecord(flag, getString(R.string.successUpdate));
                        Toast.makeText(context,getString(R.string.successUpdate),Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(context,getString(R.string.wrongData),Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    class CheckDataQuality implements TextWatcher{


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            try{
                int value = Integer.parseInt(s.toString());
                if (value <0 || s.toString() == null){
                    s.replace(0,s.length(),"0");
                    Toast.makeText(context,getString(R.string.noNegNumber),Toast.LENGTH_SHORT).show();
                }
            }catch (NumberFormatException nfm){}
        }
    }

}
