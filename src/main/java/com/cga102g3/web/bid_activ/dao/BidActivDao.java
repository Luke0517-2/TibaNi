package com.cga102g3.web.bid_activ.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cga102g3.web.bid_activ.entity.BidActiv;
import com.cga102g3.web.bid_activ.entity.Bidder;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-22 下午 04:53
 */
public interface BidActivDao {
//    Set<String> ACTIVITIES = Collections.synchronizedSet(new HashSet<>());
//    Set<String> ACTIVITIES = new HashSet<>();
    void insert(BidActiv bidActiv);
    void insert(List<BidActiv> bidActivs);
    void delete(Integer bidID);
    void delete(String bidActivID);
    List<BidActiv> getAll();

    /**
     * 更新競標商品價格，投標金額需高於目前最高價格，且標的商品也必須存在
     * @param bidID
     * @param bidder
     */
    void updateRec(Integer bidID, Bidder bidder);

    /**
     * 依競標商品編號查詢目前最高出價者以及出資金額
     * 回傳null表示沒人出價
     * @param bidID 競標商品編號
     * @return
     */
    Bidder getCurWinner(Integer bidID);

    /**
     * 依競標商品編號查詢所有投標人者以及出資金額
     * 回傳空list表示沒人出價
     * @param bidID
     * @return
     */
    List<Bidder> getAllBidders(Integer bidID);

    Set<String> getAllActivitiesIDs();

    boolean isExists(Integer bidID);

    Set<BidActiv> getExpiredActivs();

    BidActiv getBidActivity(Integer bidID);

    List<Map<String, Object>> selectAllBidInfo(Integer page);
    List<Map<String, Object>> selectBidInfoByISBN(String ISBN, int page);
    List<Map<String, Object>> selectBidInfoByTitle(String title, int page);
    List<Map<String, Object>> selectAllBidInfo();
}
