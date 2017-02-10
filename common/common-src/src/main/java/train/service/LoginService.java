/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 *//*

package train.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.log4j.Log4j;
import train.constants.HttpHeader;
import train.constants.UrlConstants;
import train.util.DynamicJsUtil;
import train.vo.JsonMsg4Authcode;
import train.vo.JsonMsg4Login;

*/
/**
 * $Id LoginService.java Oct 13,2016 wangguoxing (554952580@qq.com) $
 *//*

@Log4j
public class LoginService {
    private String JSESSIONID = null;
    public boolean checkLoginAuthcode(String authCode) {
        if (log.isDebugEnabled()) {
            log.debug("---ajax post 检查验证码---");
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("randCode", authCode));
        params.add(new BasicNameValuePair("rand", "sjrand"));
        params.add(new BasicNameValuePair("randCode_validate", ""));

        HttpPost post = new HttpPost(UrlConstants.REQ_CHECK_CODE_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);

        boolean result = false;
        try {
            String checkResult = doPostRequest(post, params);
            JsonMsg4Authcode msg = JSONObject.parseObject(checkResult, JsonMsg4Authcode.class);
            if ("1".equals(msg.getData().getResult())) {
                result = true;
            }
        }
        catch (Exception e) {
            log.error("验证码检查发生异常", e);
        }

        return result;
    }

    */
/**
     * <登录检查>
     *
     * @return null代表成功，否则返回登录失败原因
     *//*

    public String checkLogin(String username, String password, String authcode) {
        if (log.isDebugEnabled()) {
            log.debug("---ajax post 登录检查---");
        }
        boolean checkCodeResult = checkLoginAuthcode(authcode);// 先检查验证码
        if (!checkCodeResult) {
            return "验证码不正确！";
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("loginUserDTO.user_name", username));
        params.add(new BasicNameValuePair("userDTO.password", password));
        params.add(new BasicNameValuePair("randCode", authcode));
        params.add(new BasicNameValuePair("randCode_validate", ""));
        if (loginKey != null) {
            params.add(new BasicNameValuePair(loginKey, DynamicJsUtil.getRandomParamValue(loginKey)));
        }
        else {
            log.trace("尚未生成登录key，请稍后...");
        }
        params.add(new BasicNameValuePair("myversion", "undefined"));

        HttpPost post = new HttpPost(UrlConstants.REQ_LOGIN_AYSN_SUGGEST_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);

        String result = null;
        try {
            String checkResult = doPostRequest(post, params);
            JsonMsg4Login msg = JSONObject.parseObject(checkResult, JsonMsg4Login.class);
            if (!msg.getStatus()) {
                result = msg.getMessages()[0];
            }
            else {
                String loginCheck = msg.getData().getString("loginCheck");
                if (loginCheck == null || !loginCheck.equals("Y")) {// 登录不成功
                    result = msg.getMessages()[0];
                }
                else {
                    login(); //登录
                }
            }
        }
        catch (Exception e) {
            result = "登录检查异常!";
            log.error("登录检查发生异常", e);
        }

        return result;
    }

    public void login() {
        if (log.isDebugEnabled()) {
            log.debug("---ajax post 登录---");
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("_json_att", ""));

        HttpPost post = new HttpPost(UrlConstants.REQ_LOGIN_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);

        try {
            doPostRequest(post, params);
        }
        catch (Exception e) {
            log.error("登录发生异常", e);
            return;
        }

        initMy12306();
    }

    private void initMy12306() {
        if (log.isDebugEnabled()) {
            log.debug("---ajax get 初始化我的12306---");
        }
        HttpGet get = new HttpGet(UrlConstants.GET_INIT_MY_12306_URL);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, null);

        try {
            doGetRequest(get);
        }
        catch (Exception e) {
            log.error("初始化我的12306异常", e);
        }
    }

    private void setCookie(HttpRequestBase request, Map<String, String> cookieMap) {
        request.setHeader("Cookie", getCookieStr(cookieMap));
    }

    private String getCookieStr(Map<String, String> cookieMap) {
        String cookie;
        if (cookieMap != null && cookieMap.size() > 0) {
            cookie = "JSESSIONID=" + JSESSIONID;
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                cookie += "; " + entry.getKey() + "=" + entry.getValue();
            }
            cookie += ";BIGipServerotn=" + BIGipServerotn;
            if (current_captcha_type != null) {
                cookie += ";current_captcha_type=" + current_captcha_type;
            }
        }
        else {
            cookie = "JSESSIONID=" + JSESSIONID + ";BIGipServerotn=" + BIGipServerotn;
            if (current_captcha_type != null) {
                cookie += ";current_captcha_type=" + current_captcha_type;
            }
        }
        log.debug("[request cooikes] {" + cookie + "}");
        return cookie;
    }
}
*/
