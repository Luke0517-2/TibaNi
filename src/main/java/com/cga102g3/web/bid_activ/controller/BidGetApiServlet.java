package com.cga102g3.web.bid_activ.controller;

import static com.cga102g3.core.util.CommonUtil.writePojo2Json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cga102g3.core.controller.BaseGetAPIServlet;
import com.cga102g3.core.pojo.ErrMsg;
import com.cga102g3.web.bid_activ.entity.BidActiv;
import com.cga102g3.web.bid_activ.entity.BidErrStat;
import com.cga102g3.web.bid_activ.entity.Bidder;
import com.cga102g3.web.bid_activ.service.BidGameService;
import com.cga102g3.web.bid_activ.service.impl.BidGameServiceImpl;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-25 下午 10:46
 */
@WebServlet(name = "BidGetApiServlet", urlPatterns = {"/bid/api/*"})
public class BidGetApiServlet extends BaseGetAPIServlet {
    private final BidGameService bidGameService = new BidGameServiceImpl();
    /**
     * 路由: /bid/api/getIdentify
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getIdentify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession();
        Integer mbrID = (Integer) session.getAttribute("mbrID");
        ErrMsg errMsg = new ErrMsg();
        if (mbrID == null) {
            errMsg.setMsg("生份未驗證");
            writePojo2Json(response, errMsg);
        }
        errMsg.setErr(false);
        writePojo2Json(response, errMsg);
    }

    /**
     * 路由: /bid/api/getMbrID?mbrID={int}
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getMbrID(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        Integer mbrID = null;
        try {
            mbrID = Integer.parseInt(request.getParameter("mbrID"));
            System.out.println(mbrID);
        } catch (NumberFormatException e) {
            ErrMsg errMsg = new ErrMsg();
            errMsg.setMsg("mbrID格式錯誤");
            writePojo2Json(response, errMsg);
            return;
        }
        HttpSession session = request.getSession();
        session.setAttribute("mbrID", mbrID);
    }

    /**
     * 路由: /bid/api/isValid4Bid?bidPrice={int}&bidID={int}
     * 檢視投標者是否可以投標
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isValid4Bid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession();
        int birPrice = -1;
        int bidID = -1;
        ErrMsg errMsg = new ErrMsg();
        try {
            birPrice = Integer.parseInt(request.getParameter("bidPrice"));
            bidID = Integer.parseInt(request.getParameter("bidID"));
        } catch (NumberFormatException e) {
            errMsg.setMsg("參數格式傳遞錯誤");
            writePojo2Json(response, errMsg);
            return;
        }

        Integer mbrID = (Integer) session.getAttribute("mbrID");
        Bidder bidder = new Bidder(mbrID, birPrice);

        verify(response, bidID, bidder, errMsg);
//        BidErrStat stat = bidGameService.isEnableRaise(bidder, bidID);
//
//        if (stat == BidErrStat.NoBidder) {
//            errMsg.setMsg(BidErrStat.NoBidder.getStat());
//            writePojo2Json(response, errMsg);
//            return;
//        }
//        if (stat == BidErrStat.LowerStartPrice) {
//            errMsg.setMsg(BidErrStat.LowerStartPrice.getStat());
//            writePojo2Json(response, errMsg);
//            return;
//        }
//
//        if (stat == BidErrStat.OverDirectPrice) {
//            errMsg.setMsg(BidErrStat.OverDirectPrice.getStat());
//            writePojo2Json(response, errMsg);
//            return;
//        }
//        if (stat == BidErrStat.NotWin) {
//            errMsg.setMsg(BidErrStat.NotWin.getStat());
//            writePojo2Json(response, errMsg);
//            return;
//        }
//        if (stat == BidErrStat.OverrideYourself) {
//            errMsg.setMsg(BidErrStat.OverrideYourself.getStat());
//            writePojo2Json(response, errMsg);
//            return;
//        }
//        if (stat == BidErrStat.UnAffordable) {
//            errMsg.setMsg(BidErrStat.UnAffordable.getStat());
//            writePojo2Json(response, errMsg);
//            return;
//        }
//
//        errMsg.setErr(false);
//        writePojo2Json(response, errMsg);
    }

    private void verify(HttpServletResponse response, Integer bidID, Bidder bidder, ErrMsg errMsg) {
        BidErrStat stat = bidGameService.isEnableRaise(bidder, bidID);
        if (stat == BidErrStat.NoBidder) {
            errMsg.setMsg(BidErrStat.NoBidder.getStat());
            writePojo2Json(response, errMsg);
            return;
        }
        if (stat == BidErrStat.LowerStartPrice) {
            errMsg.setMsg(BidErrStat.LowerStartPrice.getStat());
            writePojo2Json(response, errMsg);
            return;
        }

        if (stat == BidErrStat.OverDirectPrice) {
            errMsg.setMsg(BidErrStat.OverDirectPrice.getStat());
            writePojo2Json(response, errMsg);
            return;
        }
        if (stat == BidErrStat.NotWin) {
            errMsg.setMsg(BidErrStat.NotWin.getStat());
            writePojo2Json(response, errMsg);
            return;
        }
        if (stat == BidErrStat.OverrideYourself) {
            errMsg.setMsg(BidErrStat.OverrideYourself.getStat());
            writePojo2Json(response, errMsg);
            return;
        }
        if (stat == BidErrStat.UnAffordable) {
            errMsg.setMsg(BidErrStat.UnAffordable.getStat());
            writePojo2Json(response, errMsg);
            return;
        }

        errMsg.setErr(false);
        writePojo2Json(response, errMsg);
    }

    /**
     * 路由: /bid/api/isValid4TakeBid?bidID={int}
     * 檢視投標者是否可以直購
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isValid4TakeBid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession();
        int bidID = -1;
        ErrMsg errMsg = new ErrMsg();
        try {
            bidID = Integer.parseInt(request.getParameter("bidID"));
        } catch (NumberFormatException e) {
            errMsg.setMsg("參數格式傳遞錯誤");
            writePojo2Json(response, errMsg);
            return;
        }
        BidActiv bidActivity = bidGameService.getBidActivity(bidID);

        Integer mbrID = (Integer) session.getAttribute("mbrID");
        Bidder bidder = new Bidder(mbrID, bidActivity.getBidDirectPrice());
        verify(response, bidID, bidder, errMsg);
    }


    /**
     * /bid/api/getBidInfoListByKeyword?keyword={isbn, title}&page={int}
     * 書籍關鍵字查詢競標商品，若沒有輸入關鍵字，則返回書籍書目排序結果
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getBidInfoListByKeyword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        keyword = keyword.trim();
        int page = Integer.parseInt(request.getParameter("page"));
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> res = bidGameService.getBooksByKeyword(keyword, page);
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        System.out.println(res.get("data"));
        writePojo2Json(response, res);
    }

    /**
     * /bid/api/getAllBiddersByBidId?bidID={int}
     * 依據bidID回傳競標者出價金額，競標者出價資訊不重複
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getAllBiddersByBidId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        ErrMsg errMsg = new ErrMsg();
        Integer bidID = null;
        try {
            bidID = Integer.parseInt(request.getParameter("bidID"));
        } catch (Exception e) {
            e.printStackTrace();
            errMsg.setMsg("參數格式傳遞錯誤");
            writePojo2Json(response, errMsg);
            return;
        }
        List<Bidder> allBidders = null;
        allBidders = bidGameService.getAllBidders(bidID);
        writePojo2Json(response, allBidders);

    }

    /**
     * /bid/api/getAllBidInfo
     * 獲取正在上架中的競標商品資訊(不含目前競標價格，要知道目前價格請用getAllBiddersByBidId)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getAllBidInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> allBidInfo = bidGameService.getAllBidInfo();
        writePojo2Json(response, allBidInfo);
    }

}
