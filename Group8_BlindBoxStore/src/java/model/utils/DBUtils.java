package model.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static final String SERVER_NAME = "localhost";
    private static final String DB_NAME = "BlindBox";
    private static final String PORT_NUMBER = "1433";
    private static final String INSTANCE_NAME = "SQLEXPRESS"; 
    private static final String USER_ID = "sa"; //
    private static final String PASSWORD = "12345"; 

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        String url = "jdbc:sqlserver://" + SERVER_NAME + "\\" + INSTANCE_NAME + 
                     ":" + PORT_NUMBER + ";databaseName=" + DB_NAME + 
                     ";encrypt=true;trustServerCertificate=true;";
        
        conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
        
        return conn;
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}