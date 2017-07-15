package com.market.android.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.market.android.inventory.R;

public class ProductActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        setTitle(getString(intent.getData() == null ?
                R.string.add_product :
                R.string.edit_product));
    }
}
