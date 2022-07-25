package com.manyun.business.config.unionUtil;

import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class SM2 {

    public static String sm2Encrypt(String plainText, String pubKey) {
        byte[] data = SM2.encrypt(plainText, pubKey);
        String enData = Base64.encode(data);
        return enData;
    }

    public static String sm2Decrypt(String plainText, String priKey) {
        byte[] encryptData = Base64.decode(plainText);
        String rawData = SM2.decrypt(encryptData, priKey);
        return rawData;
    }

    //国密办文件中推荐的椭圆曲线相关参数

    private static BigInteger n = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);
    private static BigInteger p = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);
    private static BigInteger a = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);
    private static BigInteger b = new BigInteger(
            "28E9FA9E" + "9D9F5E34" + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41" + "4D940E93", 16);
    private static BigInteger gx = new BigInteger(
            "32C4AE2C" + "1F198119" + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589" + "334C74C7", 16);
    private static BigInteger gy = new BigInteger(
            "BC3736A2" + "F4F6779C" + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5" + "2139F0A0", 16);
    private static final int DIGEST_LENGTH = 32;
    private static SecureRandom random = new SecureRandom();
    private static ECCurve.Fp curve = new ECCurve.Fp(p, a, b);
    private static ECPoint G = curve.createPoint(gx, gy);
    private static ECDomainParameters ecc_bc_spec = new ECDomainParameters(curve, G, n);

    /**
     * 以16进制打印字节数组
     *
     * @param b
     */
    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }
        System.out.println();
    }

    /**
     * 随机数生成器
     *
     * @param max
     * @return
     */
    private static BigInteger random(BigInteger max) {

        BigInteger r = new BigInteger(256, random);
        while (r.compareTo(max) >= 0) {
            r = new BigInteger(128, random);

        }
        return r;
    }

    /**
     * 判断字节数组是否全0
     *
     * @param buffer
     * @return
     */
    private static boolean allZero(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 公钥加密
     *
     * @param input     加密原文
     * @param pubKeyStr 公钥
     * @return
     */
    public static byte[] encrypt(String input, String pubKeyStr) {
        ECPoint publicKey = curve.decodePoint(hexStr2Bytes(pubKeyStr));
        byte[] inputBuffer = new byte[0];
        try {
            inputBuffer = input.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] C1Buffer;
        ECPoint kpb;
        byte[] t;
        do {
            /* 1 产生随机数k，k属于[1, n-1] */
            BigInteger k = random(n);
            /* 2 计算椭圆曲线点C1 = [k]G = (x1, y1) */
            ECPoint C1 = G.multiply(k);
            C1Buffer = C1.getEncoded(false);
            /*
             * 3 计算椭圆曲线点 S = [h]Pb
             */
            BigInteger h = ecc_bc_spec.getH();
            if (h != null) {
                ECPoint S = publicKey.multiply(h);
                if (S.isInfinity()) {
                    throw new IllegalStateException();
                }
            }
            /* 4 计算 [k]PB = (x2, y2) */
            kpb = publicKey.multiply(k).normalize();
            /* 5 计算 t = KDF(x2||y2, klen) */
            byte[] kpbBytes = kpb.getEncoded(false);
            t = KDF(kpbBytes, inputBuffer.length);
        } while (allZero(t));
        /* 6 计算C2=M^t */
        byte[] C2 = new byte[inputBuffer.length];
        for (int i = 0; i < inputBuffer.length; i++) {
            C2[i] = (byte) (inputBuffer[i] ^ t[i]);
        }
        /* 7 计算C3 = Hash(x2 || M || y2) */
        byte[] C3 = sm3hash(kpb.getXCoord().toBigInteger().toByteArray(), inputBuffer,
                kpb.getYCoord().toBigInteger().toByteArray());
        /* 8 输出密文 C=C1 || C2 || C3 */
        byte[] encryptResult = new byte[C1Buffer.length + C2.length + C3.length];
        System.arraycopy(C1Buffer, 0, encryptResult, 0, C1Buffer.length);
        System.arraycopy(C2, 0, encryptResult, C1Buffer.length, C2.length);
        System.arraycopy(C3, 0, encryptResult, C1Buffer.length + C2.length, C3.length);
        return encryptResult;
    }

    /**
     * 私钥解密
     *
     * @param encryptData 密文数据字节数组
     * @param priKeyStr   解密私钥
     * @return
     */
    public static String decrypt(byte[] encryptData, String priKeyStr) {


        BigInteger privateKey = new BigInteger(priKeyStr, 16);

        byte[] C1Byte = new byte[65];
        System.arraycopy(encryptData, 0, C1Byte, 0, C1Byte.length);

        ECPoint C1 = curve.decodePoint(C1Byte).normalize();

        /*
         * 计算椭圆曲线点 S = [h]C1 是否为无穷点
         */
        BigInteger h = ecc_bc_spec.getH();
        if (h != null) {
            ECPoint S = C1.multiply(h);
            if (S.isInfinity()) {
                throw new IllegalStateException();
            }
        }
        /* 计算[dB]C1 = (x2, y2) */
        ECPoint dBC1 = C1.multiply(privateKey).normalize();

        /* 计算t = KDF(x2 || y2, klen) */
        byte[] dBC1Bytes = dBC1.getEncoded(false);
        int klen = encryptData.length - 65 - DIGEST_LENGTH;
        byte[] t = KDF(dBC1Bytes, klen);

        if (allZero(t)) {
            System.err.println("all zero");
            throw new IllegalStateException();
        }

        /* 计算M'=C2^t */
        byte[] M = new byte[klen];
        for (int i = 0; i < M.length; i++) {
            M[i] = (byte) (encryptData[C1Byte.length + i] ^ t[i]);
        }

        /*  计算 u = Hash(x2 || M' || y2) 判断 u == C3是否成立 */
        byte[] C3 = new byte[DIGEST_LENGTH];


        System.arraycopy(encryptData, encryptData.length - DIGEST_LENGTH, C3, 0, DIGEST_LENGTH);
        byte[] u = sm3hash(dBC1.getXCoord().toBigInteger().toByteArray(), M,
                dBC1.getYCoord().toBigInteger().toByteArray());

        if (Arrays.equals(u, C3)) {
            try {
                return new String(M, "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 判断是否在范围内
     *
     * @param param
     * @param min
     * @param max
     * @return
     */
    private static boolean between(BigInteger param, BigInteger min, BigInteger max) {
        if (param.compareTo(min) >= 0 && param.compareTo(max) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断生成的公钥是否合法
     *
     * @param publicKey
     * @return
     */
    private static boolean checkPublicKey(ECPoint publicKey) {
        if (!publicKey.isInfinity()) {
            BigInteger x = publicKey.getXCoord().toBigInteger();
            BigInteger y = publicKey.getYCoord().toBigInteger();
            if (between(x, new BigInteger("0"), p) && between(y, new BigInteger("0"), p)) {
                BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);
                BigInteger yResult = y.pow(2).mod(p);
                if (yResult.equals(xResult) && publicKey.multiply(n).isInfinity()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字节数组拼接
     *
     * @param params
     * @return
     */
    private static byte[] join(byte[]... params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] res = null;
        try {
            for (int i = 0; i < params.length; i++) {
                baos.write(params[i]);
            }
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * sm3摘要
     * @param params
     * @return
     */
    private static byte[] sm3hash(byte[]... params) {
        byte[] res = null;
        try {
            res = SM3.hash(join(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 密钥派生函数
     * @param Z
     * @param klen 生成klen字节数长度的密钥
     * @return
     */
    private static byte[] KDF(byte[] Z, int klen) {
        int ct = 1;
        int end = (int) Math.ceil(klen * 1.0 / 32);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            for (int i = 1; i < end; i++) {
                baos.write(sm3hash(Z, SM3.toByteArray(ct)));
                ct++;
            }
            byte[] last = sm3hash(Z, SM3.toByteArray(ct));
            if (klen % 32 == 0) {
                baos.write(last);
            } else {
                baos.write(last, 0, klen % 32);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] hexStr2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; ++i) {
            int m = i * 2 + 1;
            int n = m + 1;
            ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
        }
        return ret;
    }

    private static byte uniteBytes(String src0, String src1) {
        byte b0 = Byte.decode("0x" + src0);
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1);
        byte ret = (byte) (b0 | b1);
        return ret;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String pubKeyStr = "";//测试SM2公钥
        String priKeyStr = "";//测试SM2私钥
        String body = SM2.sm2Encrypt("womenyiqi", pubKeyStr);
        System.out.println("密文:" + body);
        //String st = "BEBj+2M/IBZS8z8uxDXP5clOxfTQWs8voktESHbRVkaz3Iy3g+Dtl1hY4dJg9Ldyz/ePAsZzQDKV\\r\\naKdf2lDBmq3gzJusfN3sYlw5Qsbh7K0qWhUh/DiwFhag/X1rrQeSJ7go6IK1+GZe8NZPWMFwFW12\\r\\nQS+Awd+FJYtEwy14Vlmy4kw8qtLIfxlOYH++N3RAgXFHpZWDtGkaN4wPq0WiC1Wm92kWMQD4M1N4\\r\\nD72i8cs21Q==";
        String rawData = SM2.sm2Decrypt(body, priKeyStr);
        System.out.println("解密后明文:" + rawData);
    }
}