package com.market.android.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.market.android.inventory.R;
import com.market.android.inventory.adapter.ProductAdapter;
import com.market.android.inventory.model.Product;

import java.util.ArrayList;
import java.util.List;

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

        ProductAdapter productAdapter = new ProductAdapter(this, getDummyProducts());
        ListView listView = (ListView) findViewById(R.id.product_list);
        listView.setAdapter(productAdapter);
    }

    private List<Product> getDummyProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Apple", 10, "someTitle@apple.com", 500));
        products.add(new Product("Banana", 30, "otherTitle@banana.com", 10));
        products.add(new Product("Orange", 20, "lastTitle@orange.com", 35));
        return products;
    }
}
