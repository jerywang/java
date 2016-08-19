/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package http.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * $Id HelloServlet.java Aug 19,2016 wangguoxing@baidu.com $
 */
public class HelloServlet implements Servlet {
    public void init(ServletConfig config) {
        System.out.println("init");
    }

    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        PrintWriter writer = res.getWriter();
        writer.println("Hello.");
    }

    public void destroy() {
        System.out.println("destroy");
    }

    public String getServletInfo() {
        return null;
    }

    public ServletConfig getServletConfig() {
        return null;
    }
}
