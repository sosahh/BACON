package com.jyss.bacon.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadUtils {
	/**
	 * @功能 下载临时素材接口
	 * @param fileName
	 *            文件将要保存的目录
	 * @param method
	 *            请求方法，包括POST和GET
	 * @param url
	 *            请求的路径
	 * @return
	 */

	public static void saveUrlAs(String url, OutputStream out, String fileName,
			String method) {
		// System.out.println("fileName---->"+filePath);
		// 创建不同的文件夹目录
		// File file = new File(filePath);
		// 判断文件夹是否存在
		// if (!file.exists()) {
		// // 如果文件夹不存在，则创建新的的文件夹
		// file.mkdirs();
		// }
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			// 建立链接
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			// 以Post方式提交表单，默认get方式
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// post方式不能使用缓存
			conn.setUseCaches(false);
			// 连接指定的资源
			conn.connect();
			// 获取网络输入流
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			// 判断文件的保存路径后面是否以/结尾
			// if (!filePath.endsWith("/")) {
			//
			// filePath += "/";
			//
			// }
			// 写入到文件（注意文件保存路径的后面一定要加上文件的名称）
			// fileOut = new FileOutputStream(filePath + File.separator +
			// fileName);
			// BufferedOutputStream bos = new BufferedOutputStream(fileOut);

			byte[] buf = new byte[4096];
			int length = bis.read(buf);
			int size = 0;
			// 保存文件
			while (length != -1) {
				out.write(buf, 0, length);
				length = bis.read(buf);
				size += length;
			}
			System.out.println("文件大小" + size);
			out.close();
			bis.close();
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("抛出异常！！");
		}

		// return file;

	}



	public static Boolean download(String urlPath,String path){
		try {
			// 构造URL
			URL url = new URL(urlPath);
			// 打开连接
			URLConnection con = url.openConnection();
			//设置请求超时为5s
			con.setConnectTimeout(5*1000);
			// 输入流
			InputStream is = con.getInputStream();

			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			File sf = new File(path);

			OutputStream os = new FileOutputStream(sf);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
			return true;

		} catch (IOException e) {
			return false;

		}
	}



	// public static void main(String[] args) {
	// String photoUrl = "http://192.168.0.26:8080/uploadVedio/11.mp4";
	// String fileName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
	// // System.out.println("fileName---->"+fileName);
	// String filePath = "d:" + File.separator + "MST" + File.separator
	// + "uploadVedio";
	// File file = saveUrlAs(photoUrl, filePath, fileName, "GET");
	// System.out.println("Run ok!/n<BR>Get URL file " + file + File.separator
	// + fileName);
	//
	// }

	public static void main(String[] args) {
		System.out.println(DownLoadUtils.download("http://img.zcool.cn/community/018d4e554967920000019ae9df1533.jpg@900w_1l_2o_100sh.jpg",
				"E://apache-tomcat-7.0.83//webapps//uploadAuthPic//123.png"));
	}
}
