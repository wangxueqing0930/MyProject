package com.wxq.example.interceptor;

import org.aspectj.weaver.ClassAnnotationValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by wxq on 2018/1/29.
 */
public class LogInterceptor implements HandlerInterceptor{

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*取得调用的controller方法等*/
        if(handler instanceof HandlerMethod){
            HandlerMethod hMethod = (HandlerMethod)handler;
            Class<?> hClass = hMethod.getBean().getClass();
            Method method = hMethod.getMethod();
            log.info("logInterceptor.preHandle()--Controller Name:"+hClass.getName()+"--method Name:"+method.getName());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(null!=modelAndView){
            System.out.println("logInterceptor.postHandle()---view Name:"+modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /*做一些清理工作*/
    }
}
