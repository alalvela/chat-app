package dto;

public class Friendship {
	
	private String sender;
	private String reciever;
	
	public Friendship() {
		
	}
	
	public Friendship(String sender, String reciever) {
		super();
		this.sender = sender;
		this.reciever = reciever;
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
	
	
	
	

}
