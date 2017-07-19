package com.market.android.inventory.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.market.android.inventory.model.Product;

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
        public final static String[] PROJECTION = new String[]{_ID, COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_PRICE, COLUMN_PRODUCT_SUPPLIER_MAIL, COLUMN_PRODUCT_QUANTITY};

        public static Product getProductFromCursor(Cursor cursor) {
            int nameColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_PRICE);
            int supplierMailColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_SUPPLIER_MAIL);
            int quantityColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String supplierMail = cursor.getString(supplierMailColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);

            return new Product(name, price, supplierMail, quantity);
        }
    }
}
