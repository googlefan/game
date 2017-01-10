/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public abstract class ReflectionUtility extends ReflectionUtils {

    public static void doWithDeclaredFields(Class<?> clazz, ReflectionUtils.FieldCallback fc, ReflectionUtils.FieldFilter ff)
            throws IllegalArgumentException {
        if ((clazz == null) || (clazz == Object.class)) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ((ff == null) || (ff.matches(field))) {
                try {
                    fc.doWith(field);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("非法访问属性 '" + field.getName() + "': " + ex);
                }
            }
        }
    }

    public static Field getFirstDeclaredFieldWith(Class<?> clz, Class<? extends Annotation> annotationClass) {
        for (Field field : clz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                return field;
            }
        }
        return null;
    }

    public static Field getRecursiveFirstDeclaredFieldWith(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Field currField = null;
        if ((clazz != null) && (clazz != Object.class)) {
            currField = getFirstDeclaredFieldWith(clazz, annotationClass);
            if (currField == null) {
                return getRecursiveFirstDeclaredFieldWith(clazz.getSuperclass(), annotationClass);
            }
        }
        return currField;
    }

    public static Field[] getDeclaredFieldsWith(Class<?> clz, Class<? extends Annotation> annotationClass) {
        List<Field> fields = new ArrayList();
        for (Field field : clz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                fields.add(field);
            }
        }
        return (Field[]) fields.toArray(new Field[0]);
    }

    public static Method getFirstDeclaredMethodWith(Class<?> clz, Class<? extends Annotation> annotationClass) {
        for (Method method : clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return method;
            }
        }
        return null;
    }

    public static Method[] getDeclaredMethodsWith(Class<?> clz, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList();
        for (Method method : clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }
        return (Method[]) methods.toArray(new Method[0]);
    }

    public static Method[] getDeclaredGetMethodsWith(Class<?> clz, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList();
        for (Method method : clz.getDeclaredMethods()) {
            if (method.getAnnotation(annotationClass) != null) {
                if (!method.getReturnType().equals(Void.TYPE)) {
                    if (method.getParameterTypes().length <= 0) {
                        methods.add(method);
                    }
                }
            }
        }
        return (Method[]) methods.toArray(new Method[0]);
    }
}
