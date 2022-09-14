package com.manyun.common.pays.utils.llpay.security;

import java.nio.charset.StandardCharsets;

public class Base64 {
    /**
     * @param s
     * @return
     */
    public static String getBASE64(String s) {
        if (s == null) {
            return null;
        }
        return getBASE64(s.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param b
     * @return
     */
    public static String getBASE64(byte[] b) {
        byte[] rb = org.apache.commons.codec.binary.Base64.encodeBase64(b);
        if (rb == null) {
            return null;
        }
        return new String(rb, StandardCharsets.UTF_8);
    }

    /**
     * @param s
     * @return
     */
    public static String getFromBASE64(String s) {
        if (s == null) {
            return null;
        }
        byte[] b = getBytesBASE64(s);
        if (b == null) {
            return null;
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    /**
     * @param s
     * @return
     */
    public static byte[] getBytesBASE64(String s) {
        if (s == null) {
            return null;
        }
        byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(s.getBytes(StandardCharsets.UTF_8));
        return b;
    }
}
