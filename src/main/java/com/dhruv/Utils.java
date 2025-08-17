package com.dhruv;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> urlCollector(Class<?> clazz) {
        List<String> urls = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (var field : fields) {
            if(Modifier.isStatic(field.getModifiers()) && field.getType().equals(String.class)) {
                try {
                    urls.add((String) field.get(null));
                }
                catch(IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return urls;

    }

}
