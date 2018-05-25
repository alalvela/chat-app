package ws;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.User;
import model.request.RequestMessage;
import model.request.ResponseMessage;
import session.UserSessionManager;
import util.ChatAppPropertiesBean;
import util.UrlBuilder;

@ServerEndpoint("/wsep")
@Singleton
public class WSEndpoint {
	
	@EJB
	ChatAppPropertiesBean props;
	
	@EJB
	WSMessageParser msgParser;
	
	@EJB
	ClientRequestRouter requestRouter;
	
	@EJB
	UserSessionManager userSessionManager;
	
	private Gson gson = new Gson();
	
	
	@OnOpen
	public void open(Session session) {
		
	}
	
	@OnClose
	public void close(Session session) {
		//send logout request
		User user = userSessionManager.getUserFromSession(session);
		
		Client client=ClientBuilder.newClient();
		String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getUserAppName(), "user", "logout");
		client.target(targetUrl).request(MediaType.APPLICATION_JSON).post(Entity.json(user));
	}
	
	@OnError
	public void onError(Session session, Throwable t) {
		
	}
	
	@OnMessage
	public void handleMessage(Session session, String msg, boolean last) throws IOException {
		System.out.println("Stigla poruka sa fronta:::: " + msg);
		
		RequestMessage requestMsg = msgParser.parseMessage(msg);	
		ResponseMessage responseMsg = requestRouter.routeRequest(requestMsg, session);	//JMS || REST
				
		String strResMsg = gson.toJson(responseMsg);
		session.getBasicRemote().sendText(strResMsg);
	}
}
