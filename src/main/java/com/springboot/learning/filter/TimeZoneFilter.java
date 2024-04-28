package com.springboot.learning.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

//@Component
//@WebFilter("/*")
public class TimeZoneFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        // System.out.println("now:" + new Date().getTime() + ":" + System.currentTimeMillis());
        // response.setHeader("Cache-Control", "no-cache");
        // response.setDateHeader("Date", System.currentTimeMillis());
        // // response.setHeader("Time-Zone", TimeZone.getDefault().getID()); // 可选
        //
        // // 获取当前时间并转换为服务器时区时间
        // long currentTimeMillis = System.currentTimeMillis();
        // TimeZone serverTimeZone = TimeZone.getTimeZone("Asia/Shanghai"); // 替换为您的服务器时区
        // Date serverDate = new Date(currentTimeMillis + serverTimeZone.getRawOffset());
        //
        // // 设置响应头部 Date 字段为服务器时间
        // response.setDateHeader("Date", serverDate.getTime());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // 设置Date头部
        // response.setHeader("Date", dateFormat.format(calendar.getTime()));
        response.addHeader("Date", dateFormat.format(calendar.getTime()));

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化
    }

    @Override
    public void destroy() {
        // 销毁
    }
}