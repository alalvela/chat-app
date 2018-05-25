package util;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import db.dao.GroupDAO;
import db.dao.MessageDAO;
import model.Group;
import model.Message;

/**
 * Session Bean implementation class TestBean
 */
@Stateless
@LocalBean
@Path("/testMsg")
public class TestBean {

	@EJB
	MessageDAO msgDAO;
	
	@EJB
	GroupDAO groupDAO;
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
    public void insertMessage(Message message) {
    	msgDAO.insertMessage(message);
    }
    
	@POST
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public List<Message> getMessagesFromTo(TestDTO dto) {
    	return msgDAO.getAllMessagesFromUserToUser(dto.getSender(), dto.getReciever());
    }
	
	
	@POST
	@Path("/addGroup")
	@Consumes(MediaType.APPLICATION_JSON)
    public void insertGroup(Group group) {
		groupDAO.insertGroup(group);
	}
	
	@POST
	@Path("/addMember")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addMemberToGroup(TestDTO dto) {
		groupDAO.addMember(dto.getSender(), dto.getReciever());
	}
	
	@POST
	@Path("/removeMember")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeMemberToGroup(TestDTO dto) {
		groupDAO.removeMember(dto.getSender(), dto.getReciever());
	}
	
	
	@GET
	@Path("/getMembers/{group}")
	@Produces(MediaType.APPLICATION_JSON)
    public List<String> getMembersFromGroup(@PathParam("group") String group) {
    	return groupDAO.getGroupMemberUsernames(group);
    }
	
	@GET
	@Path("/getGroups/{member}")
	@Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroups(@PathParam("member") String member) {
    	return groupDAO.getGroupsFromMember(member);
    }
	

}
