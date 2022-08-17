package com.cga102g3.web.bid_activ.service;

import java.util.List;
import java.util.Map;

import com.cga102g3.web.bid_activ.entity.BidActiv;
import com.cga102g3.web.bid_activ.entity.BidActivityStat;
import com.cga102g3.web.bid_activ.entity.BidErrStat;
import com.cga102g3.web.bid_activ.entity.Bidder;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-25 下午 09:41
 */
public interface BidGameService {
    Map<String, Object> getBidInfo(Integer bidID);
    Bidder getCurWinner(Integer bidID);

    /**
     * 判斷該會員是否有足夠的餘額以直接價購買競標商品
     * @param mbrID
     * @param bidID
     * @return
     */
    boolean isBidderDirectGet(Integer mbrID, Integer bidID);

    /**
     * 判斷該會員是否有足夠的餘額繼續加碼
     * @param bidder
     * @param bidID
     * @return
     */
    BidErrStat isEnableRaise(Bidder bidder, Integer bidID);

    /**
     * 會員出價競標標商品
     * @param bidder
     * @param bidID
     */
    BidActivityStat raise(Bidder bidder, Integer bidID);

    BidActivityStat participate(Bidder bidder, Integer bidID);


    BidActiv getBidActivity(Integer bidID);

    void persistActivRes(BidActiv activity);

    Map<String, Object> getBooksByKeyword(String keyword, int page);

    List<Bidder> getAllBidders(Integer bidID);

    Map<String, Object> getAllBidInfo();
}