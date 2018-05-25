package model.jms;

import java.io.Serializable;

import model.User;

public class ActiveUserMessageJMS implements Serializable{
	
	private String action;
	private User user;
	
	public ActiveUserMessageJMS() {
		super();
	}

	public ActiveUserMessageJMS(String action, User user) {
		super();
		this.action = action;
		this.user = user;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return " action: " + this.getAction() + "\nuser: username: " + this.getUser().getUsername();
	}
}
