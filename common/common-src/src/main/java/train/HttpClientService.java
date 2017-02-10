/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

import lombok.extern.log4j.Log4j;
import train.vo.ContacterInfo;
import train.vo.JsonMsg4CheckOrder;
import train.vo.JsonMsg4QueryWait;
import train.vo.OrderToken;
import train.vo.PassengerData;
import train.vo.QueryLeftNewDTO;
import train.vo.QueryTrainVo;
import train.vo.TicketConfigInfo;
import train.vo.TrainInfo;
import util.JsonUtil;

/**
 * $Id HttpClientService.java Oct 13,2016 wangguoxing (554952580@qq.com) $
 */
@Log4j
public class HttpClientService extends Thread {

    private static TicketHttpClient httpClient = null;

    private static void sleepSec(long sec) {
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void run() {
        if (httpClient == null) {
            httpClient = TicketHttpClient.getInstance();
            if (httpClient != null) {
                log.info("初始获取cookie成功");
            } else {
                log.error("初始获取cookie失败");
                System.exit(-1);
            }
        }

        // 登录验证码
        File imageFile = httpClient.buildLoginCodeImage();
        if (imageFile == null || !imageFile.exists()) {
            log.info("获取验证码失败");
            System.exit(-1);
        }
        log.info("获取验证码成功");

        // 登录部分
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your authCode:");
            String authCode = br.readLine();
//             String result = httpClient.checkLogin("jeu707", "jfu750", authCode);
            String result = httpClient.checkLogin("jtc974", "ogu600", authCode);
            if (result == null) {
                log.info("登录成功!");
            }
        } catch (Exception e) {
            log.error("登录失败");
        }

        sleepSec(1000L);

        // 提交订单
        TicketConfigInfo ticketConfigInfo = new TicketConfigInfo("上海", "SHH", "张家口南", "ZMP", "2016-10-25");
        TrainInfo trainInfo = genTrainInfo();
        String submitRes =
                httpClient.submitOrderRequest(ticketConfigInfo, httpClient.getCookie(ticketConfigInfo), trainInfo);
        log.info("submitOrderRequest res: " + submitRes);
        sleepSec(200L);

        // 联系人信息
        ContacterInfo[] contacterInfos = httpClient.getPassengers();
        log.info("联系人信息: " + JsonUtil.toJson(contacterInfos));

        sleepSec(1000L);

        // 下单验证码
        File imageFile1 = httpClient.buildOrderCodeImage(null);

        sleepSec(1000L);

        if (imageFile1 == null || !imageFile1.exists()) {
            log.error("获取提交订单验证码失败");
            System.exit(-1);
        }
        log.info("获取提交订单验证码成功");

        sleepSec(1000L);

        // 订单流程
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your orderCode:");
            String orderCode = br2.readLine();

//            OrderToken orderToken = new OrderToken("49f78d1a78f88e266beb131c1fb8a6d3",
//             "7F4355EFF76CC95B7010E086F40944B8F39EBCE0297C6B748C5E2F66");
            OrderToken orderToken = httpClient.getOrderToken();
            log.info("orderToken is: " + JsonUtil.toJson(orderToken));
            // 检验订单码
            boolean checkCodeResult = httpClient.checkOrderCode(orderCode, orderToken.getToken(), httpClient
                    .getCookie(ticketConfigInfo));
            // 先检查验证码
            if (!checkCodeResult) {
                log.error("验证码不正确! 重试一次");
                checkCodeResult = httpClient.checkOrderCode(orderCode, orderToken.getToken(), httpClient
                        .getCookie(ticketConfigInfo));
                if (!checkCodeResult) {
                    log.error("验证码不正确! 重试二次");
                    orderToken = new OrderToken("49f78d1a78f88e266beb131c1fb8a6d3",
                            "7F4355EFF76CC95B7010E086F40944B8F39EBCE0297C6B748C5E2F66");
                    checkCodeResult = httpClient.checkOrderCode(orderCode, orderToken.getToken(), httpClient
                            .getCookie(ticketConfigInfo));
                    if (!checkCodeResult) {
                        log.error("验证码不正确! 放弃");
                        System.exit(-1);
                    }
                }
            }

            sleepSec(200L);

            // 校验订单
            JsonMsg4CheckOrder msg4CheckOrder = httpClient.checkOrderInfo(orderCode,
                    orderToken.getToken(), genPassengers(), httpClient.getCookie(ticketConfigInfo));
            if (Boolean.valueOf(msg4CheckOrder.getData().getString("submitStatus"))) {
                log.info("post checkOrderInfo success!");
            } else {
                log.error("post checkOrderInfo failed!");
            }
            // 获取排队数
            Date date = null;
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                date = df.parse("2016-10-25");
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpClient.getQueueCount(trainInfo, date, "3", orderToken.getToken(),
                    httpClient.getCookie(ticketConfigInfo));

            // confirmSingleForQueue
            httpClient.confirmQueue(trainInfo, orderToken, genPassengers(), orderCode,
                    httpClient.getCookie(ticketConfigInfo));

            // queryOrderWaitTime
            JsonMsg4QueryWait jsonMsg4QueryWait =
                    httpClient.queryOrderWaitTime(orderToken.getToken(), httpClient.getCookie(ticketConfigInfo));
            String orderId = jsonMsg4QueryWait.getData().getString("orderId");
            if (!StringUtils.isEmpty(orderId) && !orderId.equals("null")) {
                log.info("恭喜你买到票了，可以回家了，哦耶!(请尽快登录12306付款)");
            }
            // resultOrderForDcQueue
            httpClient.resultOrderForDcQueue(orderId, orderToken.getToken());

        } catch (Exception e) {
            log.error("检验订单失败",e);
        }
    }

    public static TicketHttpClient getHttpClient() {
        return httpClient;
    }

    private OrderToken queryOrderToken(TicketConfigInfo ticketConfigInfo) {
        OrderToken token = null;

        TicketHttpClient client = HttpClientService.getHttpClient();

        int sum = 0;
        while (token == null && sum < 3) {
            try {
                String msg = client.getInitDcPage(client.getCookie(ticketConfigInfo));
                String token_str = null;
                String key_check_isChange_str = null;

                Matcher m_token = PATTERN_TOKEN.matcher(msg);
                Matcher m_key_check_isChange = PATTERN_KEY_CHECK.matcher(msg);
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

    private List<PassengerData> genPassengers() {
        List<PassengerData> passengers = Lists.newArrayList();
        PassengerData passenger = new PassengerData("胡相玉", "512222195805250888", "15624549284");
        passenger.setSeatType(PassengerData.SeatType.HARD_SLEEPER);
        passengers.add(passenger);
        return passengers;
    }

    /**
     * 车次详情
     *
     * @return trainInfo
     */
    private static TrainInfo genTrainInfo() {
        String res = httpClient.queryC();
        QueryTrainVo queryTrainVo = JsonUtil.fromJson(res, QueryTrainVo.class);
        List<QueryLeftNewDTO> queryLeftNewDTOs = queryTrainVo.getData();
        QueryLeftNewDTO queryLeftNewDTO = queryLeftNewDTOs.get(0);
        queryLeftNewDTO.getQueryLeftNewDTO().setSecretStr(queryLeftNewDTO.getSecretStr());
        return queryLeftNewDTO.getQueryLeftNewDTO();
    }

    private static final Pattern PATTERN_TOKEN = Pattern.compile("var globalRepeatSubmitToken = '(\\w+)'");

    private static final Pattern PATTERN_KEY_CHECK = Pattern.compile("'key_check_isChange':'(\\w+)'");
}
