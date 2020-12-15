package com.bruce.lightning.rpc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 自定义响应结构, 转换类
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     */
    public static String toJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonStr  json数据
     * @param beanType 对象中的object类型
     */
    public static <T> T jsonToObj(String jsonStr, Class<T> beanType) {
        try {
            return objectMapper.readValue(jsonStr, beanType);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object parse(String json, Type type) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        try {
            return objectMapper.readerFor(javaType).readValue(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> beanType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


}
