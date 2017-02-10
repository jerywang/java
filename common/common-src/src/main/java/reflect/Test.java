/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package reflect;
/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.sun.org.glassfish.gmbal.Description;

/**
 * $Id Test.java Nov 30,2016 wangguoxing (554952580@qq.com) $
 */
@Description(key = "keys", value = "abc")
public class Test {

    public int i;
    public String str = "ss";

    public void printName(String name) throws Exception {
        System.out.println(this.getClass().getName());
        System.out.println(this.getClass().getMethod("printName", String.class));
        for (Field field : this.getClass().getFields()) {
            //System.out.println(field.getName() + field.getType());
            System.out.println(field.get(this));
        }
        System.out.println(name);
        for (Annotation annotation : this.getClass().getAnnotations()) {
            System.out.println(annotation.toString());
        }
        System.out.println(this.getClass().getAnnotation(Description.class).key());
        System.out.println(this.getClass().getAnnotation(Description.class).value());
    }

    public void print() throws Exception {
        Class<?> clazz = Class.forName(this.getClass().getName());
        Object object = clazz.newInstance();
        Method m = clazz.getMethod("printName", String.class);
        m.invoke(object, "myName");
    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.print();
    }
}

