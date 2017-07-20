package com.market.android.inventory.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.market.android.inventory.R;
import com.market.android.inventory.data.ProductContract.ProductEntry;
import com.market.android.inventory.model.Product;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static com.market.android.inventory.activity.InventoryActivity.PRODUCT_LOADER;
import static com.market.android.inventory.data.ProductContract.ProductEntry.PROJECTION;

public class ProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MAIL_REG_EXP = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MAIL_BLUEPRINT = "mailto:%s?subject=%s&body=%s";
    private static final int REQUEST_LOAD_IMAGE_CODE = 200;
    private static final String PRODUCT_IMAGE_URI_STUB = "product images not used below kitkat";

    private Uri mCurrentProductUri;
    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductSupplierMail;
    private EditText mProductQuantity;
    private ImageView mProductImage;
    private Uri mProductImageUri;

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
        mProductImage = (ImageView) findViewById(R.id.product_image);

        if (mCurrentProductUri != null) {
            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
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

        Button selectProductImageButton = (Button) findViewById(R.id.select_product_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            selectProductImageButton.setVisibility(View.VISIBLE);
            selectProductImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    startActivityForResult(photoPickerIntent, REQUEST_LOAD_IMAGE_CODE);
                }
            });
        } else {
            mProductImageUri = Uri.parse(PRODUCT_IMAGE_URI_STUB);
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
            throw new IllegalArgumentException(getString(R.string.toast_enter_name));
        }

        String priceStr = mProductPrice.getText().toString().trim();
        if (priceStr.isEmpty() || priceStr.startsWith("-")) {
            throw new IllegalArgumentException(getString(R.string.toast_enter_price));
        }
        int price = Integer.parseInt(priceStr);

        String supplierMail = mProductSupplierMail.getText().toString().trim();
        if (supplierMail.isEmpty() || !isEmailValid(supplierMail)) {
            throw new IllegalArgumentException(getString(R.string.toast_enter_mail));
        }

        String quantityStr = mProductQuantity.getText().toString().trim();
        if (quantityStr.isEmpty() || quantityStr.startsWith("-")) {
            throw new IllegalArgumentException(getString(R.string.toast_enter_quantity));
        }
        int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

        if (mProductImageUri == null || mProductImageUri.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(getString(R.string.toast_select_product_image));
        }

        return new Product(name, price, supplierMail, quantity, mProductImageUri.toString());
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
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE_URI_STRING, mProductImageUri.toString());
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, mCurrentProductUri, PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                Product product = ProductEntry.getProductFromCursor(cursor);
                initializeOrderButton(product);

                mProductName.setText(product.getName());
                mProductPrice.setText(Integer.toString(product.getPrice()));
                mProductSupplierMail.setText(product.getSupplierMail());
                mProductQuantity.setText(Integer.toString(product.getQuantity()));

                mProductImageUri = Uri.parse(product.getImageUriString());
                if (!PRODUCT_IMAGE_URI_STUB.equals(product.getImageUriString())) {
                    updateProductImageByUri();
                }
            }
            cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductName.setText("");
        mProductPrice.setText("");
        mProductSupplierMail.setText("");
        mProductQuantity.setText("");
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == REQUEST_LOAD_IMAGE_CODE && resultCode == RESULT_OK) {
            mProductImageUri = data.getData();
            updateProductImageByUri();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(mProductImageUri, takeFlags);
            }
        }
    }

    private void updateProductImageByUri() {
        try {
            InputStream imageStream = getContentResolver().openInputStream(mProductImageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            mProductImage.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, R.string.toast_image_file_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeOrderButton(final Product product) {
        Button orderProductButton = (Button) findViewById(R.id.order_product);
        orderProductButton.setVisibility(View.VISIBLE);
        orderProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = String.format(MAIL_BLUEPRINT,
                        Uri.encode(product.getSupplierMail()),
                        String.format(getString(R.string.mail_subject),
                                Uri.encode(product.getName())),
                        String.format(getString(R.string.mail_body),
                                Uri.encode(Integer.toString(product.getPrice())),
                                Uri.encode(Integer.toString(product.getQuantity()))));
                intent.setData(Uri.parse(uriText));
                startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
            }
        });
    }
}
