package com.yassine.demoecommerce.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCoreFilter implements Filter {

    @Value("${app.client.url}")
    private String clientUrl = "";
    public SimpleCoreFilter() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response=(HttpServletResponse)res;
        HttpServletRequest request=(HttpServletRequest) req;
        Map<String,String> map=new HashMap<>();
        String originheader=request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin",originheader);
        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age","3600");
        response.setHeader("Access-Control-Allow-Headers","*");
        if("Options".equalsIgnoreCase(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
        filterChain.doFilter(req,res);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
