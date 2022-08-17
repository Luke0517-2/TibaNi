package com.cga102g3.core.linstener;
/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-22 下午 04:34
 */

import com.cga102g3.core.util.JedisPoolUtil;

import javax.servlet.*;
import javax.servlet.annotation.*;

public class ResourceListener implements ServletContextListener {

   

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        JedisPoolUtil.shutdownJedisPool();
    }


}
