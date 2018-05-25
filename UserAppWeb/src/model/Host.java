package model;

import java.io.Serializable;

public class Host implements Serializable{
	
	private String address;
	private String alias;
	
	public Host() {
		this.address = "";
		this.alias = "";
	}
	
	public Host(String address, String alias) {
		this.address = address;
		this.alias = alias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String adress) {
		this.address = adress;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
	

}
