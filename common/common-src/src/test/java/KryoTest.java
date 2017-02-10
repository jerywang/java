/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;
import util.KryoUtil;

/**
 * $Id KryoTest.java Oct 11,2016 wangguoxing (554952580@qq.com) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class KryoTest {
    @Test
    public void testKryo() {
        DemoObject object = new DemoObject();
        List<DemoObjectVo> list = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            DemoObjectVo vo = new DemoObjectVo();
            vo.setId(i);
            vo.setName("name" + String.valueOf(i));
            vo.setDate(new Date());
            Map<String, String> map = Maps.<String, String>newHashMap();
            map.put("key" + String.valueOf(i),"value" + String.valueOf(i));
            vo.setHashMap(map);
            list.add(vo);
        }
        object.setVoList(list);
        byte[] bytes = KryoUtil.objectToByte(object);
        System.out.println("bytes.length is: " + bytes.length); //buffer size can be set 4096
        for (byte b : bytes) {
            System.out.print(b);
        }
        DemoObject object1 = KryoUtil.byteToObject(bytes, DemoObject.class);
        System.out.println("\n demoObject is: " + object1);
    }
}

@Data
class DemoObject {
    private List<DemoObjectVo> voList;
}

@Data
class DemoObjectVo {
    private int id;
    private String name;
    private Date date;
    private Map<String, String> hashMap;
}