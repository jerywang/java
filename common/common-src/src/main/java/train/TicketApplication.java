/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train;

import lombok.extern.log4j.Log4j;

/**
 * $Id TicketApplication.java Oct 13,2016 wangguoxing (554952580@qq.com) $
 */
@Log4j
public class TicketApplication {
    public static void main(String[] args) {
        new TicketApplication().start();
    }

    public void start() {
        new HttpClientService().start();
    }
}
