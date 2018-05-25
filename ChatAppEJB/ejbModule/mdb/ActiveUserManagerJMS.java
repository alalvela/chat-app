package mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.google.gson.Gson;

import app.ChatAppMainBean;
import model.User;
import restclient.RestClientMasterServiceBean;
import session.UserSessionManager;

@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "queue/TestQueue"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		})
public class ActiveUserManagerJMS implements MessageListener {

	@EJB
	RestClientMasterServiceBean restClient;
	
	@EJB
	ChatAppMainBean mainBean;
	
	@EJB
	UserSessionManager userSessionManager;
	
    public void onMessage(Message message) {
    	if (message instanceof TextMessage) {
    		TextMessage msg = (TextMessage)message;    		
    		try {
    			String action = msg.getStringProperty("action");
    			String userStr = msg.getStringProperty("user");
    			User userFromJson = new Gson().fromJson(userStr, User.class);
    			
    			switch(action) {
    			case "login": sendAddRequest(userFromJson);
    			break;
    			case "logout": sendRemoveRequest(userFromJson);
    			}
			} catch (JMSException e) {
				e.printStackTrace();
			}    		
    	} else {
    		System.out.println("Primljena netekstualna poruka");
    	}
    }
    
    private void sendAddRequest(User user) {
    	mainBean.addActiveUser(user);
    	if(mainBean.getLocalHost().getAlias().equals("master")) {
    		userSessionManager.notifyAllOfNewActiveUser(user);
    	}
    	restClient.sendAddActiveUserRequest(user); 
    }
    
    private void sendRemoveRequest(User user) {
    	mainBean.removeActiveUser(user);
    	if(mainBean.getLocalHost().getAlias().equals("master")) {
    		userSessionManager.notifyAllOfRemoveActiveUser(user);
    	}
    	restClient.sendRemoveActiveUserRequest(user);
    }

}
