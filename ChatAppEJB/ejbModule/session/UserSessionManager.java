package session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.websocket.Session;

import com.google.gson.Gson;

import model.User;
import model.request.ResponseMessage;

@Singleton
@LocalBean
public class UserSessionManager {

   private Map<Session, User> map;

   @PostConstruct
   private void init() {
	   map = new HashMap<>();
   }
   
   public void add(Session s, User u) {
	   map.put(s, u);
   }
   
   public void remove(Session s) {
	   map.remove(s);
   }
   
   public User getUserFromSession(Session s) {
	   return map.get(s);
   }
   
   public boolean containsSession(Session s) {
	   return map.containsKey(s);
   }
   
   public void notifyAllOfNewActiveUser(User user) {
	   ResponseMessage rm = new ResponseMessage("addActiveUser", "success", user);
	   String msg = new Gson().toJson(rm);
	   
	   map.forEach((key, val) -> {
		   if (!val.equals(user)) {
			   try {
				key.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		   }
	   });
   }
   
   public void notifyAllOfRemoveActiveUser(User user) {
	   ResponseMessage rm = new ResponseMessage("removeActiveUser", "success", user);
	   String msg = new Gson().toJson(rm);
	   
	   map.forEach((key, val) -> {
		   if (!val.equals(user)) {
			   try {
				key.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		   }
	   });
   }
   
   public void sendMessageToUser(String username, ResponseMessage resMsg) {
	   String msg = new Gson().toJson(resMsg);
	   map.forEach((key, val) -> {
		   if (val.getUsername().equals(username)) {
			   try {
				key.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		   }
	   });
   }

}
