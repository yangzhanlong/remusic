package com.me.component_base.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author yang
 */
public class TimeUtils {
    private static final String TAG = "TimeUtil";

    public static final String FORMAT_DATE_EN = "yyyy.MM.dd";
    public static final String FORMAT_DATE_EN_2 = "yyyy-MM-dd";
    public static final String FORMAT_DATE_EN_3 = "yyyy/MM/dd";
    public static final String FORMAT_DATE_CN = "yyyy年MM月dd日";

    public static final String FORMAT_TIME_CN = "yyyy年MM月dd HH时mm分ss秒";
    public static final String FORMAT_TIME_CN_2 = "yyyy年MM月dd日 HH时mm分";
    public static final String FORMAT_TIME_EN = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TIME_EN_2 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_TIME_EN_3 = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_TIME_EN_4 = "MM-dd";
    public static final String FORMAT_TIME_CN_4 = "MM月dd日";
    public static final String FORMAT_TIME_EN_5 = "MM-dd HH:mm";
    public static final String FORMAT_TIME_EN_6 = "yyyy";

    public static final String FORMAT_DAY_CN = "HH时mm分ss秒";
    public static final String FORMAT_DAY_CN_2 = "HH时mm分";
    public static final String FORMAT_DAY_EN = "HH:mm:ss";
    public static final String FORMAT_DAY_EN_2 = "HH:mm";
    public static final String FORMAT_DAY_EN_3 = "mm:ss";
    public static final String FORMAT_DAY_EN_4 = "hh:mm";


    private static final SimpleDateFormat SDF = new SimpleDateFormat(FORMAT_TIME_CN, Locale.CHINA);
    private static final SimpleDateFormat SDF_2 = new SimpleDateFormat(FORMAT_TIME_EN, Locale.CHINA);

    /**
     * 在之前
     */
    private static final int TIME_BEFORE = 1;
    /**
     * 在中间
     */
    private static final int TIME_ING = 2;
    /**
     * 在之后
     */
    private static final int TIME_AFTER = 3;

    /**
     * 获取年
     */
    public static String getYear() {
        Calendar cd = Calendar.getInstance();
        return String.valueOf(cd.get(Calendar.YEAR));
    }

    /**
     * 获取月
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取当天0点
     */
    public static long getTodayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000;
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }

    /**
     * 获取时间戳对应0点时间戳
     */
    public static long getStartTimeOfDay(long now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间戳对应的时间(上下午)
     */
    public static String getTimeByTimeStamp(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        String t;
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            t = "上午";
        } else {
            t = "下午";
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String m;
        if (minute < 10) {
            m = "0" + minute;
        } else {
            m = String.valueOf(minute);
        }
        return convertTime(TimeUtils.FORMAT_DATE_EN_2, time) + " " + hour + ":" + m;
    }

    /**
     * string型时间转换
     *
     * @param timeFormat 时间格式
     * @param timestamp  时间
     * @return 刚刚  x分钟  小时前  ...
     */
    public static String convertTime(String timeFormat, long timestamp) {
        try {
            Date date = new Date();
            date.setTime(timestamp);
            return format(timeFormat, date);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    private static String format(String timeFormat, Date date) {
        SDF.setTimeZone(TimeZone.getDefault());
        SDF.applyPattern(timeFormat);
        return SDF.format(date);
    }

    /**
     * 计算上一个时间离当前时间间隔
     *
     * @param timestamp 时间
     * @return 刚刚  x分钟  一天内  ...
     */
    public static String intervalTime(long timestamp) {
        return intervalTime(timestamp, false);
    }

    /**
     * 计算上一个时间离当前时间间隔
     *
     * @param timestamp    时间
     * @param includeAfter 时间
     * @return 刚刚  x分钟  一天内  ...
     */
    private static String intervalTime(long timestamp, boolean includeAfter) {
        String timeStr;

        long interval = (System.currentTimeMillis() - timestamp) / 1000;
        if (!includeAfter || interval >= 0) {
            //1分钟内 服务端的时间 可能和本地的有区别 所以小于0的 对于这个情况全部都显示刚刚
            if (interval <= 60) {
                timeStr = "刚刚";
            } else if (interval < 60 * 60) {
                // 1小时内
                timeStr = (interval / 60 == 0 ? 1 : interval / 60) + "分钟前";
            } else if (interval < 24 * 60 * 60) {
                // 一天内
                timeStr = (interval / 60 * 60 == 0 ? 1 : interval / (60 * 60)) + "小时前";
            } else if (interval < 7 * 24 * 60 * 60) {
                // 天前
                timeStr = interval / 24 * 60 * 60 == 0 ? "昨天" : interval / (24 * 60 * 60) + "天前";
            } else {
                Date date = new Date();
                date.setTime(timestamp);
                //timeStr = format(FORMAT_DATE_CN, date);
                timeStr = format(FORMAT_DATE_EN_3, date);
            }
        } else {
            return intervalAfterTime(timestamp);
        }
        return timeStr;
    }

    /**
     * int型时间转换 比较距离结束
     *
     * @param timestamp 时间
     * @return 刚刚  x分钟  一天后  ...
     */
    private static String intervalAfterTime(long timestamp) {
        String timeStr;

        long interval = (timestamp - System.currentTimeMillis()) / 1000;
        if (interval <= 60) {
            timeStr = "刚刚";
        } else if (interval < 60 * 60) {
            timeStr = (interval / 60 == 0 ? 1 : interval / 60) + "分钟后";
        } else if (interval < 24 * 60 * 60) {
            timeStr = (interval / 60 * 60 == 0 ? 1 : interval / (60 * 60)) + "小时后";
        } else if (interval < 30 * 24 * 60 * 60) {
            timeStr = (interval / 24 * 60 * 60 == 0 ? 1 : interval / (24 * 60 * 60)) + "天后";
        } else if (interval < 12 * 30 * 24 * 60 * 60) {
            timeStr = (interval / 30 * 24 * 60 * 60 == 0 ? 1 : interval / (30 * 24 * 60 * 60)) + "月后";
        } else {
            Date date = new Date();
            date.setTime(interval);
            timeStr = format(FORMAT_DATE_CN, date);
        }
        return timeStr;
    }

    /**
     * 将long型时间转为固定格式的时间字符串
     *
     * @param longTime 时间
     */
    public static String convertToTime(long longTime) {
        return convertToTime(FORMAT_TIME_EN, longTime);
    }

    /**
     * 将long型时间转为固定格式的时间字符串
     *
     * @param timeformat 时间格式
     * @param longTime   时间
     */
    private static String convertToTime(String timeformat, long longTime) {
        Date date = new Date(longTime);
        return convertToTime(timeformat, date);
    }

    /**
     * 将long型时间转为固定格式的时间字符串
     *
     * @param timeFormat 时间格式
     * @param longTime   时间
     */
    public static String convertToDifftime(String timeFormat, long longTime) {
        Date date = new Date(longTime);
        SDF.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        SDF.applyPattern(timeFormat);
        return SDF.format(date);
    }

    public static String convertDateToTime(String timeFormat, Date date) {
        return format(timeFormat, date);
    }


    /**
     * 将Date型时间转为固定格式的时间字符串
     *
     * @param timeFormat 时间格式
     * @param date       时间
     */
    private static String convertToTime(String timeFormat, Date date) {
        return format(timeFormat, date);
    }

    /**
     * 将Calendar型时间转为固定格式的时间字符串
     *
     * @param timeFormat 时间格式
     * @param calendar   时间
     * @return timeFormat
     */
    public static String convertToTime(String timeFormat, Calendar calendar) {
        return format(timeFormat, calendar.getTime());
    }

    /**
     * 将String类型时间转为long类型时间
     *
     * @param timestamp yyyy-MM-dd HH:mm:ss
     * @return 时间
     */
    public static long covertToLong(String timestamp) {
        try {
            Date date = SDF_2.parse(timestamp);
            return date.getTime();
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    /**
     * long型时间转换
     *
     * @param longTime 长整型时间
     * @return 2013年7月3日 18:05(星期三)
     */
    public static String convertDayOfWeek(String timeFormat, long longTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(longTime));

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String h = hour > 9 ? String.valueOf(hour) : "0" + hour;
        int minute = c.get(Calendar.MINUTE);
        String m = minute > 9 ? String.valueOf(minute) : "0" + minute;
        return String.format(Locale.getDefault(), timeFormat, year, month + 1, date, h, m, converToWeek(c.get(Calendar.DAY_OF_WEEK)));
    }

    /**
     * 转换数字的星期为字符串的
     *
     * @param w 星期
     * @return 星期x
     */
    private static String converToWeek(int w) {
        String week = null;

        switch (w) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
            default:
                break;
        }

        return week;
    }

    /**
     * 计算时间是否在区间内
     *
     * @param time  time
     * @param time1 time
     * @param time2 time
     */
    public static int betweenTime(long time, long time1, long time2) {
        if (time1 > time2) {
            long testTime = time1;
            time1 = time2;
            time2 = testTime;
        }

        //已经过去
        if (time1 > time) {
            return TIME_BEFORE;
        } else if (time2 < time) {
            return TIME_AFTER;
        } else {
            return TIME_ING;
        }
    }

    /**
     * 时间字符串转时间戳
     */
    public static long covertStringToTime(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.FORMAT_TIME_EN);
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert d != null;
        return d.getTime();
    }
}