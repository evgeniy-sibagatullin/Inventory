package com.market.android.inventory.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.market.android.inventory.R;
import com.market.android.inventory.adapter.ProductCursorAdapter;
import com.market.android.inventory.data.ProductContract.ProductEntry;
import com.market.android.inventory.data.ProductDbHelper;
import com.market.android.inventory.model.Product;

import static com.market.android.inventory.data.ProductContract.ProductEntry.PROJECTION;
import static com.market.android.inventory.data.ProductContract.ProductEntry.TABLE_NAME;

public class InventoryActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_PRODUCT = "extra_key_product";

    private ProductCursorAdapter mProductCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Button addProductButton = (Button) findViewById(R.id.add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });

        mProductCursorAdapter = new ProductCursorAdapter(this, getProductsCursor());
        ListView listView = (ListView) findViewById(R.id.product_list);
        listView.setAdapter(mProductCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(InventoryActivity.this, ProductActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProductCursorAdapter.swapCursor(getProductsCursor());
    }

    private Cursor getProductsCursor() {
        ProductDbHelper dbHelper = new ProductDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, PROJECTION, null, null, null, null, null);

        if (!cursor.moveToNext()) {
            insertDummyProducts();
            cursor = getProductsCursor();
        }

        return cursor;
    }

    private void insertDummyProducts() {
        insertProduct(new Product("Apple", 10, "someTitle@apple.com", 500));
        insertProduct(new Product("Banana", 30, "otherTitle@banana.com", 10));
        insertProduct(new Product("Orange", 20, "lastTitle@orange.com", 35));
    }

    private void insertProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL, product.getSupplierMail());
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        getContentResolver().insert(ProductEntry.CONTENT_URI, values);
    }
}
