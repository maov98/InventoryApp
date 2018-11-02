package com.example.miguelortiz.inventoryapp;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguelortiz.inventoryapp.data.InventoryContract;

public class DetailFragment extends Fragment {

    private Fragment currentFragment;
    private Context context;
    private String itemPrice;
    private String itemDescription;
    private String itemQuantity;
    private String itemIsbn;
    private String itemSupplier;
    private String itemSupplierPhone;
    private String noData;
    private long _id;
    private Bundle bundle;
    private View view;
    private TextView priceView;
    private TextView quantityView;
    private TextView descriptionView;
    private TextView isbnView;
    private TextView supplierView;
    private TextView phoneView;
    private String noItemsToAdd;
    private String noItemsToDec;
    EditText noItemsToAddView;
    EditText noItemsToReduceView;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detailed_fragment,container,false);
        bundle = getArguments();
        priceView = view.findViewById(R.id.detailItemPrice);
        quantityView = view.findViewById(R.id.detailItemQuantity);
        descriptionView = view.findViewById(R.id.detailItemDescription);
        isbnView = view.findViewById(R.id.detailItemISBN);
        supplierView = view.findViewById(R.id.detailItemSupplier);
        phoneView = view.findViewById(R.id.detailItemSupplierPhone);
        noItemsToAddView = view.findViewById(R.id.itemsToAddField);
        noItemsToReduceView =view.findViewById(R.id.itemsToDeleteField);
        Button deleteRecord = view.findViewById(R.id.detailDeleteRecordButton);
        Button addItemsButton = view.findViewById(R.id.detailAddInventoryButton);
        Button reduceItemsButton = view.findViewById(R.id.detailReduceInventoryButton);
        Button callSupplierButton = view.findViewById(R.id.callSupplierButton);
        context = getContext();
        noData = getString(R.string.noAvailableData);
        if(bundle!= null){

            _id = Long.parseLong(bundle.getString(getString(R.string._id)));
            itemPrice = bundle.getString(getString(R.string.price));
            itemDescription = bundle.getString(getString(R.string.description));
            itemQuantity = bundle.getString(getString(R.string.quantity));
            itemIsbn = bundle.getString(getString(R.string.isbn));
            itemSupplier = bundle.getString(getString(R.string.supplier));
            itemSupplierPhone = bundle.getString(getString(R.string.supplierPhone));
        }

        if(!itemPrice.equals("")){
            priceView.setText(itemPrice);
        }
        if(!itemDescription.equals("")){
            descriptionView.setText(itemDescription);
        }
        if(!itemQuantity.equals("")){
            quantityView.setText(itemQuantity);
        }
        if(!itemIsbn.equals("")){
            isbnView.setText(itemIsbn);
        }
        if(!itemSupplier.equals("")){
            supplierView.setText(itemSupplier);
        }else{
            supplierView.setText(noData);
        }
        if(!itemSupplierPhone.equals("")){
            phoneView.setText(itemSupplierPhone);

        }else{
            phoneView.setText(noData);
        }

        callSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(phoneView.getText().toString().equals(noData)) {
                    Toast.makeText(context, " No Phone Number stored in database", Toast.LENGTH_LONG).show();

                } else{
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel: %s", bundle.getString(getString(R.string.supplierPhone)))));
                    startActivity(callIntent);
                }

            }
        });

        deleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,_id);
                int flag = 2;


                int rowdeleted = getContext().getContentResolver().delete(currentItemUri,null,null);
                if (rowdeleted!=0){

                    flag = 1;
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.updatedInvRecord(flag, getString(R.string.successUpdate));
                    Toast.makeText(context,getString(R.string.successfulDeletion),Toast.LENGTH_LONG).show();

                }
            }
        });

        addItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noItemsToAdd = (noItemsToAddView.getText().toString().trim());
                int flag = 2;
                ContentValues contentValues = new ContentValues();
                String newQuantity = Integer.toString(Integer.parseInt(noItemsToAdd)+Integer.parseInt(itemQuantity));
                contentValues.put(InventoryContract.InventoryEntry.PRICE,itemPrice);
                contentValues.put(InventoryContract.InventoryEntry.PRODUCT_NAME,itemDescription);
                contentValues.put(InventoryContract.InventoryEntry.ISBN10,itemIsbn);
                contentValues.put(InventoryContract.InventoryEntry.QUANTITY,newQuantity);
                contentValues.put(InventoryContract.InventoryEntry.SUPPLIER_NAME,itemSupplier);
                contentValues.put(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER,itemSupplierPhone);

                if(!itemPrice.equals("") && !itemQuantity.equals("") && !itemDescription.equals("") && !itemIsbn.equals("")){

                    Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,_id);
                    int rowupdated = getContext().getContentResolver().update(currentItemUri,contentValues,null,null);
                    if (rowupdated!=0){
                        flag = 1;
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updatedInvRecord(flag, getString(R.string.successUpdate));
                        Toast.makeText(context,getString(R.string.successUpdate),Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(context,getString(R.string.wrongData),Toast.LENGTH_LONG).show();
                }

            }

        });

        reduceItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noItemsToDec = (noItemsToReduceView.getText().toString().trim());
                int flag = 2;
                int reduction = Integer.parseInt(noItemsToDec);
                int currentQuantity = Integer.parseInt(itemQuantity);
                if (reduction<=currentQuantity){
                    String newQuantity = Integer.toString(Integer.parseInt(itemQuantity)-reduction);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(InventoryContract.InventoryEntry.PRICE,itemPrice);
                    contentValues.put(InventoryContract.InventoryEntry.PRODUCT_NAME,itemDescription);
                    contentValues.put(InventoryContract.InventoryEntry.ISBN10,itemIsbn);
                    contentValues.put(InventoryContract.InventoryEntry.QUANTITY,newQuantity);
                    contentValues.put(InventoryContract.InventoryEntry.SUPPLIER_NAME,itemSupplier);
                    contentValues.put(InventoryContract.InventoryEntry.SUPPLIER_PHONE_NUMBER,itemSupplierPhone);

                    if(!itemPrice.equals("") && !itemQuantity.equals("") && !itemDescription.equals("") && !itemIsbn.equals("")){

                        Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,_id);
                        int rowupdated = getContext().getContentResolver().update(currentItemUri,contentValues,null,null);
                        if (rowupdated!=0){
                            flag = 1;
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.updatedInvRecord(flag, getString(R.string.successUpdate));
                            Toast.makeText(context,getString(R.string.successUpdate),Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(context,getString(R.string.wrongData),Toast.LENGTH_LONG).show();
                    }
                }else {

                    Toast.makeText(context,getString(R.string.incorrectReductionQuantity),Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }


}
