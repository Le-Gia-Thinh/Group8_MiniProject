package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.dto.UserDTO;
import model.utils.DBUtils;

public class UserDAO {
    private static final String CHECK_EMAIL = "SELECT userID, fullName, password, role, phone, address, status FROM Users WHERE email = ?";
    private static final String RESET_PASSWORD = "UPDATE Users SET password=? WHERE email=?";
    private static final String CHECK_DUPLICATE = "SELECT fullName FROM Users WHERE userID = ?";

    public UserDTO checkLogin(String userID, String password) throws SQLException, ClassNotFoundException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "SELECT userID, password, fullName, email, phone, address, role, status "
                          + "FROM Users "
                          + "WHERE userID = ? AND password = ?"; 
                stm = conn.prepareStatement(sql);
                stm.setString(1, userID);
                stm.setString(2, password);
                rs = stm.executeQuery();
                
                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    String role = rs.getString("role");
                    boolean status = rs.getBoolean("status");
                    user = new UserDTO(userID, fullName, email, phone, address, role, status);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) conn.close(); // Thay DBUtils.closeConnection bằng conn.close()
        }
        
        return user;
    }
    public UserDTO getUserByID(String userID) throws SQLException, ClassNotFoundException {
    UserDTO user = null;
    String query = "SELECT * FROM Users WHERE userID = ?";
    try (Connection conn = DBUtils.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, userID);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                user = new UserDTO();
                user.setUserID(rs.getString("userID"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getBoolean("status"));
            }
        }
    }
    return user;
}

    public boolean createUser(UserDTO user) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;
        
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO Users(userID, password, fullName, email, phone, address, role, status) "
                          + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                stm = conn.prepareStatement(sql);
                stm.setString(1, user.getUserID());
                stm.setString(2, user.getPassword());
                stm.setString(3, user.getFullName());
                stm.setString(4, user.getEmail());
                stm.setString(5, user.getPhone());
                stm.setString(6, user.getAddress());
                stm.setString(7, user.getRole());
                stm.setBoolean(8, user.isStatus());
    
                check = stm.executeUpdate() > 0 ? true : false;
            }
       } catch (SQLException e) {
        System.out.println("SQLException in createUser: " + e.getMessage());
        throw e; // Ném lại ngoại lệ để MainController xử lý
    } finally {
        if (stm != null) {
            stm.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
    return check;
}
   
    public UserDTO checkEmail(String email) throws SQLException, ClassNotFoundException {
        System.out.println("checkEmail: Starting with email = " + email);
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            System.out.println("checkEmail: Getting connection...");
            conn = DBUtils.getConnection();
            System.out.println("checkEmail: Connection = " + (conn != null));
            if (conn == null) {
                throw new SQLException("Failed to get database connection");
            }
            System.out.println("checkEmail: Preparing statement...");
            ptm = conn.prepareStatement(CHECK_EMAIL);
            ptm.setString(1, email);
            System.out.println("checkEmail: Executing query...");
            rs = ptm.executeQuery();
            if (rs.next()) {
                String userID = rs.getString("userID");
                String fullName = rs.getString("fullName");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                boolean status = rs.getBoolean("status");
                user = new UserDTO(userID, password, fullName, email, phone, address, role, status);
            } else {
            }
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (Exception e) {
        } finally {
            try {
                if (rs != null) rs.close();
                if (ptm != null) ptm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw e;
            }
        }

        System.out.println("checkEmail: Returning user = " + user);
        return user;
    }

    public boolean checkDuplicate(String userID) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CHECK_DUPLICATE);
                ptm.setString(1, userID);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    check = true;
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }

    public boolean resetPassword(String password, String email) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(RESET_PASSWORD);
                ps.setString(1, password);
                ps.setString(2, email);
                check = ps.executeUpdate() > 0 ? true : false;
            }
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return check;
    }
}