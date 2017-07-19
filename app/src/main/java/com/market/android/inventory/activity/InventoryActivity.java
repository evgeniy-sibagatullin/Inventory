package com.market.android.inventory.activity;

import android.content.ContentUris;
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

import static com.market.android.inventory.data.ProductContract.ProductEntry.PROJECTION;
import static com.market.android.inventory.data.ProductContract.ProductEntry.TABLE_NAME;

public class InventoryActivity extends AppCompatActivity {

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
        listView.setEmptyView(findViewById(R.id.empty_view));
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
        return db.query(TABLE_NAME, PROJECTION, null, null, null, null, null);
    }
}
