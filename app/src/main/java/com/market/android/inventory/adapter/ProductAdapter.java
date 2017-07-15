package com.market.android.inventory.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.market.android.inventory.R;
import com.market.android.inventory.activity.ProductActivity;
import com.market.android.inventory.model.Product;

import java.util.List;

import static com.market.android.inventory.activity.InventoryActivity.EXTRA_KEY_PRODUCT;

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

        final Product product = getItem(position);

        if (product != null) {
            TextView productName = (TextView) listItemView.findViewById(R.id.product_name);
            productName.setText(product.getName());

            TextView productPrice = (TextView) listItemView.findViewById(R.id.product_price);
            productPrice.setText(mContext.getString(R.string.dollar) + product.getPrice());

            TextView productQuatity = (TextView) listItemView.findViewById(R.id.product_quantity);
            productQuatity.setText(Integer.toString(product.getQuantity()));
        }

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductActivity.class);
                intent.putExtra(EXTRA_KEY_PRODUCT, product);
                mContext.startActivity(intent);
            }
        });

        return listItemView;
    }
}