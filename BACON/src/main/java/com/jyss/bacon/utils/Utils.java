package com.jyss.bacon.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;

public class Utils {

	// ///////////
	public static List<Long> stringToLongList(String str, String splitStr) {
		List<Long> list = new ArrayList<Long>();
		if (str == null || str.isEmpty()) {
			return list;
		}
		String[] arr = str.split(splitStr);
		for (String id : arr) {
			list.add(Long.valueOf(id));
		}
		return list;
	}

	public static List<String> stringToStringList(String str, String splitStr) {
		List<String> list = new ArrayList<String>();
		if (str == null || str.isEmpty()) {
			return list;
		}
		String[] arr = str.split(splitStr);
		for (String id : arr) {
			list.add(id);
		}
		return list;
	}

	public static String[] stringtoArray(String str, String splitStr) {
		String[] arr = null;
		if (str != null && !(str.isEmpty())) {
			arr = str.split(splitStr);
		}
		return arr;
	}

	public static boolean saveUpload(MultipartFile file, String filePath) {

		File saveDir = new File(filePath);
		if (!saveDir.getParentFile().exists()) {
			saveDir.getParentFile().mkdirs();
		}
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件路径转存
				file.transferTo(saveDir);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	////补位函数///
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);// 左补0
				// sb.append(str).append("0");//右补0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}


	// 随机生成四位数字
	public static String getSaltFour() {
		StringBuilder str = new StringBuilder();// 定义变长字符串
		Random random = new Random();
		// 随机生成数字，并添加到字符串
		for (int i = 0; i < 4; i++) {
			str.append(random.nextInt(10));
		}
		// 将字符串转换为String
		return str.toString();

	}

	////生成ID的
	public static String getMyId(String id) {
		String reStr  ="";
		String bwStr = addZeroForNum(id,6);
		String rStr = getSaltFour();
		reStr = rStr+bwStr;
		return reStr;

	}

	/**
	 * 时间戳加两位随机数
	 * @return
	 */
	public static long getItemId() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//long millis = System.nanoTime();
		//加上两位随机数
		Random random = new Random();
		int end2 = random.nextInt(99);
		//如果不足两位前面补0
		String str = millis + String.format("%02d", end2);
		long id = new Long(str);
		return id;
	}


	public static void main(String[] args)  {
		System.out.println(getMyId("5"));
		System.out.println(getMyId("555"));
		System.out.println(Utils.getItemId());

	}




}
