package com.cga102g3.web.order_Item.dao;

import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.order_Item.entity.OrderItemVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/24
 **/
public class OrderItemDaoImpl implements OrderItemDao{
   

    @Override
    public List<OrderItemVO> getAll(Integer orderID) {
        return null;
    }

    @Override
    public void insert2(OrderItemVO orderItemVO, Connection con) {
        PreparedStatement ps = null;
        final String sql = "INSERT INTO `bookstore`.`order_item` (order_ID,prod_ID, amount, sale_price) \n" +
                "VALUES (?,?, ?, ?);";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1,orderItemVO.getOrderID());
            ps.setInt(2,orderItemVO.getProdID());
            ps.setInt(3,orderItemVO.getAmount());
            ps.setInt(4,orderItemVO.getSalePrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    // exception發生，rollback()
                    System.err.print("Transaction is being ");
                    System.err.println("rolled back-由-orderItem");
                    con.rollback();
                } catch (SQLException excep) {
                    //rollback失敗
                    throw new RuntimeException("rollback error occured. "
                            + excep.getMessage());
                }
            }
            throw new RuntimeException("A database error occured. "
                    + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getByOrderID(Integer orderID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();
        final String sql = "select b.title,b.book_ID,o.amount,o.sale_price\n" +
                "from (product p\n" +
                "join book b\n" +
                "on p.book_ID = b.book_ID)\n" +
                "join order_item o\n" +
                "on p.prod_ID = o.prod_ID\n" +
                "where o.order_ID = ?";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,orderID);
            rs = ps.executeQuery();

            while (rs.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("title",rs.getString("title"));
                map.put("bookID",rs.getInt("book_ID"));
                map.put("amount",rs.getInt("amount"));
                map.put("price",rs.getInt("sale_price"));
                list.add(map);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(con,ps,rs);
        }
        return list;
    }

    /**
     * @description: 針對後台 orderitem 的查看
     * @author: Alan
     * @date: 2022/7/4
     **/
    @Override
    public List<Map<String,Object>> findAll(int orderID) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        final String sql = "select order_ID, prod_ID, amount, sale_price\n" +
                "from order_item\n" +
                "where order_ID = ?";

        final String[] columns = {"order_ID"};

        try{
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql,columns);
            //關閉自動交易
            con.setAutoCommit(false);
            ps.setInt(1, orderID);

            rs = ps.executeQuery();
            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderID", rs.getInt("order_id"));
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("amount", rs.getInt("amount"));
                map.put("sale", rs.getInt("sale_price"));
                list.add(map);
            }
            rs.close();
        }catch (SQLException e) {
            if (con != null) {
                try {
                    System.err.print("Transaction is being ");
                    System.err.println("rolled back-dept");
                    con.rollback();
                } catch (SQLException excep) {
                    throw new RuntimeException("rollback error occured. "
                            + excep.getMessage());
                }
            }
            throw new RuntimeException("A database error occured. "
                    + e.getMessage());
        }
        return list;
    }
}
