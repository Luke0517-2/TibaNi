package com.cga102g3.core.util;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * @Description JDBC連接工具類
 * @Author Robert
 * @Version
 * @Date 2022-06-05 下午 04:42
 */
public class JDBCUtil {
    private static DataSource ds;
    private static final Boolean  isConnectionPool = false;

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("jdbc");

    private static final String DRIVERCLASS = BUNDLE.getString("driver");
    private static final String URL = BUNDLE.getString("url");
    private static final String USER = BUNDLE.getString("user");
    private static final String PASSWORD = BUNDLE.getString("password");

    static {
        if (isConnectionPool) {
            // 連線池
            Context ctx = null;
            try {
                ctx = new javax.naming.InitialContext();
                ds = (DataSource) ctx.lookup("java:comp/env/jdbc/bookstore");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        } else {
            // jdbc版
            // 註冊驅動
            try{
                Class.forName(JDBCUtil.DRIVERCLASS);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private JDBCUtil(){}
    /**
     * 獲取數據庫連接物件
     * @return conn 數據庫連接物件
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (isConnectionPool) {
            // 連線池版
            Connection conn = ds.getConnection();
            return conn;
        }
        // jdbc版
        return DriverManager.getConnection(JDBCUtil.URL, JDBCUtil.USER, JDBCUtil.PASSWORD);
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
