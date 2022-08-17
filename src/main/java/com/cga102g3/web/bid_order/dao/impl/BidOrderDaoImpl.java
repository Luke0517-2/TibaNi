package com.cga102g3.web.bid_order.dao.impl;



import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.bid_order.dao.BidOrderDao;
import com.cga102g3.web.bid_order.entity.BidOrder;
import com.cga102g3.web.bid_order.entity.BidOrderStat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-11 上午 11:33
 */
public class BidOrderDaoImpl implements BidOrderDao {
    @Override
    public int insert(BidOrder pojo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int res = 0;
        final String sql =
                "insert into bid_order(bid_ID, mbr_ID, bid_price) \n" +
                "VALUES (?, ?, ?); ";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pojo.getBidID());
            pstmt.setInt(2, pojo.getMbrID());
            pstmt.setInt(3, pojo.getBidPrice());
            res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return res;
    }

    /**
     * 競標商品訂單不能刪除
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    /**
     * 競標商品訂單僅能修改狀態bid_ship_stat, pay_seller
     * @param pojo
     * @return
     */
    @Override
    public int update(BidOrder pojo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int res = 0;
        final String sql =
                "update bid_order\n" +
                "set bid_ship_stat = ?\n" +
                "where bid_order_ID = ?;";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pojo.getBidShipStat());
            pstmt.setInt(2, pojo.getBidOrderID());
            res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return res;
    }

    @Override
    public BidOrder selectByPrimaryKey(Integer id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BidOrder bidOrder = null;

        final String sql =
                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
                "       bid_ship_stat\n" +
                "from bid_order\n" +
                "where bid_order_ID = ?;";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            List<BidOrder> res = retrieve(rs);
            if(!res.isEmpty()) {
                bidOrder = res.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return bidOrder;
    }

    /**
     * 未實現
     * @return
     */
    @Override
    public List<BidOrder> selectAll() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BidOrder> list = null;

        final String sql =
                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
                "       bid_ship_stat\n" +
                "from bid_order\n";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /**
     * 查詢全部競標商品訂單，每頁12筆資料
     * @param page
     * @return
     */
    @Override
    public List<BidOrder> selectAll(Integer page) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BidOrder> list = null;

        final String sql =
                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
                "       bid_ship_stat\n" +
                "from bid_order\n" +
                "limit ?, ?;";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize + 1);
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public int insert(Connection conn, BidOrder bidOrder) throws SQLException {
//        int res = 0;
        final String sql =
                "insert into bid_order(bid_ID, mbr_ID, bid_price) \n" +
                "VALUES (?, ?, ?); ";
        try( PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, bidOrder.getBidID());
            pstmt.setInt(2, bidOrder.getMbrID());
            pstmt.setInt(3, bidOrder.getBidPrice());
            return pstmt.executeUpdate();
        }
    }

    /**
     * 依據mbrID選取會員競標訂單訊息
     * @param mbrID
     * @return
     */
    @Override
    public List<Map<String, Object>> selectByMbrID(Integer mbrID) {
        final String sql =
                "SELECT bo.bid_order_ID, bo.bid_ID, bo.bid_price, bo.bid_order_date, bo.bid_ship_stat, b.title\n" +
                "FROM bid_order AS bo\n" +
                "        JOIN bid_prod AS bp\n" +
                "            ON bo.bid_ID = bp.bid_id\n" +
                "        JOIN book AS b\n" +
                "            ON bp.book_id = b.book_ID\n" +
                "WHERE bo.mbr_ID = ?;";
        List<Map<String, Object>> bidOrderInfo = new ArrayList<>();
        try(Connection conn = JDBCUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, mbrID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> bidOrderInfoRec = new HashMap<>();
                bidOrderInfoRec.put("bidOrderID", rs.getInt("bid_order_ID"));
                bidOrderInfoRec.put("bidID", rs.getInt("bid_ID"));
                bidOrderInfoRec.put("bidPrice", rs.getInt("bid_price"));
                bidOrderInfoRec.put("bidOrderDate", rs.getTimestamp("bid_order_date").getTime());
                BidOrderStat bidShipStat = convert(rs.getInt("bid_ship_stat"));
                bidOrderInfoRec.put("bidShipStat", bidShipStat.getStat());
                bidOrderInfoRec.put("title", rs.getString("title"));
                bidOrderInfo.add(bidOrderInfoRec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bidOrderInfo;
    }

    @Override
    public int updateStat2Delivered(Integer bidOrderID) {
        final String sql =
                "UPDATE bid_order\n" +
                "SET bid_ship_stat = 2\n" +
                "where bid_order_ID = ?;";
        try(Connection conn = JDBCUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, bidOrderID);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 依會員編號查詢競標商品訂單，每頁12筆資料
     * @param mbrID
     * @param page
     * @return
     */
    @Override
    public List<BidOrder> selectByMbrID(Integer mbrID, Integer page) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BidOrder> list = null;

        final String sql =
                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
                "       bid_ship_stat\n" +
                "from bid_order\n" +
                "where mbr_ID = ?\n" +
                "limit ?, ?;";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, mbrID);
            pstmt.setInt(2, (page - 1) * pageSize);
            pstmt.setInt(3, pageSize + 1);
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /**
     * 依是否付款查詢競標商品訂單，每頁12筆資料
     * @param paySeller
     * @param page
     * @return
     */
//    @Override
//    public List<BidOrder> selectByPaySeller(Boolean paySeller, Integer page) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        List<BidOrder> list = null;
//
//        final String sql =
//                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
//                        "       bid_ship_stat, pay_seller\n" +
//                        "from bid_order\n" +
//                        "where pay_seller = ?\n" +
//                        "limit ?, ?;";
//
//        try {
//            conn = JDBCUtil.getConnection();
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setBoolean(1, paySeller);
//            pstmt.setInt(2, (page - 1) * pageSize);
//            pstmt.setInt(3, pageSize + 1);
//            rs = pstmt.executeQuery();
//            list = retrieve(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            JDBCUtil.close(conn, pstmt, rs);
//        }
//        return list;
//    }

    /**
     * 將查詢結果放入list中，並返回
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<BidOrder> retrieve(ResultSet rs) throws SQLException {
        List<BidOrder> list = new ArrayList<>();
        while (rs.next()) {
            BidOrder bidOrder = new BidOrder();
            bidOrder.setBidOrderID(rs.getInt("bid_order_id"));
            bidOrder.setBidID(rs.getInt("bid_id"));
            bidOrder.setMbrID(rs.getInt("mbr_id"));
            bidOrder.setBidPrice(rs.getInt("bid_price"));
            bidOrder.setBidOrderDate(rs.getTimestamp("bid_order_date"));
            bidOrder.setBidShipStat(rs.getInt("bid_ship_stat"));
            list.add(bidOrder);
        }
        return list;
    }
    private BidOrderStat convert(int statCode) {
        switch (statCode) {
            case 0:
                return BidOrderStat.PICKING;
            case 1:
                return  BidOrderStat.IN_TRANSIT;
            case 2:
                return BidOrderStat.DELIVERED;
            default:
                return BidOrderStat.ERROR;
        }
    }

    /** Given start date and end date, search bid product order **/
    @Override
    public List<BidOrder> selectByOrderDate(Timestamp startDate, Timestamp endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BidOrder> list = null;

        final String sql =
                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
                        "       bid_ship_stat\n " +
                        "from bid_order\n " +
                        "where bid_order_date between ? and ?";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, startDate);
            pstmt.setTimestamp(2, endDate);
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /** Search new bid product order limit 12 rows **/
    @Override
    public List<BidOrder> selectNewOrder() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BidOrder> list = null;

        final String sql =
                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
                        "       bid_ship_stat\n " +
                        "from bid_order\n " +
                        "order by bid_order_id desc limit 12";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }


    /** Search by bid product ship state **/
    @Override
    public List<BidOrder> selectShipStat(Integer bidShipStat) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BidOrder> list = null;

        final String sql =
                "select bid_order_ID, bid_ID, mbr_ID, bid_price, bid_order_date,\n" +
                        "       bid_ship_stat\n " +
                        " from bid_order\n " +
                        " where bid_ship_stat = ?";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bidShipStat);
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }


    /** Update bid product status -> shipped **/
    @Override
    public int updateShipped(Integer bidOrderID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final String sql = "UPDATE bid_order SET bid_ship_stat =1 \r\n"
                + "WHERE bid_order_id = ?";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bidOrderID);
            return pstmt.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return 0;
    }

    /** Search orders by Member Name **/
    @Override
    public List<BidOrder> selectMbrName(String mbrName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BidOrder> list = null;


        final String sql = "select b.bid_order_ID, b.bid_ID, b.mbr_ID, b.bid_price, b.bid_order_date, b.bid_ship_stat\r\n"
                            + "From bid_order b\r\n"
                            + "Left Join member m\r\n"
                            + "on b.mbr_ID = m.mbr_ID\r\n"
                            + "where mbr_name like ?";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + mbrName + "%");
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public static void main(String[] args) {
        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
        BidOrder bidOrder= bidOrderDao.selectByPrimaryKey(10);
        System.out.println(bidOrder);

//        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
//        List<BidOrder> bidOrders = bidOrderDao.selectAll(1);
//        System.out.println(bidOrders.size());

//        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
//        List<BidOrder> list = bidOrderDao.selectByMbrID(1, 1);
//        System.out.println(list);

//        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
//        List<BidOrder> list = bidOrderDao.selectByPaySeller(true, 1);
//        System.out.println(list);

//        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
//        BidOrder bidOrder = new BidOrder();
//        bidOrder.setBidID(1);
//        bidOrder.setMbrID(1);
//        bidOrder.setBidPrice(650);
//        bidOrderDao.insert(bidOrder);

//        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
//        BidOrder bidOrder = new BidOrder();
//        bidOrder.setBidOrderID(4);
//        bidOrder.setPaySeller(true);
//        bidOrder.setBidShipStat(1);
//        bidOrderDao.update(bidOrder);

//        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
//        List<Map<String, Object>> maps = bidOrderDao.selectByMbrID(1);
//        maps.forEach(e -> {
//            System.out.println(e.get("bidID"));
//        });

//        BidOrderDao bidOrderDao = new BidOrderDaoImpl();
//        bidOrderDao.updateStat2Delivered(5);

    }
}
