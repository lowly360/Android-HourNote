package com.itlowly.twenty.bean;

import cn.bmob.v3.BmobObject;

public class FileNameTable extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fileLocateName;
	private String fileServerName;
	
	public String getFileLocateName() {
		return fileLocateName;
	}
	public void setFileLocateName(String fileLocateName) {
		this.fileLocateName = fileLocateName;
	}
	public String getFileServerName() {
		return fileServerName;
	}
	public void setFileServerName(String fileServerName) {
		this.fileServerName = fileServerName;
	}
	
	
}
