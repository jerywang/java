/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package http.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * $Id SimpleServer.java Aug 19,2016 wangguoxing@baidu.com $
 */
public class SimpleServer {
    public static final String WEB_ROOT = "./";
    private static final int MAX_THREADS = 50;
    private static final ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        while (true) {
            final Socket connection = serverSocket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection);
                }
            };
            service.execute(task);
            service.shutdown();
        }
    }

    private static void handleRequest(Socket connection) {
        try {
            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
            PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
            StringBuffer request = new StringBuffer();
            while (true) {
                char c = (char) in.read();
                if (c == '\r' || c == '\n') {
                    break;
                }
                request.append(c);
            }
            String str = request.toString() + "\nhello buddy!";
            printWriter.write(str);
            System.out.println(str);
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }
}
