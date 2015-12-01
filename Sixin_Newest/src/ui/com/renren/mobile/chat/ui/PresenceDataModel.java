package com.renren.mobile.chat.ui;

public class PresenceDataModel {
	private long fromId;
	private long toId;
	private int type;
	
	public PresenceDataModel(long from,long to, int type) {
		this.fromId=from;
		this.toId=to;
		this.type=type;
	}
	public long getFromId() {
		return fromId;
	}

	public void setFromId(long fromId) {
		this.fromId = fromId;
	}

	public long getToId() {
		return toId;
	}

	public void setToId(long toId) {
		this.toId = toId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}



}
