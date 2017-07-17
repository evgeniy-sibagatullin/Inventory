package com.market.android.inventory.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ProductContract {

    static final String CONTENT_AUTHORITY = "com.market.android.inventory";
    static final String PATH_PRODUCTS = "products";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        public final static String TABLE_NAME = "products";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "name";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_SUPPLIER_MAIL = "supplier_mail";
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
    }
}
