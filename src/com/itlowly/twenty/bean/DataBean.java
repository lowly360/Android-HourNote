package com.itlowly.twenty.bean;

public class DataBean {
	private String title;
	private String content;
	private String data;
	private String time;
	private String type;
	
	public DataBean(String title, String content, String data, String time,
			String type) {
		super();
		this.title = title;
		this.content = content;
		this.data = data;
		this.time = time;
		this.type = type;
	}

	public DataBean() {
		
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final String getData() {
		return data;
	}

	public final void setData(String data) {
		this.data = data;
	}

	public final String getTime() {
		return time;
	}

	public final void setTime(String time) {
		this.time = time;
	}

	public final String getType() {
		return type;
	}

	public final void setType(String type) {
		this.type = type;
	}

	
	
}
