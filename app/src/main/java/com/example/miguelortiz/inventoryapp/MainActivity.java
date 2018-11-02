package com.example.miguelortiz.inventoryapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguelortiz.inventoryapp.data.InventoryContract;
import com.example.miguelortiz.inventoryapp.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_INV_LOADER = 2;
    ListView inventoryListView;
    InventoryCursorAdaptor inventoryCursorAdaptor;
    private Uri localUri;
    private Uri currentItemUri;
    String price;
    String quantity;
    String description;
    String isbn;
    String supplier;
    String phone;
    FragmentManager manager;
    DetailFragment detailFragment;
    UpdateFragment updateFragment;
    RefreshFragment refreshFragment;
    LoaderManager.LoaderCallbacks thisLoader;
    private Cursor c;
    private Button saleButton;
    private Button updateRecordButton;
    private TextView emptyDetailTexView;
    private View [] views = new View[]{null,null};
    private Bundle bundle;
    private TextView homeContainerHeader;
    private LinearLayout homebutton2Layout;
    private Button addInventory2;
    private View emptyView;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getLoaderManager().restartLoader(EXISTING_INV_LOADER,null,this);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getString(R.string.homeScreenTitle));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localUri = InventoryContract.InventoryEntry.CONTENT_URI;
        emptyView = findViewById(R.id.homeEmptyView);
        homeContainerHeader = findViewById(R.id.mainContainerHeader);
        Button addInventory = (Button) emptyView.findViewById(R.id.homeAddInventoryButton);
        addInventory2 = findViewById(R.id.homeAddInventoryButton2);
        addInventory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addInventory = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(addInventory);
            }
        });

        addInventory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addInventory = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(addInventory);
            }
        });

        inventoryListView = (ListView) findViewById(R.id.homeListView);
        inventoryCursorAdaptor = new InventoryCursorAdaptor(MainActivity.this,null);
        inventoryListView.setAdapter(inventoryCursorAdaptor);
        thisLoader = this;
        getLoaderManager().initLoader(EXISTING_INV_LOADER,null, thisLoader);
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

                detailFragment = new DetailFragment();
                refreshFragment = new RefreshFragment();
                views[0]=views[1];
                views[1]=view;
                if(views[0]!=null){
                    Button prevsaleButton = views[0].findViewById(R.id.saleItemButton);
                    Button prevUpdateButton = views[0].findViewById(R.id.itemUpdateInventoryRecord);
                    prevsaleButton.setVisibility(View.INVISIBLE);
                    prevUpdateButton.setVisibility(View.INVISIBLE);
                }
                TextView priceView = view.findViewById(R.id.itemPrice);
                TextView descriptionView = view.findViewById(R.id.itemDescription);
                TextView quantityView = view.findViewById(R.id.itemQuantity);
                TextView isbnView= view.findViewById(R.id.itemISBN);
                TextView supplierView= view.findViewById(R.id.itemSupplier);
                TextView phoneView= view.findViewById(R.id.itemSupplierPhone);
                saleButton = view.findViewById(R.id.saleItemButton);
                updateRecordButton = view.findViewById(R.id.itemUpdateInventoryRecord);
                currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);
                saleButton.setVisibility(View.VISIBLE);
                updateRecordButton.setVisibility(View.VISIBLE);
                price = (String) priceView.getText();
                description = (String) descriptionView.getText();
                quantity = (String) quantityView.getText();
                isbn = (String) isbnView.getText();
                supplier = (String) supplierView.getText();
                phone = (String) phoneView.getText();
                bundle = new Bundle();
                bundle.putString(getString(R.string.description),description);
                bundle.putString(getString(R.string.price),price);
                bundle.putString(getString(R.string.quantity),quantity);
                bundle.putString(getString(R.string.isbn),isbn);
                bundle.putString(getString(R.string.supplier),supplier);
                bundle.putString(getString(R.string.supplierPhone),phone);
                bundle.putString(getString(R.string._id),Long.toString(id));
                detailFragment.setArguments(bundle);
                manager = getFragmentManager();
                getSupportFragmentManager().beginTransaction().replace(R.id.detailedLayout,detailFragment,detailFragment.getClass().getSimpleName()).addToBackStack(null).commit();
                saleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int currentInv = Integer.parseInt(quantity);
                        if(currentInv > 0){

                            currentInv=currentInv-1;
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(InventoryContract.InventoryEntry.QUANTITY,Integer.toString(currentInv));
                            contentValues.put(InventoryEntry.PRODUCT_NAME,description);
                            contentValues.put(InventoryEntry.PRICE,price);
                            contentValues.put(InventoryEntry.ISBN10,isbn);
                            int rowsUpdated = getApplicationContext().getContentResolver().update(currentItemUri,contentValues,null,null);

                            if(rowsUpdated == 1){
                                Toast.makeText(getApplicationContext(),getString(R.string.unsuccessfulSale),Toast.LENGTH_LONG).show();
                                getLoaderManager().restartLoader(EXISTING_INV_LOADER,null,thisLoader);

                                if(detailFragment!=null){
                                    getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
                                } else if(refreshFragment!=null){
                                    getSupportFragmentManager().beginTransaction().remove(updateFragment).commit();
                                }
                                getSupportFragmentManager().beginTransaction().replace(R.id.detailedLayout,refreshFragment,refreshFragment.getClass().getSimpleName()).addToBackStack(null).commit();

                            }else {
                                Toast.makeText(getApplicationContext(),getString(R.string.unsuccessfulSale),Toast.LENGTH_LONG).show();
                            }

                        }else {
                            getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
                            Toast.makeText(getApplicationContext(),getString(R.string.noInventorytoSell),Toast.LENGTH_LONG).show();
                        }
                    }
                });

                updateRecordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateFragment = new UpdateFragment();
                        updateFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.detailedLayout,updateFragment,updateFragment.getClass().getSimpleName()).addToBackStack(null).commit();

                    }
                });
            }
        });

        inventoryListView.setEmptyView(emptyView);
    }

    public void updatedInvRecord(int int1, String s2){
        int b = int1;
        String message = s2;

        if (b == 1){
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            Activity thisActivity = this;
            View view = thisActivity.getCurrentFocus();
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
//            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.detailedLayout,refreshFragment,refreshFragment.getClass().getSimpleName()).addToBackStack(null).commit();
            b = 0;
            getLoaderManager().restartLoader(EXISTING_INV_LOADER,null,this);
            int noListItems = inventoryListView.getCount();

            if (noListItems==1){
                addInventory2.setVisibility(View.INVISIBLE);
                homeContainerHeader.setVisibility(View.INVISIBLE);
            } else{
                addInventory2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {InventoryContract.InventoryEntry._ID, InventoryContract.InventoryEntry.ISBN10,
                InventoryContract.InventoryEntry.SUPPLIER_NAME, InventoryEntry.SUPPLIER_PHONE_NUMBER,
                InventoryContract.InventoryEntry.PRICE, InventoryContract.InventoryEntry.QUANTITY, InventoryContract.InventoryEntry.PRODUCT_NAME};
        CursorLoader thisLoader = new CursorLoader(this,localUri,projection,null,null,null);


        if(thisLoader==null){

            return null;
        }
        else{

            return thisLoader;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        inventoryCursorAdaptor.notifyDataSetChanged();
        inventoryCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        inventoryCursorAdaptor.swapCursor(null);
    }
}
