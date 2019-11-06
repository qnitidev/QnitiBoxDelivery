package com.badrul.qnitiboxdelivery;

public class Job {

    //private int userid;
    private String orderID;
    private String sellerName;
    private String sellerAddress;
    private String sellerPhone;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String foodName;
    private String foodPrice;
    private String orderStatus;

    public Job(String orderID, String sellerName, String sellerAddress, String sellerPhone, String receiverName,String receiverAddress, String receiverPhone, String foodName, String foodPrice, String orderStatus) {

        this.orderID = orderID;
        this.sellerName = sellerName;
        this.sellerAddress = sellerAddress;
        this.sellerPhone = sellerPhone;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverPhone = receiverPhone;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.orderStatus = orderStatus;

    }

    public String getOrderID() {
        return orderID;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

}
