/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * $Id HttpServer.java Aug 19,2016 wangguoxing@baidu.com $
 */
public class HttpServer {
    private static final ExecutorService service = Executors.newFixedThreadPool(Constants.MAX_THREADS);
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer();
        //等待连接请求
        server.await();
    }

    public void await() throws IOException {
        //服务器套接字对象
        serverSocket = new ServerSocket(Constants.HTTP_PORT, 1, InetAddress.getByName("127.0.0.1"));
        // 循环等待请求
        while (!service.isShutdown()) {
            //等待连接，连接成功后，返回一个Socket对象
            final Socket socket = serverSocket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(socket);
                }
            };
            service.execute(task);
        }
    }

    private void handleRequest(Socket socket) {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            // 创建Request对象并解析
            HttpRequest request = new HttpRequest(input);
            request.parse();
            // 检查是否是关闭服务命令
            if (request.getUri().equals(Constants.SHUTDOWN_COMMAND)) {
                stop();
            }
            // 创建 Response 对象
            HttpResponse response = new HttpResponse(output);
            response.setRequest(request);

            if (request.getUri().startsWith("/servlet/")) {
                //请求uri以/servlet/开头，表示servlet请求
                HttpProcessor processor = new HttpProcessor();
                processor.process(request, response);
            } else {
                //静态资源请求
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request, response);
            }
            System.out.println("thread: " + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        service.shutdown();
        serverSocket.close();
    }
}
