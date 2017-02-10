/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train.vo;

import java.util.Date;

import lombok.extern.log4j.Log4j;
import train.util.DateUtil;

@Log4j
public class TicketConfigInfo {

    /** 出发日期 **/
	private String train_date;

	/** 出发站code **/
	private String from_station;
	
	/** 出发站name **/
	private String from_station_name;

	/** 到达站code **/
	private String to_station;
	
	/** 到达站name **/
	private String to_station_name;
	
	/** 成人票 **/
	private String purpose_codes = "ADULT";
	
	private Date trainDateAlias;
	
	public TicketConfigInfo(String fromStationName, String fromStation, String toStationName, String toStation,
			String trainDate) {
		super();
		this.from_station_name = fromStationName;
		this.from_station = fromStation;
		this.to_station_name = toStationName;
		this.to_station = toStation;
		this.train_date = trainDate;
		try{
		    trainDateAlias = DateUtil.parseDate(trainDate);
		}catch(Exception e){
		    log.error("出发日期格式错误，请检查");
		}
	}
	

	public String getTrain_date() {
		return train_date;
	}

	public String getFrom_station() {
		return from_station;
	}

	public String getTo_station() {
		return to_station;
	}

	public String getPurpose_codes() {
		return purpose_codes;
	}
	

	public String getFrom_station_name() {
		return from_station_name;
	}

	public String getTo_station_name() {
		return to_station_name;
	}

    public Date getTrainDateAlias() {
        return trainDateAlias;
    }



}
