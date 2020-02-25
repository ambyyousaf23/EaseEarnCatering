package com.example.qureshi.easeearncatering.DataModels;

/**
 * Created by qureshi on 02/05/2018.
 */

public class SubMenuDataModel {

    String id, menu_id, dishes, image, price;

    public SubMenuDataModel() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getDishes() {
        return dishes;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
