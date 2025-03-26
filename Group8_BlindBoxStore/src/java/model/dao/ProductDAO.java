package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.dto.ProductDTO;
import model.utils.DBUtils;

public class ProductDAO {

    public List<ProductDTO> searchProducts(String searchValue, Integer categoryID, int page, int productPerPage, String priceSort)
            throws SQLException, ClassNotFoundException {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT p.productID, p.productName, p.series, p.description, p.price, p.quantity, "
                       + "p.imageUrl, p.categoryID, c.categoryName, p.createDate, p.lastUpdateDate, "
                       + "p.lastUpdateUser, p.status "
                       + "FROM Products p JOIN Categories c ON p.categoryID = c.categoryID "
                       + "WHERE p.status = 1 AND p.quantity > 0 ";
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                sql += "AND p.productName LIKE ? ";
            }
            if (categoryID != null) {
                sql += "AND p.categoryID = ? ";
            }

            if ("asc".equalsIgnoreCase(priceSort)) {
                sql += "ORDER BY p.price ASC ";
            } else if ("desc".equalsIgnoreCase(priceSort)) {
                sql += "ORDER BY p.price DESC ";
            } else {
                sql += "ORDER BY p.createDate DESC ";
            }

            sql += "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            stm = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                stm.setString(paramIndex++, "%" + searchValue + "%");
            }
            if (categoryID != null) {
                stm.setInt(paramIndex++, categoryID);
            }
            stm.setInt(paramIndex++, (page - 1) * productPerPage);
            stm.setInt(paramIndex, productPerPage);
            rs = stm.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                String series = rs.getString("series");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String imageUrl = rs.getString("imageUrl");
                int catID = rs.getInt("categoryID");
                String categoryName = rs.getString("categoryName");
                Date createDate = rs.getTimestamp("createDate");
                Date lastUpdateDate = rs.getTimestamp("lastUpdateDate");
                String lastUpdateUser = rs.getString("lastUpdateUser");
                boolean status = rs.getBoolean("status");
                products.add(new ProductDTO(productId, productName, series, description, price, quantity,
                        imageUrl, catID, categoryName, createDate, lastUpdateDate, lastUpdateUser, status));
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        return products;
    }

    public int countProducts(String searchValue, Integer categoryID)
            throws SQLException, ClassNotFoundException {
        int count = 0;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT COUNT(*) as total "
                    + "FROM Products p "
                    + "WHERE p.status = 1 AND p.quantity > 0 ";

            if (searchValue != null && !searchValue.trim().isEmpty()) {
                sql += "AND p.productName LIKE ? ";
            }

            if (categoryID != null) {
                sql += "AND p.categoryID = ? ";
            }

            stm = conn.prepareStatement(sql);

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                stm.setString(paramIndex++, "%" + searchValue + "%");
            }

            if (categoryID != null) {
                stm.setInt(paramIndex++, categoryID);
            }

            rs = stm.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
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

        return count;
    }

    public ProductDTO getProductID(int productID) throws SQLException, ClassNotFoundException {
        ProductDTO product = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT p.productID, p.productName, p.series, p.description, p.price, p.quantity, "
                    + "p.imageUrl, p.categoryID, c.categoryName, p.createDate, p.lastUpdateDate, "
                    + "p.lastUpdateUser, p.status "
                    + "FROM Products p "
                    + "JOIN Categories c ON p.categoryID = c.categoryID "
                    + "WHERE p.productID = ?";

            stm = conn.prepareStatement(sql);
            stm.setInt(1, productID);

            rs = stm.executeQuery();

            if (rs.next()) {
                String productName = rs.getString("productName");
                String series = rs.getString("series");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String imageUrl = rs.getString("imageUrl");
                int catID = rs.getInt("categoryID");
                String categoryName = rs.getString("categoryName");
                Date createDate = rs.getTimestamp("createDate");
                Date lastUpdateDate = rs.getTimestamp("lastUpdateDate");
                String lastUpdateUser = rs.getString("lastUpdateUser");
                boolean status = rs.getBoolean("status");

                product = new ProductDTO(productID, productName, series, description, price, quantity,
                        imageUrl, catID, categoryName, createDate,
                        lastUpdateDate, lastUpdateUser, status);
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

        return product;
    }

    public boolean updateProduct(ProductDTO product) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "UPDATE Products "
                    + "SET productName = ?, series = ?, description = ?, price = ?, "
                    + "quantity = ?, imageUrl = ?, categoryID = ?, "
                    + "lastUpdateDate = GETDATE(), lastUpdateUser = ?, status = ? "
                    + "WHERE productID = ?";

            stm = conn.prepareStatement(sql);
            stm.setString(1, product.getProductName());
            stm.setString(2, product.getSeries());
            stm.setString(3, product.getDescription());
            stm.setDouble(4, product.getPrice());
            stm.setInt(5, product.getQuantity());
            stm.setString(6, product.getImageUrl());
            stm.setInt(7, product.getCategoryID());
            stm.setString(8, product.getLastUpdateUser());
            stm.setBoolean(9, product.isStatus());
            stm.setInt(10, product.getProductID());

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

    public boolean createProduct(ProductDTO product) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "INSERT INTO Products(productName, series, description, price, quantity, "
                    + "imageUrl, categoryID, createDate, lastUpdateDate, lastUpdateUser, status) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE(), ?, ?)";

            stm = conn.prepareStatement(sql);
            stm.setString(1, product.getProductName());
            stm.setString(2, product.getSeries());
            stm.setString(3, product.getDescription());
            stm.setDouble(4, product.getPrice());
            stm.setInt(5, product.getQuantity());
            stm.setString(6, product.getImageUrl());
            stm.setInt(7, product.getCategoryID());
            stm.setString(8, product.getLastUpdateUser());
            stm.setBoolean(9, product.isStatus());

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

    public boolean updateQuantity(int productID, int quantity) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "UPDATE Products SET quantity = quantity - ? WHERE productID = ? AND quantity >= ?";

            stm = conn.prepareStatement(sql);
            stm.setInt(1, quantity);
            stm.setInt(2, productID);
            stm.setInt(3, quantity);

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

    public List<ProductDTO> getAllProductsForAdmin(int page, int productsPerPage)
            throws SQLException, ClassNotFoundException {
        List<ProductDTO> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT p.productID, p.productName, p.series, p.description, p.price, p.quantity, "
                    + "p.imageUrl, p.categoryID, c.categoryName, p.createDate, p.lastUpdateDate, "
                    + "p.lastUpdateUser, p.status "
                    + "FROM Products p JOIN Categories c ON p.categoryID = c.categoryID "
                    + "ORDER BY p.createDate DESC "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            stm = conn.prepareStatement(sql);
            stm.setInt(1, (page - 1) * productsPerPage);
            stm.setInt(2, productsPerPage);

            rs = stm.executeQuery();

            while (rs.next()) {
                int productID = rs.getInt("productID");
                String productName = rs.getString("productName");
                String series = rs.getString("series");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String imageUrl = rs.getString("imageUrl");
                int catID = rs.getInt("categoryID");
                String categoryName = rs.getString("categoryName");
                Date createDate = rs.getTimestamp("createDate");
                Date lastUpdateDate = rs.getTimestamp("lastUpdateDate");
                String lastUpdateUser = rs.getString("lastUpdateUser");
                boolean status = rs.getBoolean("status");

                products.add(new ProductDTO(productID, productName, series, description, price, quantity,
                        imageUrl, catID, categoryName, createDate,
                        lastUpdateDate, lastUpdateUser, status));
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

        return products;
    }

    public int countAllProductsForAdmin() throws SQLException, ClassNotFoundException {
        int count = 0;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT COUNT(*) as total FROM Products";

            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
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

        return count;
    }
}
