package model;

import java.util.List;

public class Group {

	private Long id;
	private String creator;
	private String name;
	private List<String> members;
	
	public Group() {
		super();
	}

	public Group(Long id, String creator, String name, List<String> members) {
		super();
		this.id = id;
		this.creator = creator;
		this.name = name;
		this.members = members;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}
	
	
	
	
}
