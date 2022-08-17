package com.cga102g3.web.order_Item.controller; /**
 * @description: TODO
 * @author: Luke
 * @date: 2022/7/4
 **/

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cga102g3.web.order_Item.service.OrderItemService;
import com.google.gson.Gson;

@WebServlet(name = "OrderItemServlet", value = "/OrderItemServlet.do")
public class OrderItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        //=================會員查詢訂單==================

        if("getDetail".equals(action)){
            String orderID = request.getParameter("orderID");
            if (orderID != null && orderID.trim().length() != 0){
                OrderItemService svc = new OrderItemService();
                List<Map<String, Object>> list = svc.getByOrderID(Integer.valueOf(orderID));
                response.setContentType("application/json; charset=UTF-8");
                PrintWriter out = response.getWriter();
                Gson gson = new Gson();
                out.print(gson.toJson(list));
            }
            return;
        }
    }
}
