package com.manyun.business.config.unionUtil;

import java.util.Random;


public class GetRandomNum {
	/**
	 * 生成定长的随机数
	 * @param length
	 * @return
	 */
	public static String createData(int length) {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<length;i++)
        {
            sb.append(rand.nextInt(10));
        }
        String data=sb.toString();
        return data;
    }
	
}
