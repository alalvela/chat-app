package ws;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.websocket.Session;

import app.ChatAppMainBean;
import model.Host;
import model.Message;
import model.User;
import model.dto.SendMessage;
import model.request.RequestMessage;
import model.request.RequestType;
import model.request.ResponseMessage;
import restclient.GroupServiceClientBean;
import restclient.MessageServiceClientBean;
import session.UserSessionManager;


@Stateless
@LocalBean
public class ClientRequestRouter {

	@EJB
	ChatAppMainBean mainBean;
	
	@EJB
	MessageServiceClientBean messageClient;
	
	@EJB
	GroupServiceClientBean groupClient;
	
	@EJB
	UserSessionManager userSessionManager;
	
	private RequestSendingStrategyI requestSender;
	
    public ResponseMessage routeRequest(RequestMessage rm, Session session) {
    	
/*    	if (propertiesBean.getMasterHost().equals(propertiesBean.getLocalHost())) {
 *    		//JMS
 *    		requestSender = new JMSRequestStrategy();
 *    	} else {
 *    		//REST
 *   		requestSender = new RestRequestStrategy();
 *    	}
 */    	
    	if (userSessionManager.containsSession(session)) {
			rm.setLoggedInUsername(userSessionManager.getUserFromSession(session).getUsername());			
		}
    	
    	requestSender = new RestRequestStrategy();
    	ResponseMessage resMsg = new ResponseMessage();
    	
    	switch(rm.getRequestType()) {
    	case REGISTER: resMsg = requestSender.sendRegisterRequest(rm);
    		break;
    	case LOGIN: 
	    	{
	    		resMsg = requestSender.sendLoginRequest(rm, mainBean.getLocalHost());
	    		if (resMsg.getStatus().equals("success")) {
	    			userSessionManager.add(session, (User)resMsg.getObject());
	    		}
	    	}
    		break;
    	case LOGOUT:
    		{
    			resMsg = requestSender.sendLogoutRequest(rm);
    			if (resMsg.getStatus().equals("success")) {
	    			userSessionManager.remove(session);
	    		}
    		}
    		break;
    	case CHAT_DATA: resMsg = requestSender.fetchChatData(rm);
    		break;
    	case CONVERSATION_DATA: resMsg = requestSender.fetchConversationData(rm);
    		break;
    	case GROUP_CONVERSATION_DATA: resMsg = requestSender.fetchGroupConversationData(rm);
    		break;
    	case SEND_MESSAGE : 
    		{
    			String sender = rm.getLoggedInUsername();
    			SendMessage sendMsg = (SendMessage)WSMessageAdapter.getRequestParameter(rm.getObj(), RequestType.SEND_MESSAGE);
    			String recieverName = sendMsg.getUsername();
    			Message insertedMsg = messageClient.saveMessage(new Message(sender, sendMsg.getUsername(), 
    					sendMsg.getMessageContent(), sendMsg.getIsGroup()));
    			ResponseMessage responseMsg = new ResponseMessage("addMessageToActiveChat", "success", insertedMsg);  			
    			List<User> activeUsers = mainBean.getActiveUsers();
    			
    			if(!insertedMsg.getIsGroup()) {
    				User reciever = activeUsers.stream().filter(u -> u.getUsername().equals(sendMsg.getUsername())).findFirst().orElse(new User());
    				if(!reciever.getUsername().equals("")) {	//aktivan
    					Host recieverHost = reciever.getHost();
    					userSessionManager.sendMessageToUser(sender, responseMsg);
    					if(recieverHost.equals(mainBean.getLocalHost())) {	//na istom hostu - ws
    						userSessionManager.sendMessageToUser(reciever.getUsername(), responseMsg);
    					} else {	//razl host
    						messageClient.sendToHost(responseMsg, recieverHost);
    					}
    				}
    			} else {
    				//iha
    				//dodaj grupi id poruke preko resta chatapp mainbean
    				groupClient.addMessageIdToGroup(recieverName, insertedMsg.getId());
    				//getuj sve usere iz grupe
    				List<String> usernames = groupClient.getGroupMembersUsernames(recieverName);
    				
//    				userSessionManager.sendMessageToUser(sender, responseMsg);
    				
    				for(User user : activeUsers) {
    					if (usernames.contains(user.getUsername())) {
    						//ako je isti jost
    						if(user.getHost().equals(mainBean.getLocalHost())) {
    							userSessionManager.sendMessageToUser(user.getUsername(), responseMsg);
    						} else {
    							messageClient.sendToHost(responseMsg, user.getHost());
    						}
    						
    						//ako je razl
    					}
    				}
    					
    				
    				
    				//vratio je sve ok sad 
    			}
    			
    			resMsg = new ResponseMessage("sendMessage", "success", null);	
    		}
    		break;
    	//unknown jbg
    	}
    	return resMsg;
    }
    
}
