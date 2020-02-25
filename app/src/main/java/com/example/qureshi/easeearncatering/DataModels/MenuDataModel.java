package com.example.qureshi.easeearncatering.DataModels;

/**
 * Created by qureshi on 19/03/2018.
 */

public class MenuDataModel {

    String menu_id, cater_id, dishes, image;

    public MenuDataModel() {
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCater_id() {
        return cater_id;
    }

    public void setCater_id(String cater_id) {
        this.cater_id = cater_id;
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
