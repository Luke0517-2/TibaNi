package com.cga102g3.web.bid_activ.listener;
/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-23 下午 04:42
 */

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cga102g3.web.bid_activ.service.BidActivitiesSchedulingService;
import com.cga102g3.web.bid_activ.service.impl.BidActivitiesSchedulingServiceImpl;

public class BidActivitiesSchedulingListener implements ServletContextListener {
    private Timer timer;
   

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        BidActivitiesSchedulingService service = new BidActivitiesSchedulingServiceImpl();
        // 建立競拍活動排程器
        this.timer = new Timer();
        GregorianCalendar cal = new GregorianCalendar(2022, Calendar.JULY, 13, 2, 40, 0);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 避免排程器補做事情
                if (scheduledExecutionTime() >= System.currentTimeMillis()) {
                    // 安排上架
                    service.launchProducts();
                    // 安排下架
                    service.discontinuedProducts();
                }
            }
        };

        // 每5分鐘執行一次
        timer.scheduleAtFixedRate(task, cal.getTime(), 1 * 60 * 1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        // 關閉競拍活動排程器
        while (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}
