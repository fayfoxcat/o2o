package org.fox.o2o.util;

import java.security.MessageDigest;
public class MD5 {

	/**
	 * 对传入的String进行加密
	 * @param s
	 * @return
	 */
    public static final String getMd5(String s){
        char hexDigits[] = { 
        	'5', '0', '5', '6', '2', '9', 
        	'6', '2', '5', 'q','b', 'l', 
        	'e', 's', 's', 'y'
        };
        try{
        	char str[];
        	//将传入的字符串抓换为byte字符数组
	        byte strTemp[] = s.getBytes();
	        //获取MD5加密对象
	        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	        //传入需要加密的目标数组
	        mdTemp.update(strTemp);
	        //获取加密后的数组
	        byte md[] = mdTemp.digest();
	        int j = md.length;
	        str = new char[j * 2];
	        int k = 0;
	        //将数组做位移
	        for (int i = 0; i < j; i++){
	            byte byte0 = md[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexDigits[byte0 & 0xf];
	        }
	        //转换为字符串并返回
	        return new String(str);
        }catch(Exception e){
        	return null;
        }
    }
}

