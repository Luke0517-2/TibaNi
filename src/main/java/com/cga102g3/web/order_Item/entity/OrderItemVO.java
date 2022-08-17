package com.cga102g3.web.order_Item.entity;

import java.io.Serializable;

import com.cga102g3.web.order.entity.OrderVO;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/24
 **/
public class OrderItemVO implements Serializable {
    private Integer orderID;
    private Integer prodID;
    private Integer amount;
    private Integer salePrice;
    
    //@author: Alan
    private OrderVO orderVO;

	public Integer getOrderID() {
        return orderID;
    }
    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }
    public Integer getProdID() {
        return prodID;
    }
    public void setProdID(Integer prodID) {
        this.prodID = prodID;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Integer getSalePrice() {
        return salePrice;
    }
    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public OrderVO getOrderVO() {
    	return orderVO;
    }
    public void setOrderVO(OrderVO orderVO) {
    	this.orderVO = orderVO;
    }
}

