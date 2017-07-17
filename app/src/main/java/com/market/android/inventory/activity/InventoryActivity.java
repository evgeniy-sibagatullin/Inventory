package com.market.android.inventory.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.market.android.inventory.R;
import com.market.android.inventory.adapter.ProductAdapter;
import com.market.android.inventory.data.ProductContract.ProductEntry;
import com.market.android.inventory.data.ProductDbHelper;
import com.market.android.inventory.model.Product;

import java.util.ArrayList;
import java.util.List;

import static com.market.android.inventory.data.ProductContract.ProductEntry.TABLE_NAME;

public class InventoryActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_PRODUCT = "extra_key_product";

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

        ProductAdapter productAdapter = new ProductAdapter(this, getProducts());
        ListView listView = (ListView) findViewById(R.id.product_list);
        listView.setAdapter(productAdapter);
    }

    private List<Product> getProducts() {
        ProductDbHelper dbHelper = new ProductDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL,
                ProductEntry.COLUMN_PRODUCT_QUANTITY};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        List<Product> products = new ArrayList<>();
        if (!cursor.moveToNext()) {
            insertDummyProducts();
            products = getProducts();
        } else {
            cursor.moveToPrevious();
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL);
            int supplierMailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

            while (cursor.moveToNext()) {
                products.add(new Product(cursor.getString(nameColumnIndex),
                        cursor.getInt(priceColumnIndex),
                        cursor.getString(supplierMailColumnIndex),
                        cursor.getInt(quantityColumnIndex)));
            }

            cursor.close();
        }

        return products;
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
