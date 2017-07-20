package com.market.android.inventory.model;

import java.io.Serializable;

public class Product implements Serializable {

    private final String name;
    private final int price;
    private final String supplierMail;
    private final int quantity;
    private final String imageUriString;

    public Product(String name, int price, String supplierMail, int quantity, String imageUriString) {
        this.name = name;
        this.price = price;
        this.supplierMail = supplierMail;
        this.quantity = quantity;
        this.imageUriString = imageUriString;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getSupplierMail() {
        return supplierMail;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUriString() {
        return imageUriString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return price == product.price &&
                quantity == product.quantity &&
                name.equals(product.name) &&
                supplierMail.equals(product.supplierMail) &&
                imageUriString.equals(product.imageUriString);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + price;
        result = 31 * result + supplierMail.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + imageUriString.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", supplierMail='" + supplierMail + '\'' +
                ", quantity=" + quantity +
                ", imageUriString=" + imageUriString +
                '}';
    }
}
