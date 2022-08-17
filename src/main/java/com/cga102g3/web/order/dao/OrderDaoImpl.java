package com.cga102g3.web.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.bid_activ.dao.MemberDao;
import com.cga102g3.web.bid_activ.dao.impl.MemberDaoImpl;
import com.cga102g3.web.order.entity.OrderVO;
import com.cga102g3.web.order_Item.dao.OrderItemDaoImpl;
import com.cga102g3.web.order_Item.entity.OrderItemVO;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/24
 **/
public class OrderDaoImpl implements OrderDao {


    @Override
    public void insertWithOrderItem(OrderVO orderVO, List<OrderItemVO> list) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
//        int rowCount = 0;  //影響資料筆數
        int orderID = -1;  //自增主鍵

        final String sql = "INSERT INTO `bookstore`.`order` (`mbr_ID`, `total_price`, `order_status`, `ship_status`, `pay_status`, `pay_method`) \n" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        final String[] columns = {"order_ID"};

        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql, columns);
            //關閉自動交易
            con.setAutoCommit(false);

            ps.setInt(1, orderVO.getMbrID());
            ps.setInt(2, orderVO.getTotalPrice());
            ps.setInt(3, orderVO.getOrderStatus());
            ps.setInt(4, orderVO.getShipStatus());
            ps.setInt(5, orderVO.getPayStatus());
            ps.setInt(6, orderVO.getPayMethod());
            ps.executeUpdate();

            if (orderVO.getPayMethod() == 1){
                MemberDao dao = new MemberDaoImpl();
                dao.prepaid4Bid(con,orderVO.getMbrID(),orderVO.getTotalPrice());
            }
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                orderID = rs.getInt(1);
            }
            rs.close();
            OrderItemDaoImpl dao = new OrderItemDaoImpl();
            for (OrderItemVO aItem : list) {
                aItem.setOrderID(orderID);
                dao.insert2(aItem, con);
            }
            con.commit();
            con.setAutoCommit(true);

        } catch (SQLException e) {
            if (con != null) {
                try {
                    System.err.print("Transaction is being ");
                    System.err.println("rolled back-由-dept");
                    con.rollback();
                } catch (SQLException excep) {
                    throw new RuntimeException("rollback error occured. "
                            + excep.getMessage());
                }
            }
            throw new RuntimeException("A database error occured. "
                    + e.getMessage());
        } finally {
            JDBCUtil.close(con, ps, rs);
        }
    }

   

    @Override
    public OrderVO findByPrimaryKey(Integer orderID) {
        return null;
    }

    @Override
    public List<OrderVO> getAll(Integer mbrID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrderVO> list = new ArrayList<>();
        final String sql = "select * from `order` where mbr_ID=?";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mbrID);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderVO vo = new OrderVO();
                vo.setOrderID(rs.getInt("order_ID"));
                vo.setOrderDate(rs.getTimestamp("order_date"));
                vo.setTotalPrice(rs.getInt("total_price"));
                vo.setPayMethod(rs.getInt("pay_method"));
                vo.setOrderStatus(rs.getInt("order_status"));
                vo.setShipStatus(rs.getInt("ship_status"));
                list.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(con, ps, rs);
        }
        return list;
    }


    /**
     * @description: 針對後台 order 的查看
     * @author: Alan
     * @date: 2022/6/29
     **/
    @Override
    public List<Map<String, Object>> findAll() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        final String sql = "select order_ID,o.mbr_ID,order_date,total_price,order_status,ship_status,pay_status,pay_method \n" +
                "from `order` o\n" +
                "join member m\n" +
                "on o.mbr_ID = m.mbr_ID";
        final String[] columns = {"order_ID"};

        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql, columns);
            //關閉自動交易
            con.setAutoCommit(false);

            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderID", rs.getInt("order_id"));
                map.put("mbrID", rs.getInt("mbr_ID"));
                map.put("orderDate", rs.getTimestamp("order_date"));
                map.put("totalPrice", rs.getInt("total_price"));
                map.put("orderStatus", rs.getInt("order_status"));
                map.put("shipStatus", rs.getInt("ship_status"));
                map.put("payStatus", rs.getInt("pay_status"));
                map.put("payMethod", rs.getInt("pay_method"));
                list.add(map);
            }
            rs.close();
        } catch (SQLException e) {
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


    public OrderVO findOneOrder(int orderID) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrderVO order = null;
        
        final String sql = "select o.mbr_id,o.order_date,o.total_price,o.order_status,o.ship_status,o.pay_status,o.pay_method,oi.prod_id,oi.amount,oi.sale_price \n"
                + "from `order` o \n"
                + "join order_item oi \n"
                + "on o.order_id = oi.order_id \n"
                + "where o.order_id = ?;";
        final String[] columns = {"order_ID"};

        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql, columns);
            //關閉自動交易
            con.setAutoCommit(false);

            ps.setInt(1, orderID);
            rs = ps.executeQuery();

            if(rs.next()) {
            order = new OrderVO();
            OrderItemVO orderitem = new OrderItemVO();
            order.setMbrID(rs.getInt("mbr_id"));
            order.setOrderDate(rs.getTimestamp("order_date"));
            order.setTotalPrice(rs.getInt("total_price"));
            order.setOrderStatus(rs.getInt("order_status"));
            order.setShipStatus(rs.getInt("ship_status"));
            order.setPayStatus(rs.getInt("pay_status"));
            order.setPayMethod(rs.getInt("pay_method"));

            orderitem.setOrderVO(order);
            orderitem.setProdID(rs.getInt("prod_id"));
            orderitem.setAmount(rs.getInt("amount"));
            orderitem.setSalePrice(rs.getInt("sale_price"));
            }
            rs.close();
            return order;
        } catch (SQLException e) {
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
    }

    /**
     * @description: 會員取消訂單，true表示成功 false失敗
     * @param:
     * @return:
     * @auther: Luke
     * @date: 2022/07/05 09:30:47
     */
    @Override
    public boolean cancel(Integer orderID) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ans = false;
        int row = 0;
        final String sql = "UPDATE `bookstore`.`order` SET `order_status` = '3', `ship_status` = '3' WHERE (`order_ID` = ?);";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,orderID);
            row = ps.executeUpdate();
            if (row == 1) ans = true;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(con,ps,null);
        }
        return ans;
    }
    /**
     * @description: 會員完成訂單，true成功，false失敗
     * @param: [orderID]
     * @return: boolean
     * @auther: Luke
     * @date: 2022/07/05 09:31:31
     */
    @Override
    public boolean finish(Integer orderID) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ans = false;
        int row = 0;
        final String sql = "UPDATE `bookstore`.`order` SET `order_status` = '2' WHERE (`order_ID` = ?);";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,orderID);
            row = ps.executeUpdate();
            if (row == 1) ans = true;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(con,ps,null);
        }
        return ans;
    }

    @Override
    public int cancelOrder(int orderID) {
        final String sql = "UPDATE `order`\n"
                + "SET order_status = 3, ship_status = 3, pay_status = 1\n"
                + "WHERE order_ID = ?;";
        try(Connection connection = JDBCUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
        ) {

            ps.setInt(1, orderID);
            return ps.executeUpdate();
        } catch(Exception e) {
            e.getMessage();
        }
        return 0;
    }


}