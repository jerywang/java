/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package aop;

/**
 * $Id UserManagerImpl.java Aug 11,2016 wangguoxing (554952580@qq.com) $
 */
public class UserManagerImpl implements UserManager {

    public void addUser(String id, String password) {
        System.out.println("掉用了UserManager.addUser()方法！ ");
    }

    public void delUser(String id) {
        System.out.println("掉用了UserManager.delUser()方法！ ");
    }
}
