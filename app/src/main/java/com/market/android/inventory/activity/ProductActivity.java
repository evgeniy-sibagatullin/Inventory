package com.market.android.inventory.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.market.android.inventory.R;
import com.market.android.inventory.data.ProductContract.ProductEntry;

import static com.market.android.inventory.data.ProductContract.ProductEntry.PROJECTION;

public class ProductActivity extends AppCompatActivity {

    private Uri mCurrentProductUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        setTitle(getString(mCurrentProductUri == null ?
                R.string.add_product :
                R.string.edit_product));

        if (mCurrentProductUri != null) {
            Cursor cursor = getContentResolver().query(mCurrentProductUri, PROJECTION, null, null, null);

            if (cursor.moveToNext()) {
                int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
                int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
                int supplierMailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL);
                int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
                EditText productName = (EditText) findViewById(R.id.edit_product_name);
                productName.setText(cursor.getString(nameColumnIndex));
                EditText productPrice = (EditText) findViewById(R.id.edit_product_price);
                productPrice.setText(Integer.toString(cursor.getInt(priceColumnIndex)));
                EditText productSupplierMail = (EditText) findViewById(R.id.edit_product_supplier_mail);
                productSupplierMail.setText(cursor.getString(supplierMailColumnIndex));
                EditText productQuatity = (EditText) findViewById(R.id.edit_product_quantity);
                productQuatity.setText(Integer.toString(cursor.getInt(quantityColumnIndex)));
            }

            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_product, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentProductUri == null) {
            MenuItem menuItemDelete = menu.findItem(R.id.action_delete);
            menuItemDelete.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
//                savePet();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_product_request);

        builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });

        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            Toast.makeText(this, getString(rowsDeleted == 0 ? R.string.product_delete_failed :
                    R.string.product_delete_succeeded), Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
