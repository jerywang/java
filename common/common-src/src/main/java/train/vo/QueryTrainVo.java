/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train.vo;

import java.util.List;

import lombok.Data;

/**
 * $Id QueryTrainVo.java Oct 17,2016 wangguoxing (554952580@qq.com) $
 */
@Data
public class QueryTrainVo {
    private String validateMessagesShowId;
    private Boolean status;
    private List<QueryLeftNewDTO> data;
    private String[] messages;
    private Object validateMessages;
}


