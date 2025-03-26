package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
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
            String sql = "INSERT INTO OrderDetails(orderID, productID, quantity, price) "
                    + "VALUES(?, ?, ?, ?)";

            stm = conn.prepareStatement(sql);

            for (OrderDetailDTO detail : orderDetails) {
                stm.setInt(1, orderID);
                stm.setInt(2, detail.getProductID());
                stm.setInt(3, detail.getQuantity());
                stm.setDouble(4, detail.getPrice());

                int affectedRows = stm.executeUpdate();

                if (affectedRows <= 0) {
                    success = false;
                    break;
                }

                // Update book quantity
                ProductDAO productDAO = new ProductDAO();
                boolean updated = productDAO.updateQuantity(detail.getProductID(), detail.getQuantity());

                if (!updated) {
                    success = false;
                    break;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
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
            String sql = "SELECT orderID, userID, orderDate, totalAmount, customerName, \n"
                    + "customerEmail, customerPhone, customerAddress, paymentMethod, paymentStatus, orderStatus\n"
                    + "FROM Orders \n"
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
                String orderStatus = rs.getString("orderStatus");

                order = new OrderDTO(orderID, userID, orderDate, totalAmount, customerName,
                        customerEmail, customerPhone, customerAddress,
                        paymentMethod, paymentStatus, orderStatus);

                // Get order details
                List<OrderDetailDTO> orderDetails = getOrderDetails(orderID);
                order.setOrderDetails(orderDetails);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                DBUtils.closeConnection(conn);
            }
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
            String sql = "SELECT od.orderDetailID, od.orderID, od.productID, b.productName, od.quantity, od.price "
                    + "FROM OrderDetails od JOIN Products b ON od.productID = b.productID "
                    + "WHERE od.orderID = ?";

            stm = conn.prepareStatement(sql);
            stm.setInt(1, orderID);

            rs = stm.executeQuery();

            while (rs.next()) {
                int orderDetailID = rs.getInt("orderDetailID");
                int productID = rs.getInt("productID");
                String productName = rs.getString("productName");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                orderDetails.add(new OrderDetailDTO(orderDetailID, orderID, productID, productName, quantity, price));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                DBUtils.closeConnection(conn);
            }
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
                    + "customerEmail, customerPhone, customerAddress, paymentMethod, paymentStatus, orderStatus "
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
                String orderStatus = rs.getString("orderStatus");
                orders.add(new OrderDTO(orderID, userID, orderDate, totalAmount, customerName,
                        customerEmail, customerPhone, customerAddress,
                        paymentMethod, paymentStatus, orderStatus));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                DBUtils.closeConnection(conn);
            }
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
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                DBUtils.closeConnection(conn);
            }
        }

        return check;
    }

    public double getDailyRevenue(Date date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(totalAmount) FROM Orders WHERE CAST(orderDate AS DATE) = ? AND paymentStatus = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date.getTime()));
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } finally {
            closeResources(conn, ps, rs);
        }
        return 0;
    }

    public double getMonthlyRevenue(Date date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(totalAmount) FROM Orders WHERE YEAR(orderDate) = YEAR(?) AND MONTH(orderDate) = MONTH(?) AND paymentStatus = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date.getTime()));
            ps.setDate(2, new java.sql.Date(date.getTime()));
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } finally {
            closeResources(conn, ps, rs);
        }
        return 0;
    }

    public double getYearlyRevenue(Date date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(totalAmount) FROM Orders WHERE YEAR(orderDate) = YEAR(?) AND paymentStatus = 'COMPLETED'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date.getTime()));
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } finally {
            closeResources(conn, ps, rs);
        }
        return 0;
    }

    public Map<String, Double> getWeeklyRevenue(Date date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT CAST(orderDate AS DATE) AS RevenueDate, SUM(totalAmount) FROM Orders WHERE orderDate >= DATEADD(DAY, -6, ?) AND paymentStatus = 'COMPLETED' GROUP BY CAST(orderDate AS DATE) ORDER BY RevenueDate";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, Double> revenueMap = new LinkedHashMap<>();
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(date.getTime()));
            rs = ps.executeQuery();
            while (rs.next()) {
                revenueMap.put(rs.getString(1), rs.getDouble(2));
            }
        } finally {
            closeResources(conn, ps, rs);
        }
        return revenueMap;
    }

    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT orderID, userID, orderDate, totalAmount, customerName, "
                    + "customerEmail, customerPhone, customerAddress, paymentMethod, paymentStatus, orderStatus "
                    + "FROM Orders "
                    + "ORDER BY orderDate DESC";
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                String userID = rs.getString("userID");
                Date orderDate = rs.getTimestamp("orderDate");
                double totalAmount = rs.getDouble("totalAmount");
                String customerName = rs.getString("customerName");
                String customerEmail = rs.getString("customerEmail");
                String customerPhone = rs.getString("customerPhone");
                String customerAddress = rs.getString("customerAddress");
                String paymentMethod = rs.getString("paymentMethod");
                String paymentStatus = rs.getString("paymentStatus");
                String orderStatus = rs.getString("orderStatus");

                OrderDTO order = new OrderDTO(orderID, userID, orderDate, totalAmount, customerName,
                        customerEmail, customerPhone, customerAddress,
                        paymentMethod, paymentStatus, orderStatus);

                // Lấy chi tiết đơn hàng
                order.setOrderDetails(getOrderDetails(orderID));

                orders.add(order);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                DBUtils.closeConnection(conn);
            }
        }
        return orders;
    }

    
}
