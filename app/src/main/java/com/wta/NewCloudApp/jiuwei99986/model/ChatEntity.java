package com.wta.NewCloudApp.jiuwei99986.model;

/**
 * 消息体类
 */
public class ChatEntity {
	
	private String grpSendName;
	private String context;
	private int  type;
	private int sign=0;

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public ChatEntity() {
		// TODO Auto-generated constructor stub
	}
	


	public String getSenderName() {
		return grpSendName;
	}

	public void setSenderName(String grpSendName) {
		this.grpSendName = grpSendName;
	}
		


	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
