package com.coe.oom.core.base.util;

import com.alibaba.fastjson.JSON;
import com.coe.oom.core.base.entity.Message;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件操作类
 * @author lqg
 *
 */
public class FileOperationUtil {
	
	private static String fileUri=null;
	 

	public static void setFileUri(String uri) {
		fileUri = uri;
	}
	
	/**
	 * 删除文件
	 * @param fileHttpUril
	 * @return
	 */
	public static Message deleteFile(String fileHttpUril){
		return deleteFile(fileUri,fileHttpUril);
	}
	
 
	
	/**
	 * 文件上传
	 * @param file
	 * @return
	 */
	public static Message doUpload(File file){
		return doUpload(fileUri, file);
	}

	/**
	 * 删除文件
	 * @param httpUril
	 * @return
	 */
	public static Message deleteFile(String uri,String fileHttpUril){
		Map<String, String> reqMap=new HashMap<String, String>();
		reqMap.put("filePath", fileHttpUril);
		String str = HttpClientUtil.doPost(uri+"/deleteFile", reqMap);
		if(str==null || str.trim().length()==0){
			return Message.error("删除失败");
		}
		else{
			Message resPonse = JSON.parseObject(str, Message.class);
			if(!resPonse.getSuccess()){
				return resPonse;
			}
		}
		return Message.success("删除成功");
	}
	
	/**
	 * 文件下载
	 * 返回  本地绝对路径
	 * @param url
	 * @param localPath 地址(目录)
	 * @return
	 */
	public static Message doDownLoad(String url,String localPath){
		CloseableHttpClient httpclient = HttpClients
				.createDefault();
		CloseableHttpResponse response = null;

		try {
			File file = new File(localPath);
			if(!file.exists()){
				file.mkdirs();
			}
			URIBuilder e = new URIBuilder(url);
			URI urix = e.build();
			HttpGet httpGet = new HttpGet(urix);
			response = httpclient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream content = response.getEntity().getContent();
				String nameSuffix=url.substring(url.lastIndexOf("."),url.length());
				String newName=UUID.randomUUID().toString().replace("_", "").substring(0, 8)+System.nanoTime()+nameSuffix;
				
				FileOutputStream fo = new FileOutputStream(localPath+File.separator+newName);
				byte[] bytes = new byte[4096];

				int length=-1;
				while ((length = content.read(bytes)) != -1) {
					fo.write(bytes, 0, length);
				}
				fo.flush();
				fo.close();
				return Message.success(localPath+File.separator+newName);
				 
			}else{
				return Message.error("系统异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("系统异常");
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	
		
		
	}
	
	/**
	 * 文件上传
	 * 
	 * @param url
	 * @param file
	 * @return
	 */
	public static Message doUpload(String url, File file) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpPost httppost = new HttpPost(url+"/fileUpload");
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.addBinaryBody("file", file);
			HttpEntity build = multipartEntityBuilder.build();
			httppost.setEntity(build);
			// 执行http请求
			response = httpclient.execute(httppost);
			String resultString = EntityUtils.toString(response.getEntity(),"utf-8");
			Message message = JSON.parseObject(resultString, Message.class);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("系统异常");
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (Exception ignore) {

			}
		}
	}

	
}
