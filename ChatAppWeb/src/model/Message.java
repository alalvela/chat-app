package model;

import java.util.Date;
import java.util.List;

public class Message {

	private String id;
	private String sender;
	private String reciever;
	private String date;
	private String time;
	private String content;
	private boolean isGroup;
	
	public Message() {
		super();
	}

	public Message(String sender, String reciever, String content, boolean isGroup) {
		super();
		this.sender = sender;
		this.reciever = reciever;
		this.content = content;
		this.isGroup = isGroup;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	
	
	
}
