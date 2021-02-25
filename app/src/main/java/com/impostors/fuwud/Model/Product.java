package com.impostors.fuwud.Model;

public class Product {
    public String name;
    public double buyPrice;
    private String restaurant_id;
    public int count;


    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Product() {
    }

    public Product(String name, double buyPrice) {
        this.name = name;
        this.buyPrice = buyPrice;

    }

    @Override
    public String toString() {
        return
                "adı='" + name + '\'' +
                ", fiyatı=" + buyPrice +
                        ", adeti= "+count+"\n";

    }

    public String getName() {
        return name;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
