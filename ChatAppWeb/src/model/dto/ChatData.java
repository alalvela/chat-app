package model.dto;

import java.util.List;

import model.Group;
import model.User;

public class ChatData {
	
	private String loggedInUsername;
	private List<User> activeUsers;
	private List<Group> groups;
	//chat history?
		
	public ChatData() {
		super();
	}
	
	public ChatData(String loggedInUsername, List<User> activeUsers, List<Group> groups) {
		super();
		this.loggedInUsername = loggedInUsername;
		this.activeUsers = activeUsers;
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getLoggedInUsername() {
		return loggedInUsername;
	}

	public void setLoggedInUsername(String loggedInUsername) {
		this.loggedInUsername = loggedInUsername;
	}

	public List<User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<User> activeUsers) {
		this.activeUsers = activeUsers;
	}
	
	

	
	

}
