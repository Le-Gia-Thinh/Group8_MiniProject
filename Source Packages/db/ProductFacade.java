/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NGUYEN QUOC BAO
 */
public class ProductFacade {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    public List<Product> getAllProduct() throws SQLException {
        List<Product> list = new ArrayList<>();
        
        String query = "select * from Products";
        try{
            conn = DBContext.getConnection();
            if(conn != null) {
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
                while(rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setProductName(rs.getString("productName"));
                    product.setCategoryID(rs.getInt("categoryID"));
                    product.setPrice(rs.getFloat("price"));
                    product.setDiscount(rs.getFloat("discount"));
                    product.setStockQuantity(rs.getInt("stockQuantity"));
                    product.setSize(rs.getString("size"));
                    product.setMaterial(rs.getString("material"));
                    product.setDescription(rs.getString("description"));
                    product.setImageURL(rs.getString("imageURl"));
                    product.setCreatedAt(rs.getDate("createdAt"));
                    product.setUpdatedAt(rs.getDate("updatedAt"));
                    list.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        conn.close();
        return list;
    }
    
     public Product read(int id) throws SQLException{
        Product product = null;
        //Tạo kết nối db
        Connection con = DBContext.getConnection();
        //Tạo đối tượng Statement
        PreparedStatement stm = con.prepareStatement("select * from Products where productID=?");        
        stm.setInt(1, id);
        //Thực thi lệnh select
        ResultSet rs = stm.executeQuery();        
        while(rs.next()){
            //Đọc từng dòng trong table Brand để vào đối tượng product
            product = new Product();
            product.setProductID(rs.getInt("productID"));
            product.setProductName(rs.getString("productName"));
            product.setCategoryID(rs.getInt("categoryID"));
            product.setPrice(rs.getFloat("price"));
            product.setDiscount(rs.getFloat("discount"));
            product.setStockQuantity(rs.getInt("stockQuantity"));
            product.setSize(rs.getString("size"));
            product.setMaterial(rs.getString("material"));
            product.setDescription(rs.getString("description"));
            product.setImageURL(rs.getString("imageURl"));
            product.setCreatedAt(rs.getDate("createdAt"));
            product.setUpdatedAt(rs.getDate("updatedAt"));
        }
        //Đóng kết nối db
        con.close();
        return product;
    }
     
    public int count() throws SQLException{
        int row_count = 0;
        //Tạo kết nối db
        Connection con = DBContext.getConnection();
        //Tạo đối tượng Statement
        Statement stm = con.createStatement();
        //Thực thi lệnh select
        ResultSet rs = stm.executeQuery("select count(*) as row_count from Products");
        if(rs.next()){
            row_count = rs.getInt("row_count");
        }
        //Đóng kết nối db
        con.close();
        return row_count;
    }
}
