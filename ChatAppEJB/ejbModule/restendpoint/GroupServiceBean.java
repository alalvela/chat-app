package restendpoint;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import db.dao.GroupDAO;
import model.Group;
import model.Message;

/**
 * Session Bean implementation class GroupServiceBean
 */
@Stateless
@LocalBean
@Path("/group")
public class GroupServiceBean {
	
	@EJB
	GroupDAO groupDAO;
	
	@GET
	@Path("/getGroups/{member}")
	@Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroups(@PathParam("member") String member) {
    	return groupDAO.getGroupsFromMember(member);
    }
	
	//add message id to existing group
	@GET
	@Path("/addMessage/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public void addMessageIdToGroup(@PathParam("param") String param) {
		String[] parts = param.split(",");
		String groupName = parts[0];
		String messageId = parts[1];
		groupDAO.addMessageIdToGroup(groupName, messageId);
	}
	
	@GET
	@Path("/getMessagesFromGroup/{group}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getAllMesagesFromGroup(@PathParam("group") String group) {
		return groupDAO.getMessagesFromGroup(group);
	}
	
	@GET
	@Path("/getUsernamesFromGroup/{group}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getUsernamesFromGroup(@PathParam("group") String group) {
		return groupDAO.getGroupMemberUsernames(group);
	}

    
}
