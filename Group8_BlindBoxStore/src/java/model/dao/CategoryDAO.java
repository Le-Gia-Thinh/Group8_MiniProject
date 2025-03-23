package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dto.CategoryDTO;
import model.utils.DBUtils;

public class CategoryDAO {
    
    public List<CategoryDTO> getAllCategories() throws SQLException, ClassNotFoundException {
        List<CategoryDTO> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT categoryID, categoryName, description FROM Categories";
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                int categoryID = rs.getInt("categoryID");
                String categoryName = rs.getString("categoryName");
                String description = rs.getString("description");
                
                categories.add(new CategoryDTO(categoryID, categoryName, description));
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return categories;
    }
    
    public CategoryDTO getCategoryByID(int categoryID) throws SQLException, ClassNotFoundException {
        CategoryDTO category = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT categoryID, categoryName, description FROM Categories WHERE categoryID = ?";
            stm = conn.prepareStatement(sql);
            stm.setInt(1, categoryID);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                String categoryName = rs.getString("categoryName");
                String description = rs.getString("description");
                
                category = new CategoryDTO(categoryID, categoryName, description);
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return category;
    }
}