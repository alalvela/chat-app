package restclient;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import db.dao.MessageDAO;
import model.Host;
import model.Message;
import model.request.ResponseMessage;
import util.ChatAppPropertiesBean;
import util.UrlBuilder;


@Stateless
@LocalBean
public class MessageServiceClientBean {
	
	@EJB
	MessageDAO messageDAO;
	
	@EJB
	ChatAppPropertiesBean props;
	
	public Message saveMessage(Message message) {	
		//rest poziv za ovo sitaro
		Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "message", "add");
        Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).post(Entity.json(message));
        Message ret = response.readEntity(Message.class);
		return ret;
	}

	public void sendToHost(ResponseMessage rm, Host host) {
		Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(host.getAddress(), props.getChatAppName(), "message", "sendToHost");
        client.target(targetUrl).request().post(Entity.json(rm));
	}
    
}
