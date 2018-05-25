package model.dto;

public class SendMessage {
	
	private String username;
	private String messageContent;
	private boolean isGroup;
	
	public SendMessage() {
		super();
	}

	public SendMessage(String username, String messageContent, boolean isGroup) {
		super();
		this.username = username;
		this.messageContent = messageContent;
		this.isGroup = isGroup;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public boolean getIsGroup() {
		return this.isGroup;
	}
	
	public void setIsGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	

}
