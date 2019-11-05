package com.badrul.qnitiboxdelivery;

public class Job {

    //private int userid;
    private String orderID;
    private String sellerName;
    private String sellerLocation;
    private String sellerPhone;
    private String receiverName;
    private String receiverLocation;
    private String receiverPhone;
    private String foodName;

    public Job(String orderID, String sellerName, String sellerLocation, String sellerPhone, String receiverName,String receiverLocation, String receiverPhone, String foodName) {

        this.orderID = orderID;
        this.sellerName = sellerName;
        this.sellerLocation = sellerLocation;
        this.sellerPhone = sellerPhone;
        this.receiverName = receiverName;
        this.receiverLocation = receiverLocation;
        this.receiverPhone = receiverPhone;
        this.foodName = foodName;

    }

    public String getOrderID() {
        return orderID;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerLocation() {
        return sellerLocation;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverLocation() {
        return receiverLocation;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getFoodName() {
        return foodName;
    }

}
