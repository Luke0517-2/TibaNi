package com.cga102g3.web.prod.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/22
 **/

import com.cga102g3.core.util.JedisPoolUtil;
import com.cga102g3.web.category.service.impl.CategoryServiceImpl;
import com.cga102g3.web.order.entity.OrderVO;
import com.cga102g3.web.order.service.OrderService;
import com.cga102g3.web.order_Item.entity.OrderItemVO;
import com.cga102g3.web.prod.entity.CarObj;
import com.cga102g3.web.prod.entity.ProdVO;
import com.cga102g3.web.prod.service.ProdService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@WebServlet(urlPatterns = {"/ProdServlet.do"})
public class ProdServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        Gson gson = new Gson();
        HttpSession session = request.getSession(false);

//        System.out.println(action);

        //=====================商城首頁====================

        if ("shop".equals(action)) {
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getALLSALE(1);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(list));
            return;
        }

        //=======================商品明細=====================

        if ("getOne_For_Display".equals(action)) {
            String prodID = request.getParameter("prodID");
            if (prodID != null) {
                ProdService service = new ProdService();
                ProdVO prodVO = service.getOne(Integer.parseInt(prodID));
                request.setAttribute("prodID", prodVO);

                RequestDispatcher rd = request.getRequestDispatcher("/front-end/prod/prod_detail.jsp");
                rd.forward(request, response);
            }
            return;
        }
        //=======================商品明細是否特價=====================

        if ("checkSale".equals(action)) {
            String prodID = request.getParameter("prodID");
            if (prodID != null) {
                ProdService service = new ProdService();
                Integer salePrice = service.forDetail(Integer.valueOf(prodID));
                PrintWriter out = response.getWriter();
//                response.setContentType("application/json; charset=UTF-8");
                out.print(salePrice);
            }
            return;
        }

        //=======================添加商品至購物車=====================

        if ("add".equals(action)) {
            JedisPool jedisPool = JedisPoolUtil.getJedisPool();
            Jedis jedis = jedisPool.getResource();
            System.out.println("here");
            //取得當前登入用戶ID
//            session.setAttribute("mbrID", "5");
            System.out.println("mbrID: "+ session.getAttribute("mbrID"));
            Object mbrID =  session.getAttribute("mbrID");

            PrintWriter out = response.getWriter();

            if (mbrID != null) {
                String prodID = request.getParameter("prodID");

                try {
                    if (jedis.hexists("mbrID" + mbrID, "prodID" + prodID))
                        jedis.hincrBy("mbrID" + mbrID, "prodID" + prodID, 1);
                    else jedis.hset("mbrID" + mbrID, "prodID" + prodID, "1");
                } finally {
                    jedis.close();
                }
                out.print("success");
            } else out.print("null");

            return;
        }

        //=======================刪除購物車商品=====================

        if ("delete".equals(action)) {

            PrintWriter out = response.getWriter();
//            session.setAttribute("mbrID", "5");
            JedisPool jedisPool = JedisPoolUtil.getJedisPool();
            Jedis jedis = jedisPool.getResource();
            Object mbrID =  session.getAttribute("mbrID");
            String prodID = request.getParameter("prodID");

            try {
                Long effect = jedis.hdel("mbrID" + mbrID, "prodID" + prodID);
                Long length = jedis.hlen("mbrID" + mbrID);
                if (length == 0) {
                    out.print("empty");
                } else if (effect == 1) {
                    out.print("success");
                }
            } finally {
                jedis.close();
            }
            return;
        }

        //=======================購物車=====================

        if ("car".equals(action)) {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
//            session.setAttribute("mbrID", "5");
            JedisPool jedisPool = JedisPoolUtil.getJedisPool();
            Jedis jedis = jedisPool.getResource();
            Object mbrID = session.getAttribute("mbrID");

            List<CarObj> list = new ArrayList<>();
            ProdService service = new ProdService();
            try {
                Map<String, String> all = jedis.hgetAll("mbrID" + mbrID);
                for (String str : all.keySet()) {
                    if (!"".equals(str)) {
                        CarObj obj = service.forCar(Integer.valueOf(str.substring(6)));
                        obj.setAmount(Integer.valueOf(all.get(str)));
                        list.add(obj);
                    }
                }
            } finally {
                jedis.close();
            }
            out.print(gson.toJson(list));
            return;
        }
        //=======================使用者改變數量================

        if ("change".equals(action)) {

            String amount = request.getParameter("amount");
            String prodID = request.getParameter("prodID");
//            session.setAttribute("mbrID", "5");
            JedisPool jedisPool = JedisPoolUtil.getJedisPool();
            Jedis jedis = jedisPool.getResource();
            Object mbrID = session.getAttribute("mbrID");

            try {
                jedis.hset("mbrID" + mbrID, "prodID" + prodID, amount);
            } finally {
                jedis.close();
            }
            return;
        }

        //=======================購物車選取物品存入session================

        if ("checkout".equals(action)) {
            ProdService svc = new ProdService();
            //String 類型--prodID
            String prodIDArray = request.getParameter("prodID");
            JsonArray prodList = gson.fromJson(prodIDArray, JsonArray.class);
            //String 類型--amount
            String amountArray = request.getParameter("amountArray");
            JsonArray amountList = gson.fromJson(amountArray, JsonArray.class);
            //將購物車中有選取的物品存入session
            List<CarObj> list = new ArrayList<>();
            for (int i = 0; i < prodList.size(); i++) {
                CarObj obj = svc.forCar(prodList.get(i).getAsInt());
                obj.setAmount(amountList.get(i).getAsInt());
                list.add(obj);
            }
            session.setAttribute("list", list);
            return;
        }
        //=======================結帳頁面=====================

        if ("checkout2".equals(action)) {

            List<CarObj> list = (List<CarObj>) session.getAttribute("list");
            System.out.println(list);
            int total = 0;
            for (CarObj obj : list) {
                String discount = obj.getDiscount();
                if ("N".equals(discount)) total += obj.getPrice() * obj.getAmount();
                else total += obj.getSalePrice() * obj.getAmount();
            }
            PrintWriter out = response.getWriter();
            out.print(total);
            return;
        }

        //=======================結帳頁面=====================

        if ("order".equals(action)) {

            //錯誤處理
            Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
            request.setAttribute("errorMsgs", errorMsgs);

            String payerName = request.getParameter("payerName");
            String nameReg = "^[(\u4e00-\u9fa5))]{2,4}$";
            if (payerName == null || payerName.trim().length() == 0) {
                errorMsgs.put("payerName", "付款人姓名 : 請勿空白");
            } else if (!payerName.trim().matches(nameReg)) {
                errorMsgs.put("payerName", "付款人姓名 : 只能是中文字，且長度必須在2到4之間");
            }
            String payerPhone = request.getParameter("payerPhone");
            String phoneReg = "^09\\d{8}$";
            if (payerPhone == null || payerPhone.trim().length() == 0) {
                errorMsgs.put("payerPhone", "付款人手機 : 請勿空白");
            } else if (!payerPhone.trim().matches(phoneReg)) {
                errorMsgs.put("payerPhone", "付款人手機 : 格式不正確");
            }
            String method = request.getParameter("method");
            if (method == null) {
                errorMsgs.put("method", "請選擇付款方式");
            }
            String receiverName = request.getParameter("receiverName");
            String receiverPhone = request.getParameter("receiverPhone");
            if (receiverName == null || receiverName.trim().length() == 0) {
                errorMsgs.put("receiverName", "收貨人姓名 : 請勿空白");
            } else if (!receiverName.trim().matches(nameReg)) {
                errorMsgs.put("receiverName", "收貨人姓名 : 只能是中文字，且長度必須在2到4之間");
            }
            if (receiverPhone == null || receiverPhone.trim().length() == 0) {
                errorMsgs.put("receiverPhone", "收貨人手機 : 請勿空白");
            } else if (!receiverPhone.trim().matches(phoneReg)) {
                errorMsgs.put("receiverPhone", "收貨人手機 : 格式不正確");
            }
            String street = request.getParameter("street");
            String streetReg = "^[(\u4e00-\u9fa5)(0-9)]{2,10}$";
            if (street == null || street.trim().length() == 0) {
                errorMsgs.put("street", "收貨人地址 : 請勿空白");
            } else if (!street.trim().matches(streetReg)) {
                errorMsgs.put("street", "收貨人地址 : 格式不正確");
            }

            if (!errorMsgs.isEmpty()) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/front-end/prod/checkout.jsp");
                requestDispatcher.forward(request, response);
                return;
            }

            //========================永續層=======================

//            session.setAttribute("mbrID", "5");
            JedisPool jedisPool = JedisPoolUtil.getJedisPool();
            Jedis jedis = jedisPool.getResource();
            Object mbrID = session.getAttribute("mbrID");
            List<CarObj> carList = (List<CarObj>) session.getAttribute("list");
            List<OrderItemVO> list = new ArrayList<>();
            int total = 0;
            int price = 0;

            try {

                for (int i = 0; i < carList.size(); i++) {
                    //新增orderItem物件
                    OrderItemVO orderItemVO = new OrderItemVO();
                    CarObj obj = carList.get(i);
                    orderItemVO.setProdID(obj.getProdID());
                    orderItemVO.setAmount(obj.getAmount());
                    if (obj.getDiscount() == "N") price = obj.getPrice();
                    else price = obj.getSalePrice();
                    orderItemVO.setSalePrice(price);

                    //計算order總價
                    total += price * obj.getAmount();
                    list.add(orderItemVO);

                    //移除redis中物品
                    jedis.hdel("mbrID" + mbrID, "prodID" + obj.getProdID());
                }
            } finally {
                jedis.close();
            }
            OrderVO orderVO = new OrderVO();
            String mbrIDStr = mbrID + "";
            orderVO.setMbrID(Integer.valueOf(mbrIDStr));
            orderVO.setTotalPrice(total);
            orderVO.setOrderStatus(0);
            orderVO.setShipStatus(0);

            if ("信用卡".equals(method)) {
                orderVO.setPayMethod(0);
            } else if ("TibaNi錢包".equals(method)) {
                orderVO.setPayMethod(1);
            } else {
                orderVO.setPayMethod(2);
            }
            if (!("貨到付款".equals(method))) orderVO.setPayStatus(0);
            else orderVO.setPayStatus(1);

            OrderService service = new OrderService();
            service.addWithOrderItem(orderVO, list);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/front-end/prod/order.jsp");
            requestDispatcher.forward(request, response);

            return;
        }
        if ("finish".equals(action)) {
            List<CarObj> carList = (List<CarObj>) session.getAttribute("list");
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(carList));
            return;
        }

        //****** Browse Product by Category, Sale, Price Range, Keyword Search *******//

        /** Display book categories **/
        if ("get_Category".equals(action)) {
//            String categoryID = request.getParameter("categoryID");
            CategoryServiceImpl service = new CategoryServiceImpl();
            List<Map<String, Object>> list = service.getALL();
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
//            Gson gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        /** Search by category **/
        if ("get_Category_Prod".equals(action)) {
            Integer categoryID = Integer.valueOf(request.getParameter("categoryID"));
            request.getSession().setAttribute("categoryID", categoryID);
            RequestDispatcher rd = request.getRequestDispatcher("/front-end/prod/browse_category.jsp");
            rd.forward(request, response);
        }

        /** Display products by category **/
        if ("shop_Category".equals(action)) {
            Integer categoryID = (Integer) request.getSession().getAttribute("categoryID");
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getCategory(categoryID);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
//            Gson gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        /** Display products on sale **/
        if ("get_Sale".equals(action)) {
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getALLSALE(1);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
//            Gson gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }


        /** Search by Price **/
        if ("get_Price".equals(action)) {
            Integer price = Integer.valueOf(request.getParameter("price"));
            request.getSession().setAttribute("price", price);
            RequestDispatcher rd = request.getRequestDispatcher("/front-end/prod/browse_price.jsp");
            rd.forward(request, response);
        }

        /** Display products by price range **/
        if ("shop_Price".equals(action)) {
            Integer categoryID = (Integer) request.getSession().getAttribute("price");
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getPrice(categoryID);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
//            Gson gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        /** Search by keyword **/
        if ("search".equals(action)) {
            /***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
            String bookTitle = null;
            try {
                bookTitle = request.getParameter("keyword_input");
            } catch (Exception e) {
                System.out.println(e);
            }
            /***************************2.開始新增資料***************************************/
//            ProdService prodSvc = new ProdService();
//            List<Map<String,Object>> titleList = prodSvc.searchProd(bookTitle);
//            request.setAttribute("titleList", titleList);
//            System.out.println("titleList="+titleList);
            request.getSession().setAttribute("bookTitle", bookTitle);

            /***************************3.新增完成,準備轉交(Send the Success view)***********/
            String url = "/front-end/prod/browse_search.jsp";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
            return;
        }

        /** Display keyword searching result **/
        if ("shop_Search".equals(action)) {
            String bookTitle = (String) request.getSession().getAttribute("bookTitle");
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.searchProd(bookTitle);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
//            Gson gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        if ("indexshop".equals(action)) {
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getALLSALE();
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        if ("indexts".equals(action)) {
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getTOPSALE();
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        /** Alan **/
        if ("indexshop".equals(action)) {
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getALLSALE();
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        if ("indexts".equals(action)) {
            ProdService service = new ProdService();
            List<Map<String, Object>> list = service.getTOPSALE();
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            gson = new Gson();
            out.print(gson.toJson(list));
            return;
        }

        /** Search by keyword **/
        if ("search".equals(action)) {
            /***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
            String bookTitle = null;
            try {
                bookTitle = request.getParameter("keyword_input");
            } catch (Exception e) {
                System.out.println(e);
            }
            /***************************2.開始新增資料***************************************/
            request.getSession().setAttribute("bookTitle", bookTitle);

            /***************************3.新增完成,準備轉交(Send the Success view)***********/
            String url = "/front-end/prod/browse_search.jsp";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
            return;
        }
    }
}

