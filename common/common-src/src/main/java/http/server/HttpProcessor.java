/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package http.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;

/**
 * $Id HttpProcessor.java Aug 19,2016 wangguoxing@baidu.com $
 */
public class HttpProcessor {

    public void process(HttpRequest request, HttpResponse response) {
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        // 类加载器，用于从指定JAR文件或目录加载类
        URLClassLoader loader = null;
        try {
            URLStreamHandler streamHandler = null;
            URL[] urls = new URL[1];
            File classPath = new File(Constants.WEB_ROOT);
            String responitory = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, responitory, streamHandler);
            // 创建类加载器
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        Class myClass = null;
        try {
            // 加载对应的servlet类
            myClass = loader.loadClass("http.server."+servletName);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        Servlet servlet = null;

        try {
            // 生产servlet实例
            servlet = (Servlet) myClass.newInstance();
            // 执行ervlet的service方法
            servlet.service(request, response);
        } catch (Exception e) {
            System.out.println(e.toString());
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
    }
}
