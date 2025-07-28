package com.store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"/customer/*", "/cart/*", "/order/*", "/product/add", "/product/bulkUpload"})
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Authentication Filter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        String requestURI = httpRequest.getRequestURI();
        
        if (isLoggedIn) {
            // User is logged in, proceed with request
            chain.doFilter(request, response);
        } else {
            // User is not logged in, redirect to login page
            logger.info("Unauthorized access attempt to: {}", requestURI);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?redirect=" + requestURI);
        }
    }
    
    @Override
    public void destroy() {
        logger.info("Authentication Filter destroyed");
    }
}