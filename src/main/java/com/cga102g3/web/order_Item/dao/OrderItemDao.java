package com.cga102g3.web.order_Item.dao;

import com.cga102g3.web.order_Item.entity.OrderItemVO;

import java.util.List;
import java.util.Map;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/24
 **/
public interface OrderItemDao {
    

    /**使用訂單編號查詢明細**/
    public List<OrderItemVO> getAll(Integer orderID);

    /**新增訂單同時新增明細**/
    public void insert2 (OrderItemVO orderItemVO , java.sql.Connection con);

    /**會員訂單功能**/
    public List<Map<String,Object>> getByOrderID(Integer orderID);

    //Alan
    public List<Map<String,Object>> findAll(int orderID);
}
