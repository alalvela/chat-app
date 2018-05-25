package restclient;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import db.dao.GroupDAO;
import model.User;
import util.ChatAppPropertiesBean;
import util.UrlBuilder;

/**
 * Session Bean implementation class GroupServiceClientBean
 */
@Stateless
@LocalBean
public class GroupServiceClientBean {

	@EJB
	GroupDAO groupDAO;

	@EJB
	ChatAppPropertiesBean props;
	
	public void addMessageIdToGroup(String groupName, String messageId) {
		Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "group", "addMessage");
        targetUrl += "/" + groupName + "," + messageId;
    	client.target(targetUrl).request().get();
	}
	
	public List<String> getGroupMembersUsernames(String groupName) {
		Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "group", "getUsernamesFromGroup");
        targetUrl += "/" + groupName;
        Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).get();
        List<String> usernames = response.readEntity(new GenericType<List<String>>() { });
		return usernames;
	}

}
