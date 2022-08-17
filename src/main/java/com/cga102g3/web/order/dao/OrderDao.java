package com.cga102g3.web.order.dao;

import com.cga102g3.web.order.entity.OrderVO;
import com.cga102g3.web.order_Item.entity.OrderItemVO;

import java.util.List;
import java.util.Map;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/24
 **/
public interface OrderDao {
    public void insertWithOrderItem(OrderVO orderVO , List<OrderItemVO> list);
    
    /**使用訂單編號查詢訂單**/
    public OrderVO findByPrimaryKey(Integer orderID);
    /**找相同會員訂單**/
    public List<OrderVO> getAll(Integer mbrID);
    
    
    /**
     * @description: 針對後台查看order
     * @author: Alan
     * @date: 2022/6/29
     **/
    public List<Map<String,Object>> findAll();
    
    public OrderVO findOneOrder(int orderID);

    /**會員取消訂單**/
    public boolean cancel(Integer orderID);
    /**會員完成訂單**/
    public boolean finish(Integer orderID);

    public int cancelOrder(int orderID);
}
