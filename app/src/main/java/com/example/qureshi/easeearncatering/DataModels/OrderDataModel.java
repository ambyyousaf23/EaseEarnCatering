package com.example.qureshi.easeearncatering.DataModels;

public class OrderDataModel {

    String id, menu_id, submenu_id, dish, user_id, quantity, amount, total, datetime, image, email;

    public OrderDataModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getSubmenu_id() {
        return submenu_id;
    }

    public void setSubmenu_id(String submenu_id) {
        this.submenu_id = submenu_id;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
