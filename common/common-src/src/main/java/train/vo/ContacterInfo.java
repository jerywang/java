/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train.vo;

import lombok.Data;

/**
 * $Id ContacterInfo.java Oct 13,2016 wangguoxing (554952580@qq.com) $
 */
@Data
public class ContacterInfo {
    private String passenger_name;

    private String passenger_id_type_code;//1

    private String passenger_id_type_name;//二代身份证

    private String passenger_id_no;//身份证号

    private String passenger_type;//1

    private String passenger_type_name;//成人

    private String mobile_no;
}
