/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.collect.Lists;

import lombok.extern.log4j.Log4j;

/**
 * $Id KryoUtil.java Oct 11,2016 wangguoxing@baidu.com $
 */
@Log4j
public class KryoUtil {
    private static final int OBJECT_MAX_BUFFER_SIZE = 40960;

    private static KryoFactory factory = new KryoFactory() {
        public Kryo create() {
            Kryo kryo = new Kryo();
            kryo.register(Date.class);
            kryo.register(HashSet.class);
            kryo.register(HashMap.class);
            kryo.register(ArrayList.class);
            return kryo;
        }
    };

    private static KryoPool pool = new KryoPool.Builder(factory).softReferences().build();

    /**
     * translate bytes to objects. possibly return <strong>null</strong>
     *
     * @param bytes byte[]
     * @param clazz Object
     *
     * @return null when some exception or empty input
     */
    public static <T> T byteToObject(byte[] bytes, Class<T> clazz) {
        T t = null;

        if (bytes == null || bytes.length == 0) {
            return null;
        }
        Kryo kryo = pool.borrow();
        try {
            Input input = new Input(bytes);

            t = kryo.readObject(input, clazz);

            input.close();
        } catch (Exception e) {
            log.error("error in translate bytes to object", e);
        } finally {
            pool.release(kryo);
        }
        return t;
    }

    /**
     * translate bytes to objects. possibly return <strong>empty list</strong>
     *
     * @param bytes byte[]
     * @param clazz Object
     *
     * @return empty list when some exception or empty input
     */
    public static <T> List<T> byteToObject(Collection<byte[]> bytes, Class<T> clazz) {
        List<T> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(bytes)) {
            return list;
        }
        Kryo kryo = pool.borrow();
        try {
            for (byte[] b : bytes) {
                if (b == null || b.length == 0) {
                    log.debug("null found in the kryo utils when convert byte list to object list!");
                    continue;
                }
                Input input = new Input(b);
                T t = kryo.readObject(input, clazz);

                list.add(t);
                input.close();
            }

        } catch (Exception e) {
            log.error("error in translate bytes to object", e);
        } finally {
            pool.release(kryo);
        }
        return list;
    }

    /**
     * translate object to bytes. possibly return <strong>null</strong>
     *
     * @param t Object
     *
     * @return null when some exception or empty input
     */
    public static <T> byte[] objectToByte(T t) {
        byte[] bytes = null;

        if (t == null) {
            return null;
        }
        Kryo kryo = pool.borrow();
        try {
            Output output = new Output(OBJECT_MAX_BUFFER_SIZE);
            kryo.writeObject(output, t);

            bytes = output.toBytes();

            output.close();
        } catch (Exception e) {
            log.error("error in translate object to bytes", e);
        } finally {
            pool.release(kryo);
        }
        return bytes;
    }

    /**
     * translate object to bytes with class type. possibly return <strong>null</strong>
     *
     * @param t Object
     *
     * @return null when some exception or empty input
     */
    public static <T> byte[] objectToByte(T t, Class<T> clazz) {
        byte[] bytes = null;

        if (t == null) {
            return null;
        }
        Kryo kryo = pool.borrow();
        try {
            Output output = new Output(OBJECT_MAX_BUFFER_SIZE);
            kryo.writeObjectOrNull(output, t, clazz);

            bytes = output.toBytes();

            output.close();
        } catch (Exception e) {
            log.error("error in translate object to bytes", e);
        } finally {
            pool.release(kryo);
        }
        return bytes;
    }

}
