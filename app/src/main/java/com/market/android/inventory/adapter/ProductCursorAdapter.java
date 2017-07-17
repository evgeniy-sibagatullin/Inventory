package com.market.android.inventory.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.market.android.inventory.R;
import com.market.android.inventory.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    private final Context mContext;

    public ProductCursorAdapter(Context mContext, Cursor cursor) {
        super(mContext, cursor, 0);
        this.mContext = mContext;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView productPrice = (TextView) view.findViewById(R.id.product_price);
        TextView productQuatity = (TextView) view.findViewById(R.id.product_quantity);

        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

        productName.setText(cursor.getString(nameColumnIndex));
        productPrice.setText(mContext.getString(R.string.dollar) + cursor.getInt(priceColumnIndex));
        productQuatity.setText(Integer.toString(cursor.getInt(quantityColumnIndex)));
    }
}