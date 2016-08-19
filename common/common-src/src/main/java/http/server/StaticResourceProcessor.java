/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package http.server;

import java.io.IOException;

/**
 * $Id StaticResourceProcessor.java Aug 19,2016 wangguoxing@baidu.com $
 */
public class StaticResourceProcessor {
    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
