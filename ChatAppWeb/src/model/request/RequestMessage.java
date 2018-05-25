package model.request;

import com.google.gson.JsonObject;

import model.User;

public class RequestMessage {
	
	private RequestType type;
	private String loggedInUsername;
	private JsonObject obj;
	
	
	public RequestMessage() {
		super();
	}
	
	public RequestMessage(RequestType type, String username, JsonObject obj) {
		super();
		this.loggedInUsername = username;
		this.type = type;
		this.obj = obj;
	}

	public RequestType getRequestType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public JsonObject getObj() {
		return obj;
	}

	public void setObj(JsonObject obj) {
		this.obj = obj;
	}

	public String getLoggedInUsername() {
		return loggedInUsername;
	}

	public void setLoggedInUsername(String loggedInUsername) {
		this.loggedInUsername = loggedInUsername;
	}
	
	
	
	
	
	

}
