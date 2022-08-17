package com.cga102g3.web.bid_activ.web_socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.cga102g3.web.bid_activ.entity.BidActiv;
import com.cga102g3.web.bid_activ.entity.BidActivityStat;
import com.cga102g3.web.bid_activ.entity.BidMsg4WebSocket;
import com.cga102g3.web.bid_activ.entity.Bidder;
import com.cga102g3.web.bid_activ.service.BidGameService;
import com.cga102g3.web.bid_activ.service.impl.BidGameServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-25 下午 03:39
 */
@ServerEndpoint(value="/BidActivity/{bidID}", configurator=ServletAwareConfig.class)
public class BidActivitiesWebSocket {
    private static final Set<Session> connectedSessions = Collections.synchronizedSet(new HashSet<>());
    private EndpointConfig config;
    private final BidGameService bidGameService = new BidGameServiceImpl();

    private final Gson gson = new GsonBuilder().create();;


    @OnOpen
    public void onOpen(@PathParam("bidID") Integer  bidID, Session userSession, EndpointConfig config) throws IOException {
        // 建立連線時執行，相當於servlet中的init
        connectedSessions.add(userSession);

        this.config = config;
        Bidder curWinner = bidGameService.getCurWinner(bidID);
        Map<String, Object> res = new HashMap<>();
        res.put("action", "init");
        // 只有
        res.put("traceBidID", bidID);

        res.put("bidder", curWinner);
        // 找出目前競拍的最高出價者編號與出價金額
        String resJson = gson.toJson(res);
        for (Session session : connectedSessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(resJson);
            }
        }
    }

    @OnMessage
    public void onMessage(Session userSession, String message) {
        // 後端收資料時執行，相當於servlet中的service
        // 取得HttpSession物件
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
//        ServletContext servletContext = httpSession.getServletContext();

        Integer mbrID = (Integer) httpSession.getAttribute("mbrID");
        // 獲得前端訊息
        System.out.println(message);
        BidMsg4WebSocket frontMsg = gson.fromJson(message, BidMsg4WebSocket.class);

        Bidder bidder = new Bidder(mbrID, frontMsg.getBidder().getPrice());
        Integer traceBidID = frontMsg.getTraceBidID();

        // 競標狀態處理結果
        BidActivityStat stat = null;
        // 回覆前端
        BidMsg4WebSocket res = null;
        // 用以得到直購價，因為出價等於直購價時，競標商品會下架，必須在這裡先得到原先競標商品
        BidActiv bidActivity = bidGameService.getBidActivity(traceBidID);

        synchronized (this) {
            if ("BidIncrement".equals(frontMsg.getAction()) || "DirectGet".equals(frontMsg.getAction())) {
                if ("DirectGet".equals(frontMsg.getAction())) {
                    bidder.setPrice(bidActivity.getBidDirectPrice());
                }
                stat = bidGameService.participate(bidder, frontMsg.getTraceBidID());
            }
        }

        if (stat == BidActivityStat.CONTINUED) {
            res = new BidMsg4WebSocket(BidActivityStat.CONTINUED.getStat(), bidder, traceBidID);
        } else if (stat == BidActivityStat.ENDGAME) {
            // 要處理回傳的金額，回覆為直購價格
            bidder.setPrice(bidActivity.getBidDirectPrice());
            res = new BidMsg4WebSocket(BidActivityStat.ENDGAME.getStat(), bidder, traceBidID);
        }

        // 推播消息
//        if (userSession != null && userSession.isOpen() && res != null) {
//            userSession.getAsyncRemote().sendText(gson.toJson(res));
//        }
        for (Session session : connectedSessions) {
            if (session.isOpen() && userSession != null && res != null) {
                session.getAsyncRemote().sendText(gson.toJson(res));
            }
        }

    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        // 連線關閉時執行，相當於servlet中的destroy
        System.out.println("close");
        connectedSessions.remove(userSession);
    }

    @OnError
    public void onError(Session userSession, Throwable e) {
        System.out.println("Error: " + e.toString());
    }
}
