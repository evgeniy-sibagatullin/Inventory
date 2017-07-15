package com.market.android.inventory.model;

public class Product {

    private final String name;
    private final int price;
    private final int quantity;

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return price == product.price && quantity == product.quantity && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + price;
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
