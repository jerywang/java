/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper.nameservice;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * $Id ServerWrapper.java Feb 27,2017 wangguoxing $
 */
public class ServerWrapper {

    private volatile List<String> serverList = Lists.newArrayList();

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList(List<String> serverList) {
        this.serverList = serverList;
    }
}
