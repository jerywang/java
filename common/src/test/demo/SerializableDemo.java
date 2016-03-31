/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package test.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * java对象的序列化和反序列化
 * <p/>
 * Created by wangguoxing on 15-9-7.
 */
public class SerializableDemo implements Serializable {

    public static final long serialVersionUID = -1466479389299512377L;

    private int id = 21;

    private String name = "上海";

    public static void main(String[] args) {
        try {
            //1. 序列化到文件
            FileOutputStream fos = new FileOutputStream(new File("/tmp/demo.txt"));
            ObjectOutputStream os = new ObjectOutputStream(fos);
            SerializableDemo demo1 = new SerializableDemo();
            demo1.setId(10);
            demo1.setName("北京");
            os.writeObject(demo1);
            os.flush();
            os.close();

            FileInputStream fis = new FileInputStream(new File("/tmp/demo.txt"));
            ObjectInputStream ois = new ObjectInputStream(fis);

            SerializableDemo demo2 = (SerializableDemo) ois.readObject();
            System.out.println(demo2.getId());
            System.out.println(demo2.getName());
            ois.close();

            //2. 序列化成String
            //序列化使用的输出流
            ObjectOutputStream OOS = null;
            //序列化后数据流给ByteArrayOutputStream来保存 ByteArrayOutputStream可转成字符串或字节数组
            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
            //反序列化使用的输入流
            ObjectInputStream OIS = null;
            //byte[]myb="s";
            OOS = new ObjectOutputStream(BAOS);
            OOS.writeObject(demo1);
            byte[] abc = BAOS.toByteArray();
            String StrMySerializer = abc.toString();
            System.out.println("序列化后:" + StrMySerializer);
            OOS.close();

            //反序列化
            //ByteArrayInputStream可接收一个字节数组"byte[]"。供反序列化做参数
            ByteArrayInputStream BAIS = null;
            byte[] ddd = StrMySerializer.getBytes();
            System.out.println("序列化信息:" + ddd);
            BAIS = new ByteArrayInputStream(abc);
            OIS = new ObjectInputStream(BAIS);
            SerializableDemo c = (SerializableDemo) (OIS.readObject());
            System.out.println("反序列化结果:" + c.getName());
            OIS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
