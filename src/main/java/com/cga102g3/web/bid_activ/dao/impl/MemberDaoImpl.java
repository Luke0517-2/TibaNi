package com.cga102g3.web.bid_activ.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.bid_activ.dao.MemberDao;
import com.cga102g3.web.bid_activ.dao.WalletRecDao;
import com.cga102g3.web.bid_activ.entity.Member;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-25 下午 02:17
 */
public class MemberDaoImpl implements MemberDao {
//    private WalletRecDao walletRecDao = new WalletRecDaoImpl();

    @Override
    public Member selectByIDWithTCoinBal(Integer mbrID) {
        final String sql =
                "SELECT mbr_ID, Tcoin_bal\n" +
                "FROM member\n" +
                "WHERE mbr_ID = ?";
        try(Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, mbrID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Member mbr = new Member();
                mbr.setMbrID(rs.getInt("mbr_ID"));
                mbr.settCoinBal(rs.getInt("Tcoin_bal"));
                return mbr;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean chkEnufBal(Integer mbrID, Integer price) {
        Member member = this.selectByIDWithTCoinBal(mbrID);
        return member.gettCoinBal() >= price;
    }

    @Override
    public int prepaid4Bid(Connection conn, Integer mbrID, Integer amount) throws SQLException {
        final String sql1 =
                "SELECT Tcoin_bal\n" +
                "FROM member\n" +
                "WHERE mbr_ID = ?";
        final String sql2 =
                "UPDATE member\n" +
                "SET Tcoin_bal = ?\n" +
                "WHERE mbr_ID = ?;";
        int tcoin_bal = 0;
        try(PreparedStatement ps = conn.prepareStatement(sql1);) {
            ps.setInt(1,mbrID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tcoin_bal = rs.getInt("Tcoin_bal");
            }

            try(PreparedStatement pstmt = conn.prepareStatement(sql2);) {
                // 預收是減去帳戶金額
                pstmt.setInt(1, tcoin_bal - amount);
                pstmt.setInt(2, mbrID);
                return pstmt.executeUpdate();
            }
        }
    }

    @Override
    public int refund4bid(Connection conn, Integer mbrID, Integer amount) throws SQLException {
        final String sql1 =
                "SELECT Tcoin_bal\n" +
                "FROM member\n" +
                "WHERE mbr_ID = ?";
        final String sql2 =
                "UPDATE member\n" +
                "SET Tcoin_bal = ?\n" +
                "WHERE mbr_ID = ?;";
        int tcoin_bal = 0;
        try(PreparedStatement ps = conn.prepareStatement(sql1);) {
            ps.setInt(1,mbrID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tcoin_bal = rs.getInt("Tcoin_bal");
            }

            try(PreparedStatement pstmt = conn.prepareStatement(sql2);) {
                // 退款是減去帳戶金額
                pstmt.setInt(1, tcoin_bal + amount);
                pstmt.setInt(2, mbrID);
                return pstmt.executeUpdate();
            }
        }
    }

    public static void main(String[] args) {
        MemberDao memberDao = new MemberDaoImpl();
        WalletRecDao walletRecDao = new WalletRecDaoImpl();
//        Member member_ = memberDao.selectTCoinBal(2);
//        System.out.println(member_);
//        System.out.println(memberDao.chkEnufBal(2, 5000));

        try (Connection conn = JDBCUtil.getConnection();){
            // 開啟交易模式
            conn.setAutoCommit(false);
            try{
                // 放交易行為
                // mbrID = 1 會員扣款3000
//                memberDao.prepaid4Bid(conn, 1, 3000);
//                walletRecDao.prepaid4Bid(conn, 1, 3000);

                // mbrID = 1 會員退款3000
                memberDao.refund4bid(conn, 1, 3000);
                walletRecDao.refund4bid(conn, 1, 3000);

                conn.commit();
                // 結束交易模式
                conn.setAutoCommit(true);
            } catch (SQLException se) {
                se.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException excep) {
                    throw new RuntimeException("rollback error occurred. "
                            + excep.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
