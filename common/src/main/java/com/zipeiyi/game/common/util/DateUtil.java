package com.zipeiyi.game.common.util;

import com.zipeiyi.game.common.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zhuhui on 16-12-21.
 */
public class DateUtil {

    public static final SimpleDateFormat sf = new SimpleDateFormat(Constants.formatYmd);

    public static String getDateBefore(String d, String c, int day) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTime(sf.parse(d));
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        String dbefore = sf.format(now.getTime());
        if (dbefore.compareTo(c) > 0) {
            return dbefore;
        }
        return c;
    }

    public static int differentDaysByMillisecond(String end, String start) throws ParseException {
        int days = (int) ((sf.parse(end).getTime() - sf.parse(start).getTime()) / (1000 * 3600 * 24));
        return days + 1;
    }
}
