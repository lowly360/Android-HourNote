package com.itlowly.twenty.bean;

public class HistoryDataBean {
	private String historyType;
	private String tagName;
	private String title;
	private String content;
	private String data;
	private String time;
	private String type;
	private String isTwenty;

	public HistoryDataBean(String historyType, String tagName, String title,
			String content, String data, String time, String type,
			String isTwenty) {
		super();
		this.historyType = historyType;
		this.tagName = tagName;
		this.title = title;
		this.content = content;
		this.data = data;
		this.time = time;
		this.type = type;
		this.isTwenty = isTwenty;
	}

	public DataBean getDataBean() {

		return new DataBean(this.title, this.content, this.data, this.time,
				this.historyType, this.isTwenty);

	}

	public String getHistoryType() {
		return historyType;
	}

	public void setHistoryType(String historyType) {
		this.historyType = historyType;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public HistoryDataBean() {

	}

	public String getIsTwenty() {
		return isTwenty;
	}

	public void setIsTwenty(String isTwenty) {
		this.isTwenty = isTwenty;
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
