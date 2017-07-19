package com.market.android.inventory.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.market.android.inventory.R;
import com.market.android.inventory.data.ProductContract.ProductEntry;
import com.market.android.inventory.model.Product;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ProductCursorAdapter extends CursorAdapter {

    private final Context mContext;

    public ProductCursorAdapter(Context mContext, Cursor cursor) {
        super(mContext, cursor, 0);
        this.mContext = mContext;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        final Product product = ProductEntry.getProductFromCursor(cursor);

        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView productPrice = (TextView) view.findViewById(R.id.product_price);
        TextView productQuantity = (TextView) view.findViewById(R.id.product_quantity);

        productName.setText(product.getName());
        productPrice.setText(mContext.getString(R.string.dollar) + product.getPrice());
        productQuantity.setText(Integer.toString(product.getQuantity()));

        Button saleButton = (Button) view.findViewById(R.id.sale_button);
        if (product.getQuantity() == 0) {
            saleButton.setVisibility(INVISIBLE);
        } else {
            final int id = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
            saleButton.setVisibility(VISIBLE);
            saleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
                    contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, product.getPrice());
                    contentValues.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_MAIL, product.getSupplierMail());
                    contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getQuantity() - 1);
                    mContext.getContentResolver().update(currentProductUri, contentValues, null, null);
                }
            });
        }
    }

}