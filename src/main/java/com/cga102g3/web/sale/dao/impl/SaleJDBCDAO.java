package com.cga102g3.web.sale.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.category.entity.Category;
import com.cga102g3.web.prod_sale.dao.impl.ProdSaleJDBCDAO;
import com.cga102g3.web.prod_sale.entity.ProdSaleVO;
import com.cga102g3.web.sale.dao.SaleDAOInterface;
import com.cga102g3.web.sale.entity.SaleVO;

public class SaleJDBCDAO implements SaleDAOInterface {
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
    String userid = "root";
    String passwd = "abc123";

    private static final String INSERT_STMT =
            "INSERT INTO sale (sale_start, sale_end) VALUES (?, ?)";
    private static final String GET_ALL_STMT =
            "SELECT sale_ID, sale_start, sale_end FROM sale order by sale_ID";
    private static final String GET_ONE_STMT =
            "SELECT sale_ID, sale_start, sale_end FROM sale where sale_ID = ?";
    private static final String DELETE =
            "DELETE FROM sale where sale_ID = ?";
    private static final String UPDATE =
            "UPDATE sale set sale_start=?, sale_end=? where sale_ID = ?";


    @Override
    public void insert(SaleVO saleVO) {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(INSERT_STMT);

            pstmt.setDate(1, saleVO.getSaleStart());
            pstmt.setDate(2, saleVO.getSaleEnd());

            pstmt.executeUpdate();

            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. "
                    + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
            // Clean up JDBC resources
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }


    @Override
    public void update(SaleVO saleVO) {

        Connection con = null;
        PreparedStatement pstmt = null;

        try {

            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(UPDATE);

            pstmt.setDate(1, saleVO.getSaleStart());
            pstmt.setDate(2, saleVO.getSaleEnd());
            pstmt.setInt(3, saleVO.getSaleID());

            pstmt.executeUpdate();

            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. "
                    + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
            // Clean up JDBC resources
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }


    @Override
    public boolean delete(Integer sale_ID) {
        boolean ans = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        int row = 0;

        try {

            con = JDBCUtil.getConnection();
            pstmt = con.prepareStatement(DELETE);

            pstmt.setInt(1, sale_ID);

            row = pstmt.executeUpdate();
            if (row != 0) ans = true;

            // Handle any driver errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
            // Clean up JDBC resources
        } finally {
            JDBCUtil.close(con, pstmt, null);
        }
        return ans;
    }


    @Override
    public SaleVO findByPrimaryKey(Integer sale_ID) {

        SaleVO saleVO = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(GET_ONE_STMT);

            pstmt.setInt(1, sale_ID);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                // saleVO �]�٬� Domain objects
                saleVO = new SaleVO();
                saleVO.setSaleID(rs.getInt("sale_ID"));
                saleVO.setSaleStart(rs.getDate("sale_start"));
                saleVO.setSaleEnd(rs.getDate("sale_end"));
            }

            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. "
                    + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
            // Clean up JDBC resources
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return saleVO;
    }


    @Override
    public List<SaleVO> getAll() {
        List<SaleVO> list = new ArrayList<SaleVO>();
        SaleVO saleVO = null;

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(GET_ALL_STMT);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // saleVO �]�٬� Domain objects
                saleVO = new SaleVO();
                saleVO.setSaleID(rs.getInt("sale_ID"));
                saleVO.setSaleStart(rs.getDate("sale_start"));
                saleVO.setSaleEnd(rs.getDate("sale_end"));
                list.add(saleVO); // Store the row in the list
            }

            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. "
                    + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
            // Clean up JDBC resources
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return list;
    }

    /**
     * @description: 同時新增促銷專案及產品
     * @param: [saleVO, con]
     * @return: void
     * @auther: Luke
     * @date: 2022/06/29 10:44:04
     */
    @Override
    public boolean insertWithProdSale(SaleVO saleVO, List<ProdSaleVO> list) {
        boolean x = true;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int saleID = -1;
        final String sql = "INSERT INTO sale (sale_start, sale_end) VALUES (?, ?)";
        final String[] columns = {"sale_ID"};

        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql, columns);
            con.setAutoCommit(false);

            ps.setDate(1, saleVO.getSaleStart());
            ps.setDate(2, saleVO.getSaleEnd());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                saleID = rs.getInt(1);
            }
            rs.close();
            System.out.println("11");
            ProdSaleJDBCDAO dao = new ProdSaleJDBCDAO();
            for (ProdSaleVO vo : list) {
                System.out.println("22");
                vo.setSaleID(saleID);
                dao.insert2(vo, con);
            }
            System.out.println("33");
            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException se) {
            x = false;
            if (con != null) {
                try {
                    // 3●設定於當有exception發生時之catch區塊內
                    System.err.print("Transaction is being ");
                    System.err.println("rolled back-由-Sale");
                    con.rollback();
                } catch (SQLException excep) {
                    throw new RuntimeException("rollback error occured. "
                            + excep.getMessage());
                }
            }
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
            // Clean up JDBC resources
        } finally {
            JDBCUtil.close(con, ps, rs);
        }
        return x;
    }

    /**
     * @description:判斷起始時間是否存在專案
     * @param: [saleStart]
     * @return: boolean true可以新增 false區間已存在
     * @auther: Luke
     * @date: 2022/06/29 14:03:56
     */
    @Override
    public boolean judge1(Date saleStart) {
        boolean x = true;   //true可以新增 false區間已存在
        final String sql = "select *\n" +
                "from sale\n" +
                "where ? between sale_start and sale_end;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDate(1, saleStart);
            rs = ps.executeQuery();
            if (rs.next()) x = false;   //集合有值，表示時間點有專案

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(con, ps, rs);
        }
        return x;
    }

    /**
     * @description:判斷區間是否存在專案
     * @param: [saleStart, saleEnd]
     * @return: boolean true可以新增 false區間已存在
     * @auther: Luke
     * @date: 2022/06/29 14:03:56
     */
    @Override
    public boolean judge2(Date saleStart, Date saleEnd) {
        boolean x = true;  //true可以新增 false區間已存在
        String sql = "select *\n" +
                "from sale\n" +
                "where sale_start between ?\n" +
                "and ?  or sale_end between ? and ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDate(1, saleStart);
            ps.setDate(2, saleEnd);
            ps.setDate(3, saleStart);
            ps.setDate(4, saleEnd);
            rs = ps.executeQuery();
            if (rs.next()) x = false;  //集合有值，表示時間點有專案
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(con, ps, rs);
        }
        return x;
    }

    /**
     * @description: 抓取現有產品中有的種類
     * @param: []
     * @return: java.util.List<com.cga102g3.web.category.entity.Category>
     * @auther: Luke
     * @date: 2022/07/13 12:57:47
     */
    @Override
    public List<Category> getProdCategory() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Category> list = new ArrayList<>();
        final String sql = "select distinct b.category_name  \n" +
                "from product p\n" +
                "join book b\n" +
                "on p.book_ID = b.book_ID";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Category vo = new Category();
                vo.setCategoryName(rs.getString("category_name"));
                list.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(con, ps, rs);
        }
        return list;
    }

}
