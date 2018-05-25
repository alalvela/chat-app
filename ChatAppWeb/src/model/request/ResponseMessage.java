package model.request;

public class ResponseMessage {
	
	private String action;
	private String status;
	private Object object;
	
	public ResponseMessage() {
		super();
	}

	public ResponseMessage(String action, String status, Object object) {
		super();
		this.action = action;
		this.status = status;
		this.object = object;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
}
