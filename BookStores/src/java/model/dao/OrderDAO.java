package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.dto.OrderDTO;
import model.dto.OrderDetailDTO;
import model.utils.DBUtils;

public class OrderDAO {
    
    public int createOrder(OrderDTO order) throws SQLException, ClassNotFoundException {
        int orderID = -1;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);
            
            String sql = "INSERT INTO Orders(userID, orderDate, totalAmount, customerName, "
                       + "customerEmail, customerPhone, customerAddress, paymentMethod, paymentStatus) "
                       + "VALUES(?, GETDATE(), ?, ?, ?, ?, ?, ?, ?)";
            
            stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, order.getUserID());
            stm.setDouble(2, order.getTotalAmount());
            stm.setString(3, order.getCustomerName());
            stm.setString(4, order.getCustomerEmail());
            stm.setString(5, order.getCustomerPhone());
            stm.setString(6, order.getCustomerAddress());
            stm.setString(7, order.getPaymentMethod());
            stm.setString(8, order.getPaymentStatus());
            
            int affectedRows = stm.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    orderID = rs.getInt(1);
                    
                    // Insert order details
                    boolean detailsInserted = insertOrderDetails(conn, orderID, order.getOrderDetails());
                    
                    if (detailsInserted) {
                        conn.commit();
                    } else {
                        conn.rollback();
                        orderID = -1;
                    }
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                DBUtils.closeConnection(conn);
            }
        }
        
        return orderID;
    }
    
    private boolean insertOrderDetails(Connection conn, int orderID, List<OrderDetailDTO> orderDetails) 
            throws SQLException, ClassNotFoundException {
        PreparedStatement stm = null;
        boolean success = true;
        
        try {
            String sql = "INSERT INTO OrderDetails(orderID, bookID, quantity, price) "
                       + "VALUES(?, ?, ?, ?)";
            
            stm = conn.prepareStatement(sql);
            
            for (OrderDetailDTO detail : orderDetails) {
                stm.setInt(1, orderID);
                stm.setInt(2, detail.getBookID());
                stm.setInt(3, detail.getQuantity());
                stm.setDouble(4, detail.getPrice());
                
                int affectedRows = stm.executeUpdate();
                
                if (affectedRows <= 0) {
                    success = false;
                    break;
                }
                
                // Update book quantity
                BookDAO bookDAO = new BookDAO();
                boolean updated = bookDAO.updateQuantity(detail.getBookID(), detail.getQuantity());
                
                if (!updated) {
                    success = false;
                    break;
                }
            }
        } finally {
            if (stm != null) stm.close();
        }
        
        return success;
    }
    
    public OrderDTO getOrderByID(int orderID) throws SQLException, ClassNotFoundException {
        OrderDTO order = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT orderID, userID, orderDate, totalAmount, customerName, "
                       + "customerEmail, customerPhone, customerAddress, paymentMethod, paymentStatus "
                       + "FROM Orders "
                       + "WHERE orderID = ?";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, orderID);
            
            rs = stm.executeQuery();
            
            if (rs.next()) {
                String userID = rs.getString("userID");
                Date orderDate = rs.getTimestamp("orderDate");
                double totalAmount = rs.getDouble("totalAmount");
                String customerName = rs.getString("customerName");
                String customerEmail = rs.getString("customerEmail");
                String customerPhone = rs.getString("customerPhone");
                String customerAddress = rs.getString("customerAddress");
                String paymentMethod = rs.getString("paymentMethod");
                String paymentStatus = rs.getString("paymentStatus");
                
                order = new OrderDTO(orderID, userID, orderDate, totalAmount, customerName, 
                                    customerEmail, customerPhone, customerAddress, 
                                    paymentMethod, paymentStatus);
                
                // Get order details
                List<OrderDetailDTO> orderDetails = getOrderDetails(orderID);
                order.setOrderDetails(orderDetails);
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return order;
    }
    
    private List<OrderDetailDTO> getOrderDetails(int orderID) throws SQLException, ClassNotFoundException {
        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT od.orderDetailID, od.orderID, od.bookID, b.title, od.quantity, od.price "
                       + "FROM OrderDetails od JOIN Books b ON od.bookID = b.bookID "
                       + "WHERE od.orderID = ?";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, orderID);
            
            rs = stm.executeQuery();
            
            while (rs.next()) {
                int orderDetailID = rs.getInt("orderDetailID");
                int bookID = rs.getInt("bookID");
                String bookTitle = rs.getString("title");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                
                orderDetails.add(new OrderDetailDTO(orderDetailID, orderID, bookID, bookTitle, quantity, price));
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return orderDetails;
    }
    
    public List<OrderDTO> getOrdersByUserID(String userID) throws SQLException, ClassNotFoundException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT orderID, userID, orderDate, totalAmount, customerName, "
                       + "customerEmail, customerPhone, customerAddress, paymentMethod, paymentStatus "
                       + "FROM Orders "
                       + "WHERE userID = ? "
                       + "ORDER BY orderDate DESC";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, userID);
            
            rs = stm.executeQuery();
            
            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                Date orderDate = rs.getTimestamp("orderDate");
                double totalAmount = rs.getDouble("totalAmount");
                String customerName = rs.getString("customerName");
                String customerEmail = rs.getString("customerEmail");
                String customerPhone = rs.getString("customerPhone");
                String customerAddress = rs.getString("customerAddress");
                String paymentMethod = rs.getString("paymentMethod");
                String paymentStatus = rs.getString("paymentStatus");
                
                orders.add(new OrderDTO(orderID, userID, orderDate, totalAmount, customerName, 
                                       customerEmail, customerPhone, customerAddress, 
                                       paymentMethod, paymentStatus));
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return orders;
    }
    
    public boolean updatePaymentStatus(int orderID, String paymentStatus) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "UPDATE Orders SET paymentStatus = ? WHERE orderID = ?";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, paymentStatus);
            stm.setInt(2, orderID);
            
            check = stm.executeUpdate() > 0;
        } finally {
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return check;
    }
}