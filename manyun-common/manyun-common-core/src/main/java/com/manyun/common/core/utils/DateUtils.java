package com.manyun.common.core.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final Map<String, TimeUnit> convertMap = MapUtil.<String,TimeUnit>builder().put("s", TimeUnit.SECONDS).put("m", TimeUnit.MINUTES).put("h", TimeUnit.HOURS).put("d", TimeUnit.DAYS).build();

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 Date ==> LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date)
    {
        if(date==null){
            return null;
        }
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime;
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 比较两个时间大小，前者大 = -1， 相等 =0，后者大 = 1
     *
     * @param date1
     * @param date2
     * @param pattern yyyy-MM-dd
     * @return
     */
    public static int compareTo(Date date1, Date date2, String pattern) {
        date1 = getDateToDate(date1, pattern);
        date2 = getDateToDate(date2, pattern);
        if (date1.getTime() > date2.getTime()) {
            return -1;
        } else if (date1.getTime() < date2.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 将时间转换成指定格式的时间， 如 2019-10-10 10:10:10  转换格式为 yyyy-MM-dd 则最终会得到一个date对象为 2019-10-10
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date getDateToDate(Date date, String pattern) {
        String dateToStr = getDateToStr(date, pattern);
        return getStrToDate(dateToStr, pattern);
    }

    /**
     * 将date转换成字符串
     *
     * @param date    需要格式化的时间对象
     * @param pattern 字符串的时间格式 比如 字符串为:2017-01-01  需要转换对象则 pattern需要传入 yyyy-MM-dd
     * @return
     */
    public static String getDateToStr(Date date, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转换成date对象
     *
     * @param str     需要转换的字符串
     * @param pattern 字符串的时间格式 比如 字符串为:2017-01-01  需要转换对象则 pattern需要传入 yyyy-MM-dd
     * @return
     */
    public static Date getStrToDate(String str, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }


    public static Long convertTime(String time,TimeUnit returnTimeUnit){
        Assert.isTrue(time.length() >=2,"time length < 2 ?");
        TimeUnit timeLast = getTimeLast(time);
        // 小比较
        Assert.isTrue(timeLast.compareTo(returnTimeUnit)>=0,"tarTime >= returnTime ?");
        return Convert.convertTime(Long.parseLong(time.substring(0,time.length() - 1)),timeLast,returnTimeUnit);
    }


    /**
     * com.manyun.common.core.enums.DelayLevelEnum 针对此枚举
     * @param time  根据 1s 10h 等等 获取时间单位
     * @return
     */
    public static TimeUnit getTimeLast(String time){
        String last = time.substring(time.length() - 1);
          return convert(last);
    }

    /**
     * 根据  h s d m 获取时间单位
     * @param last
     * @return
     */
    private static TimeUnit convert(String last){
        TimeUnit timeUnit = convertMap.get(last);
        Assert.isTrue(Objects.nonNull(timeUnit),"last not fount TimeUnit");
        return timeUnit;
    }

}
