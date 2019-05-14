package com.coe.oom.core.base.entity;
/**
 * 基础配置类
 * @author lqg
 *
 */
public class BasicConfig {

	private String fileUri;//文件服务器地址
	
	private String tempPath;//本地临时目录
	
	private String importTempPath;//文件导入临时目录
	
	private String exportTempPath;//文件导出临时目录
	
	private String applicationId;//应用id

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getImportTempPath() {
		return importTempPath;
	}

	public void setImportTempPath(String importTempPath) {
		this.importTempPath = importTempPath;
	}

	public String getExportTempPath() {
		return exportTempPath;
	}

	public void setExportTempPath(String exportTempPath) {
		this.exportTempPath = exportTempPath;
	}

	 
	
	
}
