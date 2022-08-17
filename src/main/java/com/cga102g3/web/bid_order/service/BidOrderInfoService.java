package com.cga102g3.web.bid_order.service;

import java.util.Map;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-07-02 上午 10:54
 */
public interface BidOrderInfoService {
    /**
     * 依mbrID取得會員競標訂單
     * @param mbrID
     * @return
     */
    Map<String, Object> getBidOrderInfo(Integer mbrID);

    Map<String, Object> updateStat2Delivered(Integer bidOrderID);
}
