package com.badrul.qnitiboxdelivery;

public class User {

    //private int userid;
    private int delivererID;
    private String delivererUserName;
    private String delivererName;
    private String delivererEmail;
    private String delivererPhone;
    private String delivererLocation;

    public User(int delivererID, String delivererUserName, String delivererName, String delivererEmail, String delivererPhone, String delivererLocation) {

        this.delivererID = delivererID;
        this.delivererUserName = delivererUserName;
        this.delivererName = delivererName;
        this.delivererEmail = delivererEmail;
        this.delivererPhone = delivererPhone;
        this.delivererLocation = delivererLocation;

    }

    public int getDelivererID() {
        return delivererID;
    }

    public String getDelivererUserName() {
        return delivererUserName;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public String getDelivererEmail() {
        return delivererEmail;
    }


    public String getDelivererPhone() {
        return delivererPhone;
    }

    public String getDelivererLocation() {
        return delivererLocation;
    }

}
