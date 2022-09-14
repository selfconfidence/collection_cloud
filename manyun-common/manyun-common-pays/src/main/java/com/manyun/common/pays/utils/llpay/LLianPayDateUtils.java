package com.manyun.common.pays.utils.llpay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LLianPayDateUtils {
    private static final String DefaultPattern = "yyyyMMddHHmmss";

    private LLianPayDateUtils() {
    }

    public static String getTimestamp() {
        return format(new Date(), DefaultPattern);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
