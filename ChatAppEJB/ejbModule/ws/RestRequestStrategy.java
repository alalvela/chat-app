package ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import db.dao.MessageDAO;
import model.Group;
import model.Host;
import model.Message;
import model.User;
import model.dto.ChatData;
import model.request.RequestMessage;
import model.request.RequestType;
import model.request.ResponseMessage;
import util.ChatAppPropertiesBean;
import util.UrlBuilder;

@SuppressWarnings("unchecked")
public class RestRequestStrategy implements RequestSendingStrategyI {

	private final String fetchChatDataAction = "fetchChatData";
	private final String fetchConversationAction = "fetchConversation";
	
	private MessageDAO messageDAO;
	
	private ChatAppPropertiesBean props;
	
	public RestRequestStrategy() {
		try {
			Context context = new InitialContext();
			messageDAO = (MessageDAO)context.lookup("java:module/MessageDAO");
			props = (ChatAppPropertiesBean)context.lookup("java:module/ChatAppPropertiesBean");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ResponseMessage sendLoginRequest(RequestMessage rm, Host host) {
		JsonObject obj = rm.getObj();
		User user = (User)WSMessageAdapter.getRequestParameter(obj, RequestType.LOGIN);
		user.setHost(host);
		
		Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getUserAppName(), "user", "login");
		Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).post(Entity.json(user));
        User ret = response.readEntity(User.class);
       
        String status = ret.getUsername().equals("") ? "failure" : "success";
        return new ResponseMessage("login", status, ret);
	}

	@Override
	public ResponseMessage sendLogoutRequest(RequestMessage rm) {
		JsonObject obj = rm.getObj();
		User user = (User)WSMessageAdapter.getRequestParameter(obj, RequestType.LOGOUT);
	
		Client client=ClientBuilder.newClient();
		String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getUserAppName(), "user", "logout");
		Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).post(Entity.json(user));
    	String ret = response.readEntity(String.class);
       
        String status = Boolean.valueOf(ret) ? "success" : "failure";
        return new ResponseMessage("logout", status, ret);
	}

	@Override
	public ResponseMessage sendRegisterRequest(RequestMessage rm) {
		return null;
	}

	@Override
	public ResponseMessage fetchChatData(RequestMessage rm) {
		String loggedInUsername = rm.getLoggedInUsername();
		if(loggedInUsername.equals("")) {
			return new ResponseMessage(fetchChatDataAction, "failure", "");
		}
		Client client=ClientBuilder.newClient();
		String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getUserAppName(), "user", "getActiveUsers");
		Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).get();
        List<User> activeUsers = response.readEntity(new GenericType<List<User>>(){});
        activeUsers = activeUsers.stream()
        		.filter(user -> !user.getUsername().equals(loggedInUsername)).collect(Collectors.toList());

		String targetUrl2 = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "group", "getGroups");
		targetUrl2 += "/" + loggedInUsername;
		Response responseGroup = client.target(targetUrl2).request(MediaType.APPLICATION_JSON).get();
		List<Group> groups = responseGroup.readEntity(new GenericType<List<Group>>(){});
        
		return new ResponseMessage(fetchChatDataAction, "success", new ChatData(loggedInUsername, activeUsers, groups));
	}

	
	@Override
	public ResponseMessage fetchConversationData(RequestMessage rm) {
		String reciever = rm.getLoggedInUsername();
		if(reciever.equals("")) {
			return new ResponseMessage(fetchConversationAction, "failure", "");
		}
		User sender = (User)WSMessageAdapter.getRequestParameter(rm.getObj(), RequestType.CONVERSATION_DATA);
		
		Client client=ClientBuilder.newClient();
		String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "message", "get");
		targetUrl += "/" + sender.getUsername() + "," + reciever;
    	
    	Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).get();
    	List<Message> messages = response.readEntity(new GenericType<List<Message>>(){});
		
		return new ResponseMessage(fetchConversationAction, "success", messages);
	}
	//kopiraj ovo skroz za groupCHat i samo nek ti sender bude ime grupe umjesto sto je do sad user drugi

	@Override
	public ResponseMessage fetchGroupConversationData(RequestMessage rm) {
		User sender = (User)WSMessageAdapter.getRequestParameter(rm.getObj(), RequestType.CONVERSATION_DATA);
		String groupName = sender.getUsername();

		Client client=ClientBuilder.newClient();
		String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "group", "getMessagesFromGroup");
		targetUrl += "/" + groupName;
		
		Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).get();
    	List<Message> messages = response.readEntity(new GenericType<List<Message>>(){});

		//rest poziv za sve poruke iz jedne grupe
		
    	return new ResponseMessage("fetchGroupConversation", "success", messages);
	}
}
