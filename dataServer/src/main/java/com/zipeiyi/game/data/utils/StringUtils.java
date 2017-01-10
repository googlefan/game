package com.zipeiyi.game.data.utils;

import com.zipeiyi.game.common.proto.pojo.CardInfo;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StringUtils {
    public static List<String> splitToStringList(String str, String sepKey) {
        List list = new LinkedList();

        if ((isNotNull(str)) && (isNotNull(sepKey))) {
            if (str.contains(sepKey)) {
                String[] items = str.split(sepKey, -1);

                for (String item : items) {
                    list.add(item);
                }
            } else {
                list.add(str);
            }

        }

        return list;
    }

    public static final boolean isNotNull(String source) {
        return !isNull(source);
    }

    public static final boolean isNull(String source) {
        if ((source != null) && ((!source.trim().equals("")) || (!source.trim().equalsIgnoreCase("null")))) {
            return false;
        }

        return true;
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    public static List<Float> getFloatFromString(String s) {
        List<Float> floatList = new ArrayList<>();
        if (null != s && !"".equals(s)) {
            if (s.contains(",")) {
                String[] arr = s.split(",");
                for (String ai : arr) {
                    Float f = Float.valueOf(ai);
                    floatList.add(f);
                }
            } else {
                Float f = Float.valueOf(s);
                floatList.add(f);
            }
        }
        return floatList;
    }


    public static String covertToString(List<CardInfo> openCardList) {
        JSONObject obj = JSONObject.fromObject(openCardList);
        return obj.toString();
    }
}
