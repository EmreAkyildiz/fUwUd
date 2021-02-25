package com.impostors.fuwud.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User {
    private String email, password, name, surname,phoneNumber;
    private String user_id;
    private double longitude,latitude;
    private HashMap<String,Restaurant> favoritedRestaurant;
    private HashMap<String,Product> currentBasket;
    private String birthday;
    private HashMap<String,PrevOrder> completedOrders;

    public User(String email, String name, String surname, String phoneNumber, String user_id) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.user_id = user_id;
    }

    public HashMap<String, Product> getCurrentBasket() {
        return currentBasket;
    }

    public void setCurrentBasket(HashMap<String, Product> currentBasket) {
        this.currentBasket = currentBasket;
    }


    public void setCompletedOrders(HashMap<String, PrevOrder> completedOrders) {
        this.completedOrders = completedOrders;
    }

    public HashMap<String, PrevOrder> getCompletedOrders() {
        return completedOrders;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Restaurant> getFavoritedRestaurant() {
        return favoritedRestaurant;
    }

    public void setFavoritedRestaurant(HashMap<String, Restaurant> favoritedRestaurant) {
        this.favoritedRestaurant = favoritedRestaurant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
