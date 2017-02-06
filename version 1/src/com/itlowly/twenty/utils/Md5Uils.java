package com.itlowly.twenty.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Uils {
	/**
	 * @param password
	 * @return
	 */
	public static String encode(String password){
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(password.getBytes());
			
			StringBuffer sb = new StringBuffer();
			
			for(byte b : digest){
				int i = b & 0xff;
				
				String hexString = Integer.toHexString(i);
				
				if(hexString.length()<2){
					hexString = "0"+hexString;
				}
				
				sb.append(hexString);
			}
			
			return sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return " ";
	}
	
	/**
	 * 获取文件的md5 病毒特征码
	 * @param filePath 文件路径名
	 * @return
	 */
	public static String GetFileMd5(String filePath){
		File file = new File(filePath);
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			MessageDigest digest = MessageDigest.getInstance("md5");
			while ((len = fis.read(buffer))!=-1) {
				digest.update(buffer, 0, len);
			}
			fis.close();
			byte[] result = digest.digest();
			StringBuffer sb = new StringBuffer();
			for(byte b : result){
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if(hexString.length()<2){
					hexString = "0"+hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
