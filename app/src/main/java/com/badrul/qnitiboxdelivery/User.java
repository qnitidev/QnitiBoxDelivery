package com.badrul.qnitiboxdelivery;

public class User {

    //private int userid;
    private int delivererID;
    private String delivererName;
    private String delivererEmail;
    private String delivererLocation;
    private String delivererPhone;
    private String activeStatus;


    public User(int delivererID, String delivererName, String delivererEmail, String delivererLocation, String delivererPhone, String activeStatus) {

        this.delivererID = delivererID;
        this.delivererName = delivererName;
        this.delivererEmail = delivererEmail;
        this.delivererLocation = delivererLocation;
        this.delivererPhone = delivererPhone;
        this.activeStatus = activeStatus;


    }

    public int getDelivererID() {
        return delivererID;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public String getDelivererEmail() {
        return delivererEmail;
    }

    public String getDelivererLocation() {
        return delivererLocation;
    }

    public String getDelivererPhone() {
        return delivererPhone;
    }
    public String getActiveStatus() {
        return activeStatus;
    }

}
