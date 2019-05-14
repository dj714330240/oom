package com.coe.oom.core.base.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFileToFileServer {

	// 接收文件的服务器地址
//	private static final String serverUrl = "http://47.90.76.15:7000/fileserver/upload";

	/***
	 * 上传文件到文件服务器，得到返回的文件的网络地址并返回给调用程序
	 * 
	 * @param f
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String fileUpload(byte[] bytes, String fileName,String serverUrl) throws Exception {
		String resultSet = null;

		try {
			HttpURLConnection huc = (HttpURLConnection) new URL(serverUrl).openConnection();

			huc.setRequestMethod("POST");// 设置提交方式为post方式
			huc.setDoInput(true);
			huc.setDoOutput(true);// 设置允许output
			huc.setUseCaches(false);// POST不能使用缓存
			// 设置请求头信息
			huc.setRequestProperty("Connection", "Keep-Alive");
			huc.setRequestProperty("Charset", "UTF-8");

			// 设置边界
			String boundary = "----------" + System.currentTimeMillis();
			huc.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			// 头部：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // ////////必须多两道线
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n");
			sb.append("Content-Type: application/octet-stream\r\n\r\n");

			// 获得输出流
			OutputStream out = new DataOutputStream(huc.getOutputStream());
			out.write(sb.toString().getBytes("utf-8"));// 写入header
			// 文件数据部分
			out.write(bytes, 0, bytes.length);// 写入文件数据

			// 结尾部分
			byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);// 写入尾信息

			out.flush();
			out.close();

			// 执行提交后获取执行结果
			BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
			huc.connect();
			String line = null;
			resultSet = br.readLine();

			// 循环按行读取文本流
			while ((line = br.readLine()) != null) {
				resultSet += line + "\r\n";// 此处未加上\r\n
			}
			br.close();
			resultSet = resultSet.trim();
			huc.disconnect();
		} catch (Exception e) {
			throw e;
		}
		return resultSet;
	}

	public static String fileUpload(String sourcefile, String asfileName,String serverUrl) {
		byte[] buffer = null;
		try {
			File file = new File(sourcefile);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
			return fileUpload(buffer, asfileName, serverUrl);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		String fileName="out-warehouse-template(GW).xlsx";
		String fileName="test.xlsx";
//		String str = fileUpload("E:\\temp\\import\\1541389569248939.xlsx", fileName );
//		System.out.println(str);
	}
}
