/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zipeiyi.game.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * 使用jackson进行对象与json之间的转换
 *
 * @author penn
 */
public class JsonUtil {

    private static final String dataFormate = "yyyy-MM-dd HH:mm:ss";
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 将对象转换为json
     *
     * @param bean 需要转换的对象
     * @return json
     * @throws IOException
     */
    public static String getJsonFromBean(Object bean) {
        try {
            ObjectMapper om = new ObjectMapper();
//            om.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);//将所有bumbers转成string
            om.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
            om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            om.setDateFormat(new SimpleDateFormat(dataFormate));
            // om.enableDefaultTyping();
            return om.writeValueAsString(bean);

        } catch (Exception e) {
            logger.error("转译" + bean.getClass().getSimpleName() + "出现错误！", e);
        }
        return null;
    }

    
    public static Object getBeanFromJson2(String json,Class<?> clazz){
    	
    	 Gson gson = new Gson();
    	 Object obj = gson.fromJson(json, clazz);
    	return obj;
    }
    /**
     * 将json转换为对象
     *
     * @param json 需要转换的json
     * @param clazz 要转成的对象
     * @return object
     * @throws IOException
     */
    public static Object getBeanFromJson(String json, Class<?> clazz) {
        try {
            if(json == null){
                return null;
            }
            ObjectMapper om = new ObjectMapper();
//            om.setSerializationInclusion(Inclusion.ALWAYS);//======
            om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略未知key:vlaue
            om.setDateFormat(new SimpleDateFormat(dataFormate));
            //om.enableDefaultTyping();
            return om.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("将json转译成" + clazz.getSimpleName() + "出现错误！", e);
        }
        return null;
    }

    public static <T> List<T> getListFromJson(String json, Class<?> clazz) {
        try {
            ObjectMapper om = new ObjectMapper();
//            om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略未知key:vlaue
//            om.setDateFormat(new SimpleDateFormat(dataFormate));
//             om.enableDefaultTyping();
//             
//            TypeFactory t = TypeFactory.defaultInstance();
            // 指定容器结构和类型（这里是ArrayList和clazz）
            //  List<T> list = om.readValue(json, t.constructCollectionType(ArrayList.class, clazz));

            return om.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (Exception e) {
            logger.error("将json转译成" + clazz.getSimpleName() + "出现错误！", e);
        }
        return null;
    }

    public static String getNodeFromJson(String json, String fild) {
        try {
            ObjectMapper om = new ObjectMapper();
            om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略未知key:vlaue
            om.setDateFormat(new SimpleDateFormat(dataFormate));
            om.enableDefaultTyping();
            JsonNode jn = om.readTree(json);
            JsonNode obj = jn.get(fild);
            return obj.asText();
        } catch (Exception e) {
            logger.error("从json中获取" + fild + "出现错误！", e);
        }
        return null;
    }

////
    public static void main(String[] arg) throws Exception {
        Map map = new HashMap<Object, Object>();
        map.put("0", 0);
        map.put("1", 1);
        String s = getJsonFromBean(map);
        System.out.println("->" + s);
        Map map2 = (Map)getBeanFromJson(s, Map.class);
        System.out.println("->"+map2.get("0"));

    }
}
