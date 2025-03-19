package model.dto;

import java.io.Serializable;

public class OrderDetailDTO implements Serializable {
    private int orderDetailID;
    private int orderID;
    private int bookID;
    private String bookTitle;
    private int quantity;
    private double price;
    
    public OrderDetailDTO() {
    }
    
    public OrderDetailDTO(int orderDetailID, int orderID, int bookID, String bookTitle, int quantity, double price) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.bookID = bookID;
        this.bookTitle = bookTitle;
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
    
    public int getBookID() {
        return bookID;
    }
    
    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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