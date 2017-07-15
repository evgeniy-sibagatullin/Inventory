package com.market.android.inventory.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.market.android.inventory.R;
import com.market.android.inventory.model.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private final Context mContext;

    public ProductAdapter(Context mContext, List<Product> products) {
        super(mContext, 0, products);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.product_list_item, parent, false);
        }

        Product product = getItem(position);

        TextView productName = (TextView) listItemView.findViewById(R.id.product_name);
        if (product != null) {
            productName.setText(product.getName());
        }

        TextView productPrice = (TextView) listItemView.findViewById(R.id.product_price);
        if (product != null) {
            productPrice.setText(mContext.getString(R.string.dollar) + product.getPrice());
        }

        TextView productQuatity = (TextView) listItemView.findViewById(R.id.product_quantity);
        if (product != null) {
            productQuatity.setText(Integer.toString(product.getQuantity()));
        }

        return listItemView;
    }
}