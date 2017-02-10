/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package train.vo;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4Login extends JsonMsgSuper {
    
    private JSONObject data;

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return "JsonMsg4Login []" + super.toString();
    }


}
