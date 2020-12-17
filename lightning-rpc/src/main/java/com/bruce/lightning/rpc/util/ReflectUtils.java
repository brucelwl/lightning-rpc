package com.bruce.lightning.rpc.util;

import java.lang.reflect.Field;

public abstract class ReflectUtils {


    public static void set(Object obj, Class<?> clazz, String fieldName, Object value) {
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
