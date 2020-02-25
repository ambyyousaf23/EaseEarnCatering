package com.example.qureshi.easeearncatering.DataModels;

/**
 * Created by qureshi on 19/03/2018.
 */

public class UsersDataModel {

    String id, firstname, lastname, email, password, contact, location, is_cater;

    public UsersDataModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIs_cater() {
        return is_cater;
    }

    public void setIs_cater(String is_cater) {
        this.is_cater = is_cater;
    }
}
