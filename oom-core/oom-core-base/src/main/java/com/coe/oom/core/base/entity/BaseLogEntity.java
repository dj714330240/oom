package com.coe.oom.core.base.entity;

public class BaseLogEntity extends BaseEntity {

	private Boolean success;// 是否成功

	private String args;// 参数
	
	private String resultData;// 响应参数(用于业务的参数)
	
	private String resultMsg;//响应信息(描述)

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}



	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getResultData() {
		return resultData;
	}

	public void setResultData(String resultData) {
		this.resultData = resultData;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	
	
}
