package model.dto;

import java.io.Serializable;

public class OrderDetailDTO implements Serializable {
    private int orderDetailID;
    private int orderID;
    private int productID;
    private String productName;
    private int quantity;
    private double price;
    
    public OrderDetailDTO() {
    }
    
    public OrderDetailDTO(int orderDetailID, int orderID, int productID, String productName, int quantity, double price) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Getters and setters
    public int getOrderDetailID() {
        return orderDetailID;
    }
    
    public void setOrderDetailID(int orderDetailID) {
        this.orderDetailID = orderDetailID;
    }
    
    public int getOrderID() {
        return orderID;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    
   
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getTotal() {
        return price * quantity;
    }
}