package com.impostors.fuwud.Model;

import java.util.ArrayList;

public class Menu {
    String menuName;
    ArrayList<Product> products;

    public Menu() {
    }

    public Menu(String menuName, ArrayList products) {
        this.menuName = menuName;
        this.products = products;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
