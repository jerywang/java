/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train.vo;

/**
 * $Id OrderToken.java Oct 14,2016 wangguoxing (554952580@qq.com) $
 */
public class OrderToken {

    private String token;

    private String key_check_isChange;

    public OrderToken(String token, String keyCheckIsChange) {
        super();
        this.token = token;
        key_check_isChange = keyCheckIsChange;
    }

    public String getKey_check_isChange() {
        return key_check_isChange;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "OrderToken [token=" + token + ", key_check_isChange=" + key_check_isChange + "]";
    }

}
