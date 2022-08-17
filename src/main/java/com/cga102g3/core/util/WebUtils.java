package com.cga102g3.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @Description 請求參數封裝工具
 * @Author Robert
 * @Version
 * @Date 2022-06-16 下午 08:12
 */
public class WebUtils {
    public static <P> P copyParams2Bean(Map<String, String[]> reqGetParameterMap, P pojo) {
        try {
            BeanUtils.populate(pojo, reqGetParameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return pojo;
    }
}
