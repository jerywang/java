/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package http.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

/**
 * $Id HttpResponse.java Aug 19,2016 wangguoxing (554952580@qq.com) $
 */
public class HttpResponse implements ServletResponse {

    private static final int BUFFER_SIZE = 1024;
    private HttpRequest request;
    private OutputStream output;
    private PrintWriter writer;

    public HttpResponse(OutputStream output) {
        this.output = output;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(Constants.WEB_ROOT, request.getUri());
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            } else {
                String errorMessage =
                        "HTTP/1.1 404 File Not Found/r/n" + "Content-Type: text/html/r/n" + "Content-Length: 23/r/n"
                                + "/r/n" + " File Not Found";
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public String getCharacterEncoding() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public PrintWriter getWriter() throws IOException {
        writer = new PrintWriter(output, true);
        return writer;
    }

    public void setCharacterEncoding(String charset) {
    }

    public void setContentLength(int len) {
    }

    public void setContentType(String type) {
    }

    public void setBufferSize(int size) {
    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {
    }

    public void resetBuffer() {
    }

    public boolean isCommitted() {
        return true;
    }

    public void reset() {
    }

    public void setLocale(Locale loc) {
    }

    public Locale getLocale() {
        return null;
    }
}
