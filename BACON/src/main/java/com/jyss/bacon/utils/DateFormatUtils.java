package com.jyss.bacon.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtils {
    public static final String YMD = "yyyyMMdd";
    public static final String YMD_YEAR = "yyyy";
    public static final String YMD_BREAK = "yyyy-MM-dd";
    public static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String YMDHMS_BREAK = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMS_BREAK_HALF = "MM-dd HH:mm";

    /**
     * 计算时间差
     */
    public static final int CAL_MINUTES = 1000 * 60;
    public static final int CAL_HOURS = 1000 * 60 * 60;
    public static final int CAL_DAYS = 1000 * 60 * 60 * 24;

    /**
     * 获取当前时间格式化后的值
     *
     * @param pattern
     * @return
     */
    public static String getNowDateText(String pattern){
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * 获取日期格式化后的值
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateText(Date date, String pattern){
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串时间转换成Date格式
     *
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
    public static Date getDate(String date, String pattern) throws Exception{
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    private static SimpleDateFormat getSimpleDateFormat(String pattern){
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取时间戳
     * @param date
     * @return
     */
    public static Long getTime(Date date){
        return date.getTime();
    }

    /**
     * 计算时间差
     * @param startDate
     * @param endDate
     * @param calType 计算类型,按分钟、小时、天数计算
     * @return
     */
    public static int calDiffs(Date startDate, Date endDate, int calType){
        Long start = DateFormatUtils.getTime(startDate);
        Long end = DateFormatUtils.getTime(endDate);
        int diff = (int) ((end - start)/calType);
        return diff;
    }

    /**
     * 计算时间差值以某种约定形式显示
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String timeDiffText(Date startDate, Date endDate){
        int calDiffs = DateFormatUtils.calDiffs(startDate, endDate, DateFormatUtils.CAL_MINUTES);
        if(calDiffs == 0){
            return "刚刚";
        }
        if(calDiffs < 60){
            return calDiffs + "分钟前";
        }
        calDiffs = DateFormatUtils.calDiffs(startDate, endDate, DateFormatUtils.CAL_HOURS);
        if(calDiffs < 24){
            return calDiffs + "小时前";
        }
        calDiffs = DateFormatUtils.calDiffs(startDate, endDate, DateFormatUtils.CAL_DAYS);
        if(calDiffs < 2){
            return calDiffs + "天前";
        }
        return DateFormatUtils.getDateText(startDate, DateFormatUtils.YMDHMS_BREAK_HALF);
    }

    /**
     * 显示某种约定后的时间值
     *
     * @param date
     * @return
     */
    public static String showTimeText(Date date){
        return DateFormatUtils.timeDiffText(date, new Date());
    }


    /**
     * 由出生日期获得年龄
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }

    public static void main(String[] args) throws Exception {
        Date start = DateFormatUtils.getDate("2018-01-07 15:30:02", DateFormatUtils.YMDHMS_BREAK);
        Date birthDay = DateFormatUtils.getDate("2010-10-20", DateFormatUtils.YMD_BREAK);
        System.out.println(DateFormatUtils.showTimeText(start));
        System.out.println(System.currentTimeMillis());
        System.out.println(DateFormatUtils.getAge(birthDay));
    }
}
