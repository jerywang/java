package zookeeper.test.util;

import java.io.Reader;
import java.lang.reflect.Type;

import com.baidu.gson.Gson;
import com.baidu.gson.GsonBuilder;

/**
 * Comment of JsonUtils
 *
 */
public class JsonUtil {
    private static Gson gson = new GsonBuilder().create();

    public static <T> String toJson(T t) {
        return gson.toJson(t);
    }

    public static String toJson(Object o, Type t) {
        return gson.toJson(o, t);
    }

    public static <T> T fromJson(String json, Class<T> c) {
        return gson.fromJson(json, c);
    }

    public static <T> T fromJson(Reader reader, Class<T> c) {
        return gson.fromJson(reader, c);
    }
}
