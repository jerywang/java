/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package aop;

/**
 * $Id UserManager.java Aug 11,2016 wangguoxing@baidu.com $
 */
public interface UserManager {

    public void addUser(String id, String password);

    public void delUser(String id);
}
