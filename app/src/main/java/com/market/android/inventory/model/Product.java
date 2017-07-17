package com.market.android.inventory.model;

import java.io.Serializable;

public class Product implements Serializable {

    private final String name;
    private final int price;
    private final String supplierMail;
    private final int quantity;

    public Product(String name, int price, String supplierMail, int quantity) {
        this.name = name;
        this.price = price;
        this.supplierMail = supplierMail;
        this.quantity = quantity;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return price == product.price &&
                quantity == product.quantity &&
                name.equals(product.name) &&
                supplierMail.equals(product.supplierMail);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + price;
        result = 31 * result + supplierMail.hashCode();
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", supplierMail='" + supplierMail + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
