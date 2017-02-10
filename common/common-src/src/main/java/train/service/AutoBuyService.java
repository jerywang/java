/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.log4j.Log4j;
import train.HttpClientService;
import train.TicketHttpClient;
import train.util.DateUtil;
import train.vo.PassengerData;
import train.vo.TicketBuyInfo;
import train.vo.TicketConfigInfo;
import train.vo.TrainConfigInfo;
import train.vo.TrainInfo;

@Log4j
public class AutoBuyService {

    private TicketBuyInfo buyInfo;

    private Map<String, String> cookies;

    /**
     * 同步代码块用
     */
    public static final Object obj = new Object();

    /**
     * 同步代码块用
     */
    public static boolean waitFlag = true;

    private static final Pattern PATTERN_TOKEN = Pattern.compile("var globalRepeatSubmitToken = '(\\w+)'");

    private static final Pattern PATTERN_KEY_CHECK = Pattern.compile("'key_check_isChange':'(\\w+)'");

    public AutoBuyService(TicketBuyInfo ticketBuyInfo) {
        this.buyInfo = ticketBuyInfo;
        cookies = getCookie(ticketBuyInfo.getTicketConfigInfo());
    }
    
    /**
     * <获取用户优选车次列表>
     * @param all
     * @return
     */
    private List<TrainInfo> getUserPerfer(List<TrainInfo> all) {
        List<TrainInfo> perfers = new ArrayList<TrainInfo>();
        TrainConfigInfo trainConfigInfo = buyInfo.getTrainConfigInfo();
        if (trainConfigInfo != null) {
            List<TrainConfigInfo.SpecificTrainInfo> selTrainInfo = trainConfigInfo.getTrains();
            for (TrainConfigInfo.SpecificTrainInfo specTrainInfo : selTrainInfo) {
                TrainInfo trainInfo = specTrainInfo.convertToTrainInfo();
                int index = all.indexOf(trainInfo);
                if (index != -1) {
                    perfers.add(all.get(index));
                }
            }
        }
        else {
            for (TrainInfo train : all) {
                if (!TrainInfo.isSellOut(train.getZe_num())) {//二等座
                    perfers.add(train);
                }
                else if (!TrainInfo.isSellOut(train.getYz_num())) {//硬座
                    perfers.add(train);
                }
                else if (!TrainInfo.isSellOut(train.getYw_num())) {//硬卧
                    perfers.add(train);
                }
                else if (!TrainInfo.isSellOut(train.getWz_num())) {//无座
                    perfers.add(train);
                }
                
            }
        }
        
        return perfers;
    }
    

    private Map<String, String> getCookie(TicketConfigInfo config) {
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put("_jc_save_fromStation", getUnicode4Cookie(config.getFrom_station_name(), config.getFrom_station()));
        cookies.put("_jc_save_toStation", getUnicode4Cookie(config.getTo_station_name(), config.getTo_station()));
        cookies.put("_jc_save_fromDate", config.getTrain_date());
        cookies.put("_jc_save_toDate", DateUtil.formatDate(new Date()));
        cookies.put("_jc_save_wfdc_flag", "dc");
        cookies.put("_jc_save_showZtkyts", "true");
        return cookies;
    }
    
    public static String getUnicode4Cookie(String cityName, String cityCode) {
        String result = "";
        for (int i = 0; i < cityName.length(); i++) {
            int chr1 = (char)cityName.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result += "%u" + Integer.toHexString(chr1).toUpperCase();
            }
            else {
                result += cityName.charAt(i);
            }
        }
        result += "%2C" + cityCode;
        return result;
    }
    

}
