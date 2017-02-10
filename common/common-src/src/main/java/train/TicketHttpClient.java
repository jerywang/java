/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.log4j.Log4j;
import train.constants.HttpHeader;
import train.constants.UrlConstants;
import train.util.DateUtil;
import train.util.DynamicJsUtil;
import train.vo.ContacterInfo;
import train.vo.JsonMsg4Authcode;
import train.vo.JsonMsg4CheckOrder;
import train.vo.JsonMsg4ConfirmQueue;
import train.vo.JsonMsg4Contacter;
import train.vo.JsonMsg4LeftTicket;
import train.vo.JsonMsg4Login;
import train.vo.JsonMsg4QueryWait;
import train.vo.JsonMsg4QueueCount;
import train.vo.JsonMsgSuper;
import train.vo.OrderToken;
import train.vo.PassengerData;
import train.vo.TicketConfigInfo;
import train.vo.TrainInfo;
import util.JsonUtil;

/**
 * $Id TicketHttpClient.java Oct 13,2016 wangguoxing (554952580@qq.com) $
 */
@Log4j
public class TicketHttpClient {

    private String JSESSIONID = null;

    private String BIGipServerotn = null;

    private String current_captcha_type = null;

    private String loginKey = null;

    private String orderTicketKey = null;

    private String checkOrderKey = null;

    private static final int DEBBUG_MAX_COUNT = 1000;

    private String query_url = UrlConstants.REQ_TICKET_QUERY_URL;

    private static final Pattern PATTERN_DYNAMIC_JS = Pattern.compile("/otn/dynamicJs/(\\w+)");

    private static final Pattern PATTERN_LOGIN_KEY = Pattern.compile("var key='(\\w+)'");

    private static final Pattern PATTERN_TOKEN = Pattern.compile("var globalRepeatSubmitToken = '(\\w+)'");

    private static final Pattern PATTERN_KEY_CHECK = Pattern.compile("'key_check_isChange':'(\\w+)'");

    private OrderToken orderToken;

    /*
     * private static final String loginAuthCodeFilePath =
     * System.getProperty("user.dir") + File.separator + "login_authcode.jpg";
     */

    /* loginUrl = path + "image" + File.separator + "passcode-login.jpg"; */

    private TicketHttpClient(String JSESSIONID, String BIGipServerotn) {
        this.JSESSIONID = JSESSIONID;
        this.BIGipServerotn = BIGipServerotn;
    }

    /**
     * 初始获取cookie
     *
     * @return TicketHttpClient
     */
    public static TicketHttpClient getInstance() {
        log.info("初始获取cookie /login/init");
        TicketHttpClient instance = null;
        HttpClient httpclient = buildHttpClient();
        HttpGet get = new HttpGet(UrlConstants.GET_LOGIN_INIT_URL);
        HttpHeader.setLoginInitHeader(get);

        String JSESSIONID = null;
        String BIGipServerotn = null;
        InputStream is = null;
        String loginDynamicJs = null;
        try {
            HttpResponse response = httpclient.execute(get);
            Header[] headers = response.getAllHeaders();
            boolean isGzip = false;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Set-Cookie")) {
                    String cookie[] = headers[i].getValue().split("=");
                    String cookieName = cookie[0];
                    String cookieValue = cookie[1].split(";")[0];
                    if (cookieName.equals("JSESSIONID")) {
                        JSESSIONID = cookieValue;
                    }
                    if (cookieName.equals("BIGipServerotn")) {
                        BIGipServerotn = cookieValue;
                    }
                } else if ("Content-Encoding".equals(headers[i].getName()) && "gzip".equals(headers[i].getValue())) {
                    isGzip = true;
                }
            }

            if (JSESSIONID != null && BIGipServerotn != null) {
                is = response.getEntity().getContent();
                String responseBody;
                if (isGzip) {
                    responseBody = zipInputStream(is);
                } else {
                    responseBody = readInputStream(is);
                }

                Matcher m_token = PATTERN_DYNAMIC_JS.matcher(responseBody);
                if (m_token.find()) {
                    loginDynamicJs = m_token.group(1);
                } else {
                    log.error("httpClient init get loginDynamicJsUrl  fail for unknow reason, check it!");
                }
            }
            log.info("JSESSIONID = " + JSESSIONID + ";BIGipServerotn = " + BIGipServerotn);
        } catch (Exception e) {
            log.error("getInstance error : ", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        if (JSESSIONID != null && BIGipServerotn != null) {
            instance = new TicketHttpClient(JSESSIONID, BIGipServerotn);
            if (loginDynamicJs != null) {
                instance.loginKey = instance.getRandomParamKey(loginDynamicJs);
            }
        }

        return instance;
    }

    /**
     * 获取动态参数
     *
     * @param jsFileName
     *
     * @return
     */
    private String getRandomParamKey(String jsFileName) {
        log.debug("---ajax get 获取dynamicJs的内容，从中提取出key---");
        String dynamicJsUrl = UrlConstants.GET_DYNAMIC_JS_URL + jsFileName;
        HttpGet get = new HttpGet(dynamicJsUrl);
        HttpHeader.setCommonHeader(get);
        setCookie(get, null);
        String key = null;
        try {
            String result = doGetRequest(get);
            clearKeyTimmer(result);
            Matcher m_token = PATTERN_LOGIN_KEY.matcher(result);
            if (m_token.find()) {
                key = m_token.group(1);
                System.out.println("key " + key);
            }
        } catch (Exception e) {
            log.error("获取key出错", e);
        }

        return key;
    }

    /**
     * 清除key失效时间
     *
     * @param jsStr
     */
    private void clearKeyTimmer(String jsStr) {
        log.debug("---ajax post 清除key失效时间---");
        Matcher m_token = PATTERN_DYNAMIC_JS.matcher(jsStr);
        String jsFileName = null;
        if (m_token.find()) {
            jsFileName = m_token.group(1);
        }
        if (jsFileName == null) {
            log.error("清除key失效时间出错");
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("_json_att", ""));

        String dynamicJsUrl = "https://kyfw.12306.cn/otn/dynamicJs/" + jsFileName;
        HttpPost post = new HttpPost(dynamicJsUrl);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);

        try {
            doPostRequest(post, params);
        } catch (Exception e) {
            log.error("清除key失效时间出错", e);
        }
    }

    public File buildLoginCodeImage() {
        log.info("获取验证码 [get url] {" + UrlConstants.GET_LOGIN_PASSCODE_URL + "}");
        HttpClient httpclient = buildHttpClient();
        HttpGet get = new HttpGet(UrlConstants.GET_LOGIN_PASSCODE_URL);
        HttpHeader.setLoginAuthCodeHeader(get);
        setCookie(get, null);

        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + JSESSIONID + ".login.jpg");
        OutputStream out = null;
        InputStream is = null;
        try {
            HttpResponse response = httpclient.execute(get);
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Set-Cookie")) {
                    String cookie[] = headers[i].getValue().split("=");
                    String cookieName = cookie[0];
                    String cookieValue = cookie[1].split(";")[0];
                    if (cookieName.equals("current_captcha_type")) {
                        current_captcha_type = cookieValue;
                    }
                }
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                out = new FileOutputStream(file);
                int readByteCount = -1;
                byte[] buffer = new byte[256];
                while ((readByteCount = is.read(buffer)) != -1) {
                    out.write(buffer, 0, readByteCount);
                }
            }
        } catch (Exception e) {
            file = null;
            log.error("buildLoginCodeImage error : ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            httpclient.getConnectionManager().shutdown();
        }
        return file;
    }

    public boolean checkLoginAuthcode(String authCode) {
        log.info("---ajax post 检查验证码---");
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
        } catch (Exception e) {
            log.error("验证码检查发生异常", e);
        }

        return result;
    }

    public boolean checkOrderCode(String orderCode, String token, Map<String, String> cookieMap) {
        log.info("---ajax post 检查订单验证码---");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("randCode", orderCode));
        params.add(new BasicNameValuePair("rand", "randp"));
        params.add(new BasicNameValuePair("_json_att", ""));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));
        log.info("ajax post 检查订单验证码params: " + JsonUtil.toJson(params));
        HttpPost post = new HttpPost(UrlConstants.REQ_CHECK_CODE_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, cookieMap);

        boolean result = false;
        try {
            String checkResult = doPostRequest(post, params);
            JsonMsg4Authcode msg = JSONObject.parseObject(checkResult, JsonMsg4Authcode.class);
            if ("1".equals(msg.getData().getResult())) {
                result = true;
            }
        } catch (Exception e) {
            log.error("验证码检查发生异常", e);
        }

        return result;
    }

    /**
     * <登录检查>
     *
     * @return null代表成功，否则返回登录失败原因
     */
    public String checkLogin(String username, String password, String authcode) {
        log.debug("---ajax post 登录检查---");
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
        } else {
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
            } else {
                String loginCheck = msg.getData().getString("loginCheck");
                if (loginCheck == null || !loginCheck.equals("Y")) {// 登录不成功
                    result = msg.getMessages()[0];
                } else {
                    login(); //登录
                }
            }
        } catch (Exception e) {
            result = "登录检查异常!";
            log.error("登录检查发生异常", e);
        }

        return result;
    }

    public void login() {
        log.debug("---ajax post 登录---");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("_json_att", ""));

        HttpPost post = new HttpPost(UrlConstants.REQ_LOGIN_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);

        try {
            doPostRequest(post, params);
        } catch (Exception e) {
            log.error("登录发生异常", e);
            return;
        }

        initMy12306();
    }

    private void initMy12306() {
        log.debug("---ajax get 初始化我的12306---");
        HttpGet get = new HttpGet(UrlConstants.GET_INIT_MY_12306_URL);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, null);

        try {
            doGetRequest(get);
        } catch (Exception e) {
            log.error("初始化我的12306异常", e);
        }
    }

    public ContacterInfo[] getPassengers() {
        log.info("---ajax post 获取联系人---");
        HttpPost post = new HttpPost(UrlConstants.REQ_PASSENGERS_QUERY_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);

        ContacterInfo[] result = null;
        try {
            String checkResult = doPostRequest(post, null);
            JsonMsg4Contacter msg = JSONObject.parseObject(checkResult, JsonMsg4Contacter.class);
            if (msg.getStatus()) {
                if (!msg.getData().isExist() && msg.getData().getExMsg() != null) {
                    log.error(msg.getData().getExMsg());
                } else {
                    result = msg.getData().getNormal_passengers();
                }
            }
        } catch (Exception e) {
            log.error("获取联系人异常", e);
        }

        return result;
    }

    public String getOrderNoPay() {
        HttpPost post = new HttpPost(UrlConstants.ORDER_NO_PAY);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);
        String responseBody = null;
        try {
            responseBody = doPostRequest(post, null);
        } catch (Exception e) {
            log.error("获取未支付订单失败", e);
        }

        return responseBody;
    }

        /**
         * <查询余票信息>
         * @param configInfo
         * @param cookieMap
         * @return
         */
        public List<TrainInfo> queryLeftTicket(TicketConfigInfo configInfo, Map<String, String> cookieMap) {
            if (log.isDebugEnabled()) {
                log.debug("---ajax get 查询余票信息---");
            }
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("leftTicketDTO.train_date", configInfo.getTrain_date()));
            params.add(new BasicNameValuePair("leftTicketDTO.from_station", configInfo.getFrom_station()));
            params.add(new BasicNameValuePair("leftTicketDTO.to_station", configInfo.getTo_station()));
            params.add(new BasicNameValuePair("purpose_codes", configInfo.getPurpose_codes()));
            String paramsUrl = URLEncodedUtils.format(params, "UTF-8");

            if ("A".equals(current_captcha_type) || "C".equals(current_captcha_type)) {//验证码为A类型时，查询前先调用LOG请示
                queryLog(paramsUrl, cookieMap);
            }
            HttpGet get = new HttpGet(query_url + "?" + paramsUrl);
            HttpHeader.setGetAjaxHeader(get);
            setCookie(get, cookieMap);

            List<TrainInfo> result = null;
            try {
                String checkResult = doGetRequest(get);
                JsonMsg4LeftTicket msg = JSONObject.parseObject(checkResult, JsonMsg4LeftTicket.class);
                if (msg.getStatus()) {
                    List<JsonMsg4LeftTicket.TrainQueryInfo> infos = msg.getData();
                    if (infos != null) {
                        result = new ArrayList<TrainInfo>();
                        TrainInfo trainInfo;
                        for (JsonMsg4LeftTicket.TrainQueryInfo info : infos) {
                            trainInfo = info.getQueryLeftNewDTO();
                            trainInfo.setSecretStr(info.getSecretStr());
                            result.add(trainInfo);
                        }
                    }
                    else {
                        String[] tips = msg.getMessages();
                        if (tips != null && tips.length > 0) {
                            if (tips[0].contains("非法")) {
                                log.error("查询余票时出现错误：" + tips[0]);
                            }
                        }
                    }
                }
                else {
                    if (StringUtils.isNotEmpty(msg.getC_url())) {//切换地址了
                        query_url = UrlConstants.GET_BASE_URL + msg.getC_url();
                    }
                }
            }
            catch (JSONException e) {
                query_url = UrlConstants.REQ_TICKET_QUERY_URL2;
                log.error("查询余票异常，内容异常", e);
            }
            catch (Exception e) {
                log.error("查询余票异常", e);
            }

            return result;
        }

    /**
     * <查询余票前先进行LOG调用>
     * @param paramsUrl
     * @param cookieMap
     */
    public void queryLog(String paramsUrl, Map<String, String> cookieMap) {
        log.debug("---ajax get 查询余票前先LOG---");

        HttpGet get = new HttpGet(UrlConstants.GET_TICKET_QUERY_LOG_URL + "?" + paramsUrl);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, cookieMap);

        try {
            String checkResult = doGetRequest(get);
            JsonMsgSuper msg = JSONObject.parseObject(checkResult, JsonMsgSuper.class);
            if (msg.getStatus()) {

            }
        }
        catch (Exception e) {
            log.error("查询余票前LOG异常", e);
        }

    }

    /**
     * 订单提交前获取动态参数
     *
     * @param cookieMap
     */
    public void leftTicketInit(Map<String, String> cookieMap) {
        if (log.isDebugEnabled()) {
            log.debug("---ajax get 获取订单页dynamicJs的内容，从中提取出订票key---");
        }

        HttpGet get = new HttpGet(UrlConstants.GET_LEFT_TICKET_INIT_URL);
        HttpHeader.setLoginInitHeader(get);
        setCookie(get, cookieMap);

        String leftTicketDynamicJs = null;
        try {
            String result = doGetRequest(get);

            Matcher m_token = PATTERN_DYNAMIC_JS.matcher(result);
            if (m_token.find()) {
                leftTicketDynamicJs = m_token.group(1);
            } else {
                log.error("httpClient init get leftTicketDynamicJsUrl  fail for unknow reason, check it!");
            }
        } catch (Exception e) {
            log.error("leftTicketInit error : ", e);
        }

        if (leftTicketDynamicJs != null) {
            orderTicketKey = getRandomParamKey(leftTicketDynamicJs);
        }
    }

    /**
     * 订单提交前获取动态参数
     * @todo 动态获取
     */
    public String queryC() {
        log.info("queryC: https://kyfw.12306.cn/otn/leftTicket/queryC");
        //        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //        params.add(new BasicNameValuePair("leftTicketDTO.train_date", "2016-10-25"));
        //        params.add(new BasicNameValuePair("leftTicketDTO.from_station", "SHH"));
        //        params.add(new BasicNameValuePair("leftTicketDTO.to_station", "ZMP"));
        //        params.add(new BasicNameValuePair("leftTicketDTO.train_date", "ADULT"));
        HttpGet get = new HttpGet(
                "https://kyfw.12306.cn/otn/leftTicket/queryC?leftTicketDTO.train_date=2016-10-25&leftTicketDTO"
                        + ".from_station=SHH&leftTicketDTO.to_station=ZMP&purpose_codes=ADULT");
        HttpHeader.setGetAjaxHeader(get);
        String response = null;
        try {
            response = doGetRequest(get);
        } catch (Exception e) {
            log.error("request /leftTicket/queryC failed", e);
        }
        return response;
    }

    /**
     * 判断用户是否已登录
     * @return boolean
     */
    public boolean checkUserLogin() {
        log.debug("---ajax post 检查是否登录---");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("_json_att", ""));

        HttpPost post = new HttpPost(UrlConstants.REQ_CHECK_USER_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);

        boolean result = false;
        try {
            String checkResult = doPostRequest(post, params);
            JsonMsg4Login msg = JSONObject.parseObject(checkResult, JsonMsg4Login.class);
            if (!msg.getStatus()) {
                result = false;
                System.out.println(msg.getMessages()[0]);
            }
            else {
                result = msg.getData().getBoolean("flag");
            }
        }
        catch (Exception e) {
            log.error("检查是否登录发生异常", e);
        }

        return result;
    }

    /**
     * 选择某个车次进入预订页前检验
     *
     * @param configInfo TicketConfigInfo
     * @param cookieMap Map
     * @param train TrainInfo
     *
     * @return String
     */
    public String submitOrderRequest(TicketConfigInfo configInfo, Map<String, String> cookieMap, TrainInfo train) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (orderTicketKey != null) {
            params.add(new BasicNameValuePair(orderTicketKey, DynamicJsUtil.getRandomParamValue(orderTicketKey)));
        }
        params.add(new BasicNameValuePair("myversion", "undefined"));
        try {
            params.add(new BasicNameValuePair("secretStr", URLDecoder.decode(train.getSecretStr(), "UTF-8")));
        } catch (UnsupportedEncodingException e1) {
            log.error("进入预订页参数编码错误", e1);
            return "预订页参数编码异常，请通知管理员!";
        }
        //        params.add(new BasicNameValuePair("secretStr", train.getSecretStr()));
        params.add(new BasicNameValuePair("train_date", configInfo.getTrain_date()));
        params.add(new BasicNameValuePair("back_train_date", DateUtils.formatDate(new Date())));
        params.add(new BasicNameValuePair("tour_flag", "dc"));//单程票标识
        params.add(new BasicNameValuePair("purpose_codes", configInfo.getPurpose_codes()));
        params.add(new BasicNameValuePair("query_from_station_name", configInfo.getFrom_station_name()));
        params.add(new BasicNameValuePair("query_to_station_name", configInfo.getTo_station_name()));
        params.add(new BasicNameValuePair("undefined", ""));

        HttpPost post = new HttpPost(UrlConstants.REQ_SUBMIT_ORDER_URL);
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, cookieMap);

        String checkResult = null;
        try {
            checkResult = doPostRequest(post, params);

            int reConnCount = 0;
            while (reConnCount < 3 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    log.error(e);
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
            this.orderToken = getOrderToken(postInitDcPage(cookieMap));
        } catch (Exception e) {
            log.error("进入预订页面校验失败", e);
        }

        return checkResult;
    }

    public OrderToken getOrderToken() {
        return orderToken;
    }

    /**
     * <选择某个车次进入预订页>
     * @param cookieMap
     * @return
     */
    public String postInitDcPage(Map<String, String> cookieMap) {
        log.debug("---ajax post 进入单程票预订页---");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("_json_att", ""));

        HttpPost post = new HttpPost(UrlConstants.REQ_INITDC_URL);
        HttpHeader.setInitDcHeader(post);
        setCookie(post, cookieMap);

        String result = null;
        String checkOrderDynamicJs = null;
        try {
            result = doPostRequest(post, params);

            // 获取订单确认key
            Matcher m_token = PATTERN_DYNAMIC_JS.matcher(result);
            if (m_token.find()) {
                checkOrderDynamicJs = m_token.group(1);
            }
        }
        catch (Exception e) {
            log.error("进入单程票预订页", e);
        }

        if (checkOrderDynamicJs != null) {
            checkOrderKey = getRandomParamKey(checkOrderDynamicJs);
        }
        return result;
    }

    /**
     * <选择某个车次进入预订页>
     *
     * @return
     */
    public String getInitDcPage(Map<String, String> cookieMap) {
        log.info("---进入单程票预订页---");
        HttpGet get = new HttpGet(UrlConstants.REQ_INITDC_URL);
        HttpHeader.setCheckOrderHeader(get);
        setCookie(get, cookieMap);
        String result = null;
        String checkOrderDynamicJs = null;
        try {
            result = doGetRequest(get);

            // 获取订单确认key
            Matcher m_token = PATTERN_DYNAMIC_JS.matcher(result);
            if (m_token.find()) {
                checkOrderDynamicJs = m_token.group(1);
            }
        } catch (Exception e) {
            log.error("进入单程票预订页", e);
        }

        if (checkOrderDynamicJs != null) {
            checkOrderKey = getRandomParamKey(checkOrderDynamicJs);
        }

        return result;
    }

    private OrderToken getOrderToken(String body) {
        OrderToken token = null;
        int sum = 0;
        while (token == null && sum < 3) {
            try {
                String token_str = null;
                String key_check_isChange_str = null;

                Matcher m_token = PATTERN_TOKEN.matcher(body);
                Matcher m_key_check_isChange = PATTERN_KEY_CHECK.matcher(body);
                if (m_token.find()) {
                    token_str = m_token.group(1);
                }
                if (m_key_check_isChange.find()) {
                    key_check_isChange_str = m_key_check_isChange.group(1);
                }
                if (token_str != null && key_check_isChange_str != null) {
                    token = new OrderToken(token_str, key_check_isChange_str);
                } else {
                    log.error("initDc get token fail for unknow reason, check it!");
                }
            } catch (Exception e) {
                log.error("initDC page error : ", e);
            }
            sum++;
        }
        log.info("order token: " + JsonUtil.toJson(token));
        return token;
    }

    public File buildOrderCodeImage(Map<String, String> cookies) {
        log.info("---ajax get 获取提交订单验证码---");
        log.info("[get url] {" + UrlConstants.GET_ORDER_PASSCODE_URL + "}");
        HttpClient httpclient = buildHttpClient();
        HttpGet get = new HttpGet(UrlConstants.GET_ORDER_PASSCODE_URL);
        HttpHeader.setLoginAuthCodeHeader(get);
        setCookie(get, cookies);

        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + JSESSIONID + ".order.jpg");
        OutputStream out = null;
        InputStream is = null;
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                out = new FileOutputStream(file);
                int readByteCount = -1;
                byte[] buffer = new byte[256];
                while ((readByteCount = is.read(buffer)) != -1) {
                    out.write(buffer, 0, readByteCount);
                }
            }
        } catch (Exception e) {
            file = null;
            log.error("buildOrderCodeImage error : ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
            httpclient.getConnectionManager().shutdown();
        }
        return file;
    }

    public JsonMsg4CheckOrder checkOrderInfo(String authCode, String token, List<PassengerData> passengers,
                                             Map<String, String> cookieMap) {
        log.info("---ajax post 检查下单验证码---");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("bed_level_order_num", "000000000000000000000000000000"));
        params.add(new BasicNameValuePair("cancel_flag", "2"));
        params.add(new BasicNameValuePair("passengerTicketStr", getPassengerTicketStr(passengers)));
        params.add(new BasicNameValuePair("oldPassengerStr", getOldPassengerStr(passengers)));
        params.add(new BasicNameValuePair("randCode", authCode));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));
        params.add(new BasicNameValuePair("tour_flag", "dc"));
        params.add(new BasicNameValuePair("_json_att", ""));
        if (checkOrderKey != null) {
            params.add(new BasicNameValuePair(checkOrderKey, DynamicJsUtil.getRandomParamValue(checkOrderKey)));
        }
        HttpPost post = new HttpPost(UrlConstants.REQ_CHECK_ORDER_URL);
        HttpHeader.setPostAjaxHeader(post);
        post.setHeader("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");
        setCookie(post, cookieMap);
        log.info("checkOrderInfo header: " + JsonUtil.toJson(post.getAllHeaders()));
        log.info("ajax post 检查下单 param: " + JsonUtil.toJson(params));
        JsonMsg4CheckOrder result = null;
        try {
            String checkResult = doPostRequest(post, params);
            int reConnCount = 0;
            while (reConnCount < 3 && StringUtils.isEmpty(checkResult)) {
                log.info("----reCheckOrderInfo-----");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    log.error(e);
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            result = JSONObject.parseObject(checkResult, JsonMsg4CheckOrder.class);
        } catch (Exception e) {
            log.error("检查下单验证码发生异常", e);
        }

        return result;
    }

    public JsonMsg4QueueCount getQueueCount(TrainInfo train, Date trianDate, String seatType, String token,
                                            Map<String, String> cookieMap) {
        if (log.isDebugEnabled()) {
            log.debug("---ajax post 获取排队人数---");
        }
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("purpose_codes", "00"));
        params.add(new BasicNameValuePair("seatType", seatType));
        params.add(new BasicNameValuePair("train_no", train.getTrain_no()));
        params.add(new BasicNameValuePair("fromStationTelecode", train.getFrom_station_telecode()));
        params.add(new BasicNameValuePair("stationTrainCode", train.getStation_train_code()));
        params.add(new BasicNameValuePair("toStationTelecode", train.getTo_station_telecode()));
        params.add(new BasicNameValuePair("leftTicket", train.getYp_info()));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));
        //Sun Feb 012015 00:00:00 GMT+0800
        params.add(new BasicNameValuePair("train_date", df.format(trianDate) + " 00:00:00 GMT+0800"));
        params.add(new BasicNameValuePair("_json_att", ""));
        log.info("ajax post 获取排队人数 param: " + JsonUtil.toJson(params));
        HttpPost post = new HttpPost(UrlConstants.REQ_QUEUE_COUNT_URL);
        HttpHeader.setPostAjaxHeader(post);
        post.setHeader("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");
        setCookie(post, cookieMap);

        JsonMsg4QueueCount result = null;
        try {
            String checkResult = doPostRequest(post, params);
            int reConnCount = 0;
            while (reConnCount < 2 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            result = JSONObject.parseObject(checkResult, JsonMsg4QueueCount.class);
        } catch (Exception e) {
            log.error("获取排队人数发生异常", e);
        }
        return result;
    }

    public JsonMsg4ConfirmQueue confirmQueue(TrainInfo train, OrderToken token, List<PassengerData> passengers,
                                             String authcode, Map<String, String> cookieMap) {
        log.info("---ajax post 确认排队购买---");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("purpose_codes", "00"));
        params.add(new BasicNameValuePair("key_check_isChange", token.getKey_check_isChange()));
        params.add(new BasicNameValuePair("passengerTicketStr", getPassengerTicketStr(passengers)));
        params.add(new BasicNameValuePair("oldPassengerStr", getOldPassengerStr(passengers)));
        params.add(new BasicNameValuePair("randCode", authcode));
        params.add(new BasicNameValuePair("leftTicketStr", train.getYp_info()));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token.getToken()));
        params.add(new BasicNameValuePair("train_location", train.getLocation_code()));
        params.add(new BasicNameValuePair("_json_att", ""));
        log.info("ajax post 确认排队购买 param: " + JsonUtil.toJson(params));
        HttpPost post = new HttpPost(UrlConstants.REQ_CONFIRM_SINGLE_URL);
        HttpHeader.setPostAjaxHeader(post);
        post.setHeader("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");
        setCookie(post, cookieMap);

        JsonMsg4ConfirmQueue result = null;
        try {
            String checkResult = doPostRequest(post, params);
            int reConnCount = 0;
            while (reConnCount < 2 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            result = JSONObject.parseObject(checkResult, JsonMsg4ConfirmQueue.class);
        } catch (Exception e) {
            log.error("确认排队购买发生异常", e);
        }
        return result;
    }

    /**
     * <查询余票信息>
     */
    public JsonMsg4QueryWait queryOrderWaitTime(String token, Map<String, String> cookieMap) {
        log.info("---ajax get 查询排队等待信息---");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("random", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("tourFlag", "dc"));
        params.add(new BasicNameValuePair("_json_att", ""));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));
        String paramsUrl = URLEncodedUtils.format(params, "UTF-8");

        HttpGet get = new HttpGet(UrlConstants.GET_QUERY_ORDER_WAIT_URL + "?" + paramsUrl);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, null);

        JsonMsg4QueryWait result = null;
        try {
            String checkResult = doGetRequest(get);
            result = JSONObject.parseObject(checkResult, JsonMsg4QueryWait.class);
        } catch (Exception e) {
            log.error("查询排队等待信息异常", e);
        }
        return result;
    }

    public JsonMsg4ConfirmQueue resultOrderForDcQueue(String orderNo, String token) {
        log.info("---ajax post 确认排队购买---");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("orderSequence_no", orderNo));
        params.add(new BasicNameValuePair("_json_att", ""));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));

        HttpPost post = new HttpPost(UrlConstants.REQ_CONFIRM_SINGLE_URL);
        HttpHeader.setPostAjaxHeader(post);
        post.setHeader("Referer", "https://kyfw.12306.cn/otn/confirmPassenger/initDc");
        setCookie(post, null);

        JsonMsg4ConfirmQueue result = null;
        try {
            String checkResult = doPostRequest(post, params);
            int reConnCount = 0;
            while (reConnCount < 3 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            result = JSONObject.parseObject(checkResult, JsonMsg4ConfirmQueue.class);
        } catch (Exception e) {
            log.error("确认排队购买发生异常", e);
        }
        return result;
    }

    /**
     * <退出>
     */
    public void loginOut() {
        if (log.isDebugEnabled()) {
            log.debug("---post 登录退出---");
        }
        HttpGet get = new HttpGet(UrlConstants.GET_LOGOUT_URL);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, null);

        try {
            doGetRequest(get);
        } catch (Exception e) {
            log.error("退出发生异常", e);
        }
    }

    private static X509TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    private static HttpClient buildHttpClient() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            log.error("getHttpClient error for NoSuchAlgorithmException", e);
        }

        try {
            sslcontext.init(null, new TrustManager[]
                    {tm}, null);
        } catch (KeyManagementException e) {
            log.error("getHttpClient error for KeyManagementException", e);
        }
        SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
        ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        HttpClient httpclient = new DefaultHttpClient(ccm, params);
        return httpclient;
    }

    /**
     * post请求
     *
     * @param request HttpPost
     * @param params  List<NameValuePair>
     *
     * @return String
     */
    private String doPostRequest(HttpPost request, List<NameValuePair> params) throws IOException {
        HttpClient httpclient = buildHttpClient();
        String responseBody = null;

        InputStream is = null;
        try {
            if (params != null) {
                UrlEncodedFormEntity uef = new UrlEncodedFormEntity(params, "UTF-8");
                request.setEntity(uef);
            }
            HttpResponse response = httpclient.execute(request);
            Header[] headers = response.getAllHeaders();
            boolean isGzip = false;
            for (int i = 0; i < headers.length; i++) {
                if ("Content-Encoding".equals(headers[i].getName()) && "gzip".equals(headers[i].getValue())) {
                    isGzip = true;
                    break;
                }
            }
            is = response.getEntity().getContent();
            if (isGzip) {
                responseBody = zipInputStream(is);
            } else {
                responseBody = readInputStream(is);
            }

        } finally {
            if (is != null) {
                is.close();
            }
            httpclient.getConnectionManager().shutdown();
        }

        log.info("post url: " + request.getURI() + " and response: " + responseBody);

        return responseBody;
    }

    /**
     * get请求
     *
     * @param request HttpGet
     *
     * @return String
     */
    private String doGetRequest(HttpGet request) throws IOException {
        HttpClient httpclient = buildHttpClient();
        String responseBody = null;

        InputStream is = null;
        try {
            HttpResponse response = httpclient.execute(request);

            Header[] headers = response.getAllHeaders();
            boolean isGzip = false;
            for (int i = 0; i < headers.length; i++) {
                if ("Content-Encoding".equals(headers[i].getName()) && "gzip".equals(headers[i].getValue())) {
                    isGzip = true;
                    break;
                }
            }
            is = response.getEntity().getContent();
            if (isGzip) {
                responseBody = zipInputStream(is);
            } else {
                responseBody = readInputStream(is);
            }

        } finally {
            if (is != null) {
                is.close();
            }
            httpclient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

    /**
     * @param request   HttpRequestBase
     * @param cookieMap Map<String, String>
     */
    private void setCookie(HttpRequestBase request, Map<String, String> cookieMap) {
        request.setHeader("Cookie", getCookieStr(cookieMap));
    }

    /**
     * @param cookieMap Map<String, String>
     *
     * @return String
     */
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
        } else {
            cookie = "JSESSIONID=" + JSESSIONID + ";BIGipServerotn=" + BIGipServerotn;
            if (current_captcha_type != null) {
                cookie += ";current_captcha_type=" + current_captcha_type;
            }
        }
        log.info("[request cooikes] {" + cookie + "}");
        return cookie;
    }

    /**
     * 获取参数oldPassengerStr值
     *
     * @param passengers List<PassengerData>
     *
     * @return String
     */
    private String getOldPassengerStr(List<PassengerData> passengers) {
        StringBuilder sb = new StringBuilder();
        for (PassengerData p : passengers) {
            sb.append(p.getName())
                    .append(',')
                    .append(p.getCardTypeValue())
                    .append(",")
                    .append(p.getCardNo())
                    .append(',')
                    .append(p.getTicketTypeValue())
                    .append('_');
        }
        return sb.toString();
    }

    /**
     * 获取oldPassengerStr
     *
     * @param passengers List<PassengerData>
     *
     * @return String
     */
    private String getPassengerTicketStr(List<PassengerData> passengers) {
        StringBuilder sb = new StringBuilder();
        for (PassengerData p : passengers) {
            if (p.getSeatType() != PassengerData.SeatType.NONE_SEAT) {
                sb.append(p.getSeatTypeValue());
            }
            sb.append(",0,")
                    .append(p.getTicketTypeValue())
                    .append(',')
                    .append(p.getName())
                    .append(',')
                    .append(p.getCardTypeValue())
                    .append(",")
                    .append(p.getCardNo())
                    .append(',')
                    .append(p.getMobileNotNull())
                    .append(",N_");
        }

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 处理返回文件流
     *
     * @param is InputStream
     *
     * @return String
     *
     * @throws IOException
     */
    private static String readInputStream(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line + "\n");
        }
        return buffer.toString();
    }

    /**
     * 处理gzip,deflate返回流
     *
     * @param is InputStream
     *
     * @return String
     *
     * @throws IOException
     */
    private static String zipInputStream(InputStream is) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(is);
        BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line + "\n");
        }
        return buffer.toString();
    }

    /**
     * @param config TicketConfigInfo
     *
     * @return Map
     */
    public Map<String, String> getCookie(TicketConfigInfo config) {
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put("_jc_save_fromStation", getUnicode4Cookie(config.getFrom_station_name(), config.getFrom_station()));
        cookies.put("_jc_save_toStation", getUnicode4Cookie(config.getTo_station_name(), config.getTo_station()));
        cookies.put("_jc_save_fromDate", config.getTrain_date());
        cookies.put("_jc_save_toDate", DateUtil.formatDate(new Date()));
        cookies.put("_jc_save_wfdc_flag", "dc");
        cookies.put("_jc_save_detail", "true");
        cookies.put("_jc_save_showIns", "true");
        cookies.put("_jc_save_czxxcx_toStation",
                getUnicode4Cookie(config.getFrom_station_name(), config.getFrom_station()));
        cookies.put("_jc_save_czxxcx_fromDate", DateUtil.formatDate(new Date()));
        cookies.put("current_captcha_type", "Z");
        return cookies;
    }

    /**
     * @param cityName String
     * @param cityCode String
     *
     * @return String
     */
    public static String getUnicode4Cookie(String cityName, String cityCode) {
        String result = "";
        for (int i = 0; i < cityName.length(); i++) {
            int chr1 = (char) cityName.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result += "%u" + Integer.toHexString(chr1).toUpperCase();
            } else {
                result += cityName.charAt(i);
            }
        }
        result += "%2C" + cityCode;
        return result;
    }

}
