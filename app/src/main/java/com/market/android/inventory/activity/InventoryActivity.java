package com.market.android.inventory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.market.android.inventory.R;
import com.market.android.inventory.adapter.ProductAdapter;
import com.market.android.inventory.model.Product;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        ProductAdapter productAdapter = new ProductAdapter(this, getDummyProducts());
        ListView listView = (ListView) findViewById(R.id.product_list);
        listView.setAdapter(productAdapter);
    }

    private List<Product> getDummyProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Apple", 10, 500));
        products.add(new Product("Banana", 30, 10));
        products.add(new Product("Orange", 20, 35));
        return products;
    }
}
