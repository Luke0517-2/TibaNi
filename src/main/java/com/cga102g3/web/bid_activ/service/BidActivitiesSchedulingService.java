package com.cga102g3.web.bid_activ.service;

import java.util.List;
import java.util.Set;

import com.cga102g3.web.bid_activ.entity.BidActiv;
import com.cga102g3.web.bid_prod.entity.BidProd;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-23 下午 11:20
 */
public interface BidActivitiesSchedulingService {
    /**
     * 競標商品上架
     */
    void launchProducts();

    /**
     * 競標商品下架
     */
    void discontinuedProducts();
    List<BidProd> getLaunchProducts();
    void persistActivsRes(Set<BidActiv> activities);
}
