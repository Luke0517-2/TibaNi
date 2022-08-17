package com.cga102g3.web.bid.controller;

import com.cga102g3.web.bid_prod.dao.BidService;
import com.cga102g3.web.bid_prod.entity.BidProd;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@WebServlet("/back-end/bid/bid.do")
public class BidProdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		/*********************************
		 * Search By Primary Key
		 ********************************/
		if ("show_One".equals(action)) {
			/* Error Messages List */
			List<String> errorMsgs = new LinkedList<>();
			req.setAttribute("errorMsgs", errorMsgs);

			/* Check null */
			String str = req.getParameter("bid_id");
			if (str == null || (str.trim()).length() == 0) {
				errorMsgs.add("請輸入競標商品編號");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}

			/* Check Input format */
			Integer bidID = null;

			try {
				bidID = Integer.valueOf(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				errorMsgs.add("商品編號格式不正確");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}

			/* Check if input exist in DB */
			BidService bs = new BidService();
			BidProd bidProd = bs.showOneBid(bidID);
			if (bidProd == null) {
				errorMsgs.add("查無資料");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}

			/* Show the success search */
			req.setAttribute("bidProd", bidProd);
			String url = "/back-end/bid/bidprod_back_show_page.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);

		}

		/*********************************
		 * Search By State
		 ********************************/
		if ("show_State".equals(action)) {
			/* Error Messages List */
			List<String> errorMsgs = new LinkedList<>();
			req.setAttribute("errorMsgs", errorMsgs);

			/* Check null */
			String str = req.getParameter("bidProdStat");
			System.out.println(str);
			if (str == null || (str.trim()).length() == 0) {
				errorMsgs.add("請選擇商品狀態");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}

			/* Check Input format */
			Integer bidProdStat = null;
			try {
				bidProdStat = Integer.valueOf(str);

			} catch (NumberFormatException e) {
				e.printStackTrace();
				errorMsgs.add("請選擇商品狀態");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}

			/* Check if input exist in DB */
			BidService bs = new BidService();
			List<BidProd> bidProd = bs.showOneStat(bidProdStat);
			if (bidProd == null) {
				errorMsgs.add("查無資料");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}

			/* Show the success search */
//			req.setAttribute("bidProd", bidProd);
			req.getSession().setAttribute("bidProdStat", bidProdStat);

			String url = "/back-end/bid/bidprod_back_show_page2.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);

		}
		
		/*********************************
		 * Search By Book Title
		 ********************************/
		if ("show_title".equals(action)) {
			/* Error Messages List */
			List<String> errorMsgs = new LinkedList<>();
			req.setAttribute("errorMsgs", errorMsgs);

			/* Check null */
			String bidProdTitle = req.getParameter("book_title");
			if (bidProdTitle == null || (bidProdTitle.trim()).length() == 0) {
				errorMsgs.add("請輸入書籍商品名稱");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}
			
			/* Check if input exist in DB */
			BidService bs = new BidService();
			List<BidProd> bidProd = bs.showTitle(bidProdTitle);
			if (bidProd.size() == 0) {
				errorMsgs.add("查無資料");
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
				fail.forward(req, res);
				return;
			}

			/* Show the success search */
			req.getSession().setAttribute("bidProdTitle", bidProdTitle);

			String url = "/back-end/bid/bidprod_back_show_page3.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
		
		}
		
		
		/***********************************
		 * Insert Bid Product
		 ***********************************/
		if ("insert".equals(action)) {

			/* Error Messages Collection */
			Map<String, String> errorMsgs = new LinkedHashMap<>();
			Map<String, String> passMsgs = new LinkedHashMap<>();
			req.setAttribute("errorMsgs", errorMsgs);
			req.setAttribute("passMsgs", passMsgs);

			/****** Book ID ******/
			/* Check if the input is null and the format */
			String bookIDstr = req.getParameter("bookID");
			Integer bookID = null;

			if (bookIDstr == null || (bookIDstr.trim()).length() == 0) {
				errorMsgs.put("bookID", "請選擇書籍");
			} else {
				try {
					bookID = Integer.valueOf(bookIDstr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					errorMsgs.put("bookID", "書籍編號格式不正確");
				}
			}

			/****** Bid Start Price ******/
			/* Check if the input is null and the format */
			String startPricestr = req.getParameter("startPrice");
			Integer startPrice = null;
			if (startPricestr == null || (startPricestr.trim()).length() == 0) {
				errorMsgs.put("startPrice", "請輸入競標底價");
			} else {
				try {
					startPrice = Integer.valueOf(startPricestr);
					if (startPrice < 1)
						errorMsgs.put("startPrice", "競標底價不得低於1元");
				} catch (NumberFormatException e) {
					e.printStackTrace();
					errorMsgs.put("startPrice", "競標底價格式不正確");
				}
			}

			/* Check the start price */

			/****** Bid Direct Price ******/
			/* Check if the input is null and the format */
			String directPricestr = req.getParameter("bidDirectPrice");
			Integer directPrice = null;
			if (directPricestr == null || (directPricestr.trim()).length() == 0) {
				errorMsgs.put("bidDirectPrice", "請輸入直購價");
			} else {
				try {
					directPrice = Integer.valueOf(directPricestr);
					if (directPrice < 1)
						errorMsgs.put("bidDirectPrice", "競標直購價不得低於1元");
						if (startPrice != null && startPrice >= directPrice) {
							
									errorMsgs.put("startPrice", "直購價須高於競標底價，請重新輸入");
									errorMsgs.put("bidDirectPrice", "直購價須高於競標底價，請重新輸入");
							  
						}
					} catch (NumberFormatException e) {
					e.printStackTrace();
					errorMsgs.put("bidDirectPrice", "直購價格式不正確");
				}
			}

			Timestamp bidStart = null;
			try {
				bidStart = Timestamp.valueOf(req.getParameter("bidStart").trim());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Timestamp bidEnd = null;
			Integer bidProdStat = 0;
			try {
				bidEnd = Timestamp.valueOf(req.getParameter("bidEnd").trim());
				if (bidStart != null) {
					bidProdStat = 1;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_insert_page.jsp");
				fail.forward(req, res);
				return;
			}

			if (bidStart != null && bidEnd != null) {
				passMsgs.put("state", "競標已排程");
			} else {
				bidStart = null;
				bidEnd = null;
			}

			/****** Insert Data into Database ******/
			BidService sc = new BidService();
			sc.insert(bookID, startPrice, directPrice, bidProdStat, bidStart, bidEnd);

			/****** Send the success view ******/
			passMsgs.put("success", "新增成功!");
			System.out.println(passMsgs);
			RequestDispatcher success = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
			success.forward(req, res);
		}

		/****************************************
		 * Get_Update
		 ********************************************/

		if ("get_update".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/***** Get Request Parameter ******/
			Integer bidID = Integer.valueOf(req.getParameter("bidID"));

			/***** Start the Query *****/
			BidService sc = new BidService();
			BidProd bidProd = sc.showOneBid(bidID);

			/***** Send the success view *****/
			req.getSession().setAttribute("bidProd", bidProd);

			
			String url = "/back-end/bid/bidprod_back_update_page.jsp";

			RequestDispatcher success = req.getRequestDispatcher(url);
			success.forward(req, res);

		}
		
		/*******************************************
		 * Update
		 ***********************************************/
		if ("update".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<>();
			Map<String, String> passMsgs = new LinkedHashMap<>();

			req.setAttribute("errorMsgs", errorMsgs);
			req.setAttribute("passMsgs", passMsgs);

			/***** Error Handling ******/
			Integer bidID = Integer.valueOf(req.getParameter("bidID").trim());

			/* Check if the input is null and the format */
			String bookIDstr = req.getParameter("bookID");
			Integer bookID = null;

			if (bookIDstr == null || (bookIDstr.trim()).length() == 0) {
				errorMsgs.put("bookID", "請輸入書籍編號");
			} else {
				try {

					bookID = Integer.valueOf(bookIDstr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					errorMsgs.put("bookID", "書籍編號格式不正確");
				}

			}

			/****** Bid Start Price ******/
			/* Check if the input is null and the format */
			String startPricestr = req.getParameter("startPrice");
			Integer startPrice = null;
			if (startPricestr == null || (startPricestr.trim()).length() == 0) {
				errorMsgs.put("startPrice", "請輸入競標底價");
			} else {
				try {
					startPrice = Integer.valueOf(startPricestr);
					if (startPrice < 1)
						errorMsgs.put("startPrice", "競標底價不得低於1元");
				} catch (NumberFormatException e) {
					e.printStackTrace();
					errorMsgs.put("startPrice", "競標底價格式不正確");
				}
			}

			/****** Bid Direct Price ******/
			/* Check if the input is null and the format */
			String directPricestr = req.getParameter("bidDirectPrice");
			Integer directPrice = null;
			if (directPricestr == null || (directPricestr.trim()).length() == 0) {
				errorMsgs.put("bidDirectPrice", "請輸入直購價");
			} else {
				try {
					directPrice = Integer.valueOf(directPricestr);
					if (directPrice < 1)
						errorMsgs.put("bidDirectPrice", "競標直購價不得低於1元");
					if (startPrice != null && startPrice >= directPrice) {
						
							errorMsgs.put("startPrice", "直購價須高於競標底價，請重新輸入");
							errorMsgs.put("bidDirectPrice", "直購價須高於競標底價，請重新輸入");
					  
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					errorMsgs.put("bidDirectPrice", "直購價格式不正確");
				}
			}

			Timestamp bidStart = null;
			try {
				bidStart = Timestamp.valueOf(req.getParameter("bidStart").trim());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Timestamp bidEnd = null;
			Integer bidProdStat = 0;
			try {
				bidEnd = Timestamp.valueOf(req.getParameter("bidEnd").trim());
				if (bidStart != null) {
					bidProdStat = 1;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}


			if (!errorMsgs.isEmpty()) {
				RequestDispatcher fail = req.getRequestDispatcher("/back-end/bid/bidprod_back_update_page.jsp");
				fail.forward(req, res);
				return;
			}

			if (bidStart != null && bidEnd != null) {
				passMsgs.put("state", "競標已排程");
			} else {
				bidStart = null;
				bidEnd = null;
			}

			/****** Insert Data into Database ******/
			BidService sc = new BidService();
			sc.update(bidID, bookID, startPrice, directPrice, bidProdStat, bidStart, bidEnd);

			/****** Send the success view ******/
			passMsgs.put("success", "修改成功!");
			System.out.println(passMsgs);
			RequestDispatcher success = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
			success.forward(req, res);
		}
		
		
		/*******************************************
		 * Cancel Bid Product
		 ***********************************************/

		
		if ("cancel".equals(action)) {
			Integer bidID = Integer.valueOf(req.getParameter("bidID").trim());
			
			BidService sc = new BidService();
			sc.updateRevoke(bidID);

			RequestDispatcher success = req.getRequestDispatcher("/back-end/bid/bidprod_back_page.jsp");
			success.forward(req, res);
		}
		
		/****************************************
		 * Get_Bid Product
		 ********************************************/

		if ("get_record".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			Integer bidID = Integer.valueOf(req.getParameter("bidID"));

			BidService sc = new BidService();
			BidProd bidProd = sc.showOneBid(bidID);

			req.getSession().setAttribute("bidProd", bidProd);

			String url = "/back-end/bid/bidprod_back_show_record.jsp";

			RequestDispatcher success = req.getRequestDispatcher(url);
			success.forward(req, res);
		}

	}

}
