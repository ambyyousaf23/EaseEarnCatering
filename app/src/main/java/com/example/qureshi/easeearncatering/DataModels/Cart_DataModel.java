package com.example.qureshi.easeearncatering.DataModels;

/**
 * Created by Raza Saeed on 3/6/2018.
 */

public class Cart_DataModel {

    String actual_price, cart_id, datetime, product_id, quantity, total_amount, user_id, prod_img, prod_name, total_stock, sale_price, description, stock_id, mart_id,  tax;

    public Cart_DataModel() {
    }

    public String getMart_id() {
        return mart_id;
    }

    public void setMart_id(String mart_id) {
        this.mart_id = mart_id;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProd_img() {
        return prod_img;
    }

    public void setProd_img(String prod_img) {
        this.prod_img = prod_img;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getTotal_stock() {
        return total_stock;
    }

    public void setTotal_stock(String total_stock) {
        this.total_stock = total_stock;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }
}
