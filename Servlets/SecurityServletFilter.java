package com.company.base.accenture.movies.Servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter(urlPatterns = "/secured/*")
//public class SecurityServletFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter( ServletRequest servletRequest,
//                          ServletResponse servletResponse,
//                          FilterChain filterChain)
//            throws IOException, ServletException {
//        HttpServletRequest req =
//                (HttpServletRequest) servletRequest;
//        HttpSession session = req.getSession(false);
//        if(session != null) {
//            String name = (String)session.getAttribute("name");
//            if(name != null) {
//                filterChain.doFilter(
//                        servletRequest, servletResponse);
//            }
//        } else {
//            throw new ServletException("You shall not pass!");
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}

