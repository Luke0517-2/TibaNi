package com.cga102g3.web.order.controller;

import static com.cga102g3.core.util.CommonUtil.writePojo2Json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cga102g3.core.controller.BaseGetAPIServlet;
import com.cga102g3.core.pojo.ErrMsg;
import com.cga102g3.web.order.service.OrderService;

/**
 * Servlet implementation class GetOrderAPIServlet
 */
@WebServlet(name = "GetOrderAPIServlet",urlPatterns= {"/order/api/*"})
public class GetOrderAPIServlet extends BaseGetAPIServlet {
	private static final long serialVersionUID = 1L;
       
	private OrderService orderService = new OrderService();
	
	

    

	/**
	 * 路由：/order/api/getOrderInfo?OrderID={int}
	 */
	public void getOrderInfo(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		Integer orderID = null;
		
		try {
			orderID = Integer.parseInt(request.getParameter("orderID"));
		} catch (Exception e) {
			e.printStackTrace();
			ErrMsg errMsg = new ErrMsg();
			errMsg.setMsg("error");
			writePojo2Json(response, errMsg);
			return;
		}
		boolean result = orderService.cancelOrder(orderID);
		Map<String, Object> res = new HashMap<>();
		if (result) {
			res.put("stat", "success");
			writePojo2Json(response, res);
			return;
		} else{
			res.put("stat", "fail");
			writePojo2Json(response, res);
			return;
		}
	}


}
