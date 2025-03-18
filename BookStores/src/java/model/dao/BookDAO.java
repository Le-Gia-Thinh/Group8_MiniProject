package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.dto.BookDTO;
import model.utils.DBUtils;

public class BookDAO {
    
    public List<BookDTO> searchBooks(String searchValue, Integer categoryID, int page, int booksPerPage) 
            throws SQLException, ClassNotFoundException {
        List<BookDTO> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT b.bookID, b.title, b.author, b.description, b.price, b.quantity, "
                       + "b.imageUrl, b.categoryID, c.categoryName, b.createDate, b.lastUpdateDate, "
                       + "b.lastUpdateUser, b.status "
                       + "FROM Books b JOIN Categories c ON b.categoryID = c.categoryID "
                       + "WHERE b.status = 1 AND b.quantity > 0 ";
            
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                sql += "AND b.title LIKE ? ";
            }
            
            if (categoryID != null) {
                sql += "AND b.categoryID = ? ";
            }
            
            sql += "ORDER BY b.createDate DESC "
                 + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            
            stm = conn.prepareStatement(sql);
            
            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                stm.setString(paramIndex++, "%" + searchValue + "%");
            }
            
            if (categoryID != null) {
                stm.setInt(paramIndex++, categoryID);
            }
            
            stm.setInt(paramIndex++, (page - 1) * booksPerPage);
            stm.setInt(paramIndex, booksPerPage);
            
            rs = stm.executeQuery();
            
            while (rs.next()) {
                int bookID = rs.getInt("bookID");
                String title = rs.getString("title");
                String author = rs.getString("author");
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
                
                books.add(new BookDTO(bookID, title, author, description, price, quantity, 
                                     imageUrl, catID, categoryName, createDate, 
                                     lastUpdateDate, lastUpdateUser, status));
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return books;
    }
    
    public int countBooks(String searchValue, Integer categoryID) 
            throws SQLException, ClassNotFoundException {
        int count = 0;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT COUNT(*) as total "
                       + "FROM Books b "
                       + "WHERE b.status = 1 AND b.quantity > 0 ";
            
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                sql += "AND b.title LIKE ? ";
            }
            
            if (categoryID != null) {
                sql += "AND b.categoryID = ? ";
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
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return count;
    }
    
    public BookDTO getBookByID(int bookID) throws SQLException, ClassNotFoundException {
        BookDTO book = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT b.bookID, b.title, b.author, b.description, b.price, b.quantity, "
                       + "b.imageUrl, b.categoryID, c.categoryName, b.createDate, b.lastUpdateDate, "
                       + "b.lastUpdateUser, b.status "
                       + "FROM Books b JOIN Categories c ON b.categoryID = c.categoryID "
                       + "WHERE b.bookID = ?";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, bookID);
            
            rs = stm.executeQuery();
            
            if (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
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
                
                book = new BookDTO(bookID, title, author, description, price, quantity, 
                                  imageUrl, catID, categoryName, createDate, 
                                  lastUpdateDate, lastUpdateUser, status);
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return book;
    }
    
    public boolean updateBook(BookDTO book) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "UPDATE Books "
                       + "SET title = ?, author = ?, description = ?, price = ?, "
                       + "quantity = ?, imageUrl = ?, categoryID = ?, "
                       + "lastUpdateDate = GETDATE(), lastUpdateUser = ?, status = ? "
                       + "WHERE bookID = ?";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, book.getTitle());
            stm.setString(2, book.getAuthor());
            stm.setString(3, book.getDescription());
            stm.setDouble(4, book.getPrice());
            stm.setInt(5, book.getQuantity());
            stm.setString(6, book.getImageUrl());
            stm.setInt(7, book.getCategoryID());
            stm.setString(8, book.getLastUpdateUser());
            stm.setBoolean(9, book.isStatus());
            stm.setInt(10, book.getBookID());
            
            check = stm.executeUpdate() > 0;
        } finally {
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return check;
    }
    
    public boolean createBook(BookDTO book) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "INSERT INTO Books(title, author, description, price, quantity, "
                       + "imageUrl, categoryID, createDate, lastUpdateDate, lastUpdateUser, status) "
                       + "VALUES(?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE(), ?, ?)";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, book.getTitle());
            stm.setString(2, book.getAuthor());
            stm.setString(3, book.getDescription());
            stm.setDouble(4, book.getPrice());
            stm.setInt(5, book.getQuantity());
            stm.setString(6, book.getImageUrl());
            stm.setInt(7, book.getCategoryID());
            stm.setString(8, book.getLastUpdateUser());
            stm.setBoolean(9, book.isStatus());
            
            check = stm.executeUpdate() > 0;
        } finally {
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return check;
    }
    
    public boolean updateQuantity(int bookID, int quantity) throws SQLException, ClassNotFoundException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement stm = null;
        
        try {
            conn = DBUtils.getConnection();
            String sql = "UPDATE Books SET quantity = quantity - ? WHERE bookID = ? AND quantity >= ?";
            
            stm = conn.prepareStatement(sql);
            stm.setInt(1, quantity);
            stm.setInt(2, bookID);
            stm.setInt(3, quantity);
            
            check = stm.executeUpdate() > 0;
        } finally {
            if (stm != null) stm.close();
            if (conn != null) DBUtils.closeConnection(conn);
        }
        
        return check;
    }
    public List<BookDTO> getAllBooksForAdmin(int page, int booksPerPage) 
        throws SQLException, ClassNotFoundException {
    List<BookDTO> books = new ArrayList<>();
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    
    try {
        conn = DBUtils.getConnection();
        String sql = "SELECT b.bookID, b.title, b.author, b.description, b.price, b.quantity, "
                   + "b.imageUrl, b.categoryID, c.categoryName, b.createDate, b.lastUpdateDate, "
                   + "b.lastUpdateUser, b.status "
                   + "FROM Books b JOIN Categories c ON b.categoryID = c.categoryID "
                   + "ORDER BY b.createDate DESC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        
        stm = conn.prepareStatement(sql);
        stm.setInt(1, (page - 1) * booksPerPage);
        stm.setInt(2, booksPerPage);
        
        rs = stm.executeQuery();
        
        while (rs.next()) {
            int bookID = rs.getInt("bookID");
            String title = rs.getString("title");
            String author = rs.getString("author");
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
            
            books.add(new BookDTO(bookID, title, author, description, price, quantity, 
                                 imageUrl, catID, categoryName, createDate, 
                                 lastUpdateDate, lastUpdateUser, status));
        }
    } finally {
        if (rs != null) rs.close();
        if (stm != null) stm.close();
        if (conn != null) DBUtils.closeConnection(conn);
    }
    
    return books;
}

public int countAllBooksForAdmin() 
        throws SQLException, ClassNotFoundException {
    int count = 0;
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    
    try {
        conn = DBUtils.getConnection();
        String sql = "SELECT COUNT(*) as total FROM Books";
        
        stm = conn.prepareStatement(sql);
        rs = stm.executeQuery();
        
        if (rs.next()) {
            count = rs.getInt("total");
        }
    } finally {
        if (rs != null) rs.close();
        if (stm != null) stm.close();
        if (conn != null) DBUtils.closeConnection(conn);
    }
    
    return count;
}
}