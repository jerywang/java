/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangguoxing on 15-8-21.
 *
 * TCP/IP的NIO非阻塞方式
 */
public class Server {
    private static Charset charset = Charset.forName("utf-8");
    private static final int PORT = 5678;
    private ExecutorService pool = Executors.newFixedThreadPool(5);
    //选择器，主要用来监控各个通道的事件, 本实例server端只有1个通道
    private Selector selector;

    private Server() throws IOException {
        //创建选择器
        this.selector = Selector.open();
        // 打开服务器通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 服务器配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 检索与此通道关联的服务器套接字
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 进行服务的绑定
        serverSocket.bind(new InetSocketAddress(PORT));
        // 注册到selector，等待连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started...");
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.doService();
    }

    private void doService() throws IOException {
        while (true) {
            int keysCount;
            // 选择一组键，并且相应的通道已经打开
            if (selector.select() == 0) {
                continue;
            }
            // 返回此选择器的已选择键集
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    try {
                        // 接受到此通道套接字的连接。
                        // 此方法返回的套接字通道（如果有）将处于阻塞模式。
                        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                        // 配置为非阻塞
                        socketChannel.configureBlocking(false);
                        System.out.println("客户端:" + socketChannel.socket().getInetAddress().getHostAddress() + key.toString() + " "
                                + "已连接");
                        // 注册到selector，等待连接
                        SelectionKey k = socketChannel.register(selector, SelectionKey.OP_READ);
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        k.attach(buf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (key.isReadable()) {
                    key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                    pool.execute(new Worker(key));
                }
            }
        }
    }

    private static class Worker implements Runnable {
        private SelectionKey key;

        private Worker(SelectionKey key) {
            this.key = key;
        }

        @Override
        public void run() {
            // 返回为之创建此键的通道
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buf = (ByteBuffer) key.attachment();
            //将缓冲区清空以备下次读取
            buf.clear();
            int len = 0;
            try {
                //读取服务器发送来的数据到缓冲区中
                while ((len = socketChannel.read(buf)) > 0) {//非阻塞，立刻读取缓冲区可用字节
                    buf.flip();
                    System.out.println("客户端 " + key.toString() + ":" + charset.decode(buf).toString());
                    buf.clear();
                }
                if (len == -1) {
                    System.out.println("客户端断开 " + key.toString());
                    socketChannel.close();
                }
                //没有可用字节,继续监听OP_READ
                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                key.selector().wakeup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}