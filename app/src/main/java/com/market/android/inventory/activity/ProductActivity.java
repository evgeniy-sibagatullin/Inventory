package com.market.android.inventory.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.market.android.inventory.R;
import com.market.android.inventory.data.ProductContract.ProductEntry;
import com.market.android.inventory.model.Product;

import java.util.regex.Pattern;

import static com.market.android.inventory.data.ProductContract.ProductEntry.PROJECTION;

public class ProductActivity extends AppCompatActivity {

    private static final String MAIL_REG_EXP = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private Uri mCurrentProductUri;
    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductSupplierMail;
    private EditText mProductQuantity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        setTitle(getString(mCurrentProductUri == null ?
                R.string.add_product :
                R.string.edit_product));

        mProductName = (EditText) findViewById(R.id.edit_product_name);
        mProductPrice = (EditText) findViewById(R.id.edit_product_price);
        mProductSupplierMail = (EditText) findViewById(R.id.edit_product_supplier_mail);
        mProductQuantity = (EditText) findViewById(R.id.edit_product_quantity);

        if (mCurrentProductUri != null) {
            Cursor cursor = getContentResolver().query(mCurrentProductUri, PROJECTION, null, null, null);

            if (cursor != null) {
                if (cursor.moveToNext()) {
                    Product product = ProductEntry.getProductFromCursor(cursor);
                    mProductName.setText(product.getName());
                    mProductPrice.setText(Integer.toString(product.getPrice()));
                    mProductSupplierMail.setText(product.getSupplierMail());
                    mProductQuantity.setText(Integer.toString(product.getQuantity()));
                }

                cursor.close();
            }
        }

        findViewById(R.id.inc_quantity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = mProductQuantity.getText().toString();
                if (quantityStr.isEmpty()) {
                    mProductQuantity.setText("1");
                } else {
                    mProductQuantity.setText(Integer.toString(Integer.parseInt(quantityStr) + 1));
                }
            }
        });

        findViewById(R.id.dec_quantity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = mProductQuantity.getText().toString();
                if (quantityStr.isEmpty()) {
                    mProductQuantity.setText("0");
                } else {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity > 0) mProductQuantity.setText(Integer.toString(quantity - 1));
                }
            }
        });
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
                if (saveProduct()) {
                    finish();
                }
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean saveProduct() {
        Product product;
        try {
            product = prepareAndValidateProduct();
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mCurrentProductUri == null) {
            insertProduct(product);
        } else {
            updateProduct(product);
        }

        return true;
    }

    private Product prepareAndValidateProduct() throws IllegalArgumentException {
        String name = mProductName.getText().toString().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(getString(R.string.enter_name));
        }

        String priceStr = mProductPrice.getText().toString().trim();
        if (priceStr.isEmpty() || priceStr.startsWith("-")) {
            throw new IllegalArgumentException(getString(R.string.enter_price));
        }
        int price = Integer.parseInt(priceStr);

        String supplierMail = mProductSupplierMail.getText().toString().trim();
        if (supplierMail.isEmpty() || !isEmailValid(supplierMail)) {
            throw new IllegalArgumentException(getString(R.string.enter_mail));
        }

        String quantityStr = mProductQuantity.getText().toString().trim();
        if (quantityStr.isEmpty() || quantityStr.startsWith("-")) {
            throw new IllegalArgumentException(getString(R.string.enter_quantity));
        }
        int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

        return new Product(name, price, supplierMail, quantity);
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile(MAIL_REG_EXP, Pattern.CASE_INSENSITIVE).matcher(email).matches();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.product_delete_request);
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
        builder.create().show();
    }

    private ContentValues prepareProductValues(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL, product.getSupplierMail());
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        return values;
    }

    private void insertProduct(Product product) {
        getContentResolver().insert(ProductEntry.CONTENT_URI, prepareProductValues(product));
    }

    private void updateProduct(Product product) {
        getContentResolver().update(mCurrentProductUri, prepareProductValues(product), null, null);
    }

    private void deleteProduct() {
        getContentResolver().delete(mCurrentProductUri, null, null);
        finish();
    }
}
