package com.coe.oom.core.base.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于业务交互之间信息传递
 * 
 * @author lqg
 *
 */
public class Message implements Serializable {
	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 4081619286503989887L;
	/**
	 * 是否成功
	 */
	private Boolean success;
	/**
	 * 消息代码 , 由使用者自由发挥
	 */
	private String code;
	/**
	 * 消息内容体 , 由使用者自由发挥
	 */
	private String msg;
	/**
	 * 扩展
	 */
	private Object data;

	/**
	 * 此扩展用于  service-api 返回值时封装绑定在线程中的数据
	 */
	private Object data2;

	public static Message getInstance() {
		Message message = new Message();
		return message;
	}

	public static Message success(String msg) {
		Message message = getInstance();
		message.setSuccess(true);
		message.setMsg(msg);
		return message;
	}

	public static Message success(String msg, String code) {
		Message message = success(msg);
		message.setCode(code);
		return message;
	}

	public static Message success(String msg, Object data) {
		Message message = success(msg);
		message.setData(data);
		return message;
	}

	public static Message success(Object data) {
		Message message = success("");
		message.setData(data);
		return message;
	}

	public static Message success(String msg, String code, Object data) {
		Message message = success(msg);
		message.setCode(code);
		message.setData(data);
		return message;
	}

	public static Message error(String msg) {
		Message message = getInstance();
		message.setSuccess(false);
		message.setMsg(msg);
		return message;
	}

	public static Message error(String msg, String code) {
		Message message = error(msg);
		message.setCode(code);
		return message;
	}

	public static Message error(String msg, Object data) {
		Message message = error(msg);
		message.setData(data);
		return message;
	}

	public static Message error(String msg, String code, Object data) {
		Message message = error(msg);
		message.setCode(code);
		message.setData(data);
		return message;
	}
	
	/***
     * 导入excel 错误时返回的对象
     * @param msg
     * @param errors
     * @return
     */
    public static Message excelImportError(String msg, List<String> errors) {
        Message message = error(msg);
        Map<String, Object> map = new HashMap<>();
        map.put("errors",errors);
        message.setData(map);
        return  message;
    }
	
	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {

		if (msg != null && msg.length() > 512) {// 大于数据库的长度
			return msg.substring(0, 512);
		}

		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Message [success=" + success + ", code=" + code + ", msg=" + msg + ", extend=" + data + "]";
	}

	public Object getData2() {
		return data2;
	}

	public void setData2(Object data2) {
		this.data2 = data2;
	}

	
	public<T> T getDataFromJson(Class<T> clazz){
		Object data = getData();
		if(data instanceof com.alibaba.fastjson.JSONObject){
			return JSONObject.toJavaObject(((com.alibaba.fastjson.JSONObject)data), clazz);	
		}
        if(data instanceof Map){
			String dataStr = JSON.toJSONString(data);
			return JSON.parseObject(dataStr, clazz);
		}
		return null;
	}


	public<T> T getData2FromJson(Class<T> clazz){
		Object data = getData2();
		if(data instanceof com.alibaba.fastjson.JSONObject){
			return JSONObject.toJavaObject(((com.alibaba.fastjson.JSONObject)data), clazz);	
		}else if(data instanceof Map){
			String dataStr = JSON.toJSONString(data);
			return JSON.parseObject(dataStr, clazz);
		}
		return null;
	}
	
	public<T> T getDataFromString(Class<T> clazz){
		String data = (String) getData();
		return JSON.parseObject(data, clazz);
	}
	
	public <T> List<T> getDataFromJsonForList(Class<T> clazz){
		Object data = getData();
		return JSON.parseArray(JSON.toJSONString(data),clazz);
	}

	public boolean isSuccess(){
    	if(success == null){
    		return false;
		}
    	return success;
	}
	
}
