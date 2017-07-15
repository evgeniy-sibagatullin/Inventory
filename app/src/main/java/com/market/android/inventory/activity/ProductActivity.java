package com.market.android.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.market.android.inventory.R;
import com.market.android.inventory.model.Product;

import static com.market.android.inventory.activity.InventoryActivity.EXTRA_KEY_PRODUCT;

public class ProductActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        setTitle(getString(bundle == null ?
                R.string.add_product :
                R.string.edit_product));

        if (bundle != null) {
            Product product = (Product) bundle.getSerializable(EXTRA_KEY_PRODUCT);

            if (product != null) {
                EditText productName = (EditText) findViewById(R.id.edit_product_name);
                productName.setText(product.getName());

                EditText productPrice = (EditText) findViewById(R.id.edit_product_price);
                productPrice.setText(Integer.toString(product.getPrice()));

                EditText productQuatity = (EditText) findViewById(R.id.edit_product_quantity);
                productQuatity.setText(Integer.toString(product.getQuantity()));
            }
        }
    }
}
