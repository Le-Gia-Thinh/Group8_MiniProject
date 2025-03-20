package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.dto.UserDTO;
import model.dto.UserGoogleDTO;
import model.utils.DBUtils;

public class UserDAO {
    
    public UserDTO checkLogin(String userID, String password) throws SQLException, ClassNotFoundException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT userID, password, fullName, email, phone, address, role, status "
                       + "FROM Users "
                       + "WHERE userID = ? AND password = ? AND status = 1";
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
                
                user = new UserDTO(userID, "", fullName, email, phone, address, role, status);
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return user;
    }
    
    public boolean createUser(UserDTO user) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;
        
        try {
            conn = DBUtils.getConnection();
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
            
            check = stm.executeUpdate() > 0;
        } finally {
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return check;
    }
    public UserGoogleDTO checkGoogleLogin(String googleId) throws SQLException, ClassNotFoundException {
        UserGoogleDTO googleUser = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT id, email, name, given_name, family_name, picture, verified_email "
                       + "FROM UsersGoogle "
                       + "WHERE id = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1, googleId);
            rs = stm.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");
                String givenName = rs.getString("given_name");
                String familyName = rs.getString("family_name");
                String picture = rs.getString("picture");
                boolean verifiedEmail = rs.getBoolean("verified_email");

                googleUser = new UserGoogleDTO(googleId, email, verifiedEmail, name, givenName, familyName, picture);
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }

        return googleUser;
    }

    public boolean createGoogleUser(UserGoogleDTO googleUser) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "INSERT INTO UsersGoogle(id, email, verified_email, name, given_name, family_name, picture) "
                       + "VALUES(?, ?, ?, ?, ?, ?, ?)";
            stm = conn.prepareStatement(sql);
            stm.setString(1, googleUser.getId());
            stm.setString(2, googleUser.getEmail());
            stm.setBoolean(3, googleUser.isVerified_email());
            stm.setString(4, googleUser.getName());
            stm.setString(5, googleUser.getGiven_name());
            stm.setString(6, googleUser.getFamily_name());
            stm.setString(7, googleUser.getPicture());

            check = stm.executeUpdate() > 0;
        } finally {
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }

        return check;
    }

}