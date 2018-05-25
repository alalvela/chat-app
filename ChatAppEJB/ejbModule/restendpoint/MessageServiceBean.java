package restendpoint;

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

import org.codehaus.jackson.map.ObjectMapper;

import db.dao.MessageDAO;
import model.Message;
import model.request.ResponseMessage;
import session.UserSessionManager;

/**
 * Session Bean implementation class MessageServiceBean
 */
@Stateless
@LocalBean
@Path("/message")
public class MessageServiceBean {

	@EJB
	MessageDAO messageDAO;
	
	@EJB
	UserSessionManager userSessionManager;
    
	@GET
	@Path("/get/{fromto}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessagesFromUserToUser(@PathParam("fromto") String fromto) {
		String[] parts = fromto.split(",");
		return messageDAO.getAllMessagesFromUserToUser(parts[0], parts[1]);
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message insertMessage(Message message) {
		return messageDAO.insertMessage(message);
	}
	
	@POST
	@Path("/sendToHost")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendToHost(ResponseMessage resMsg) {
		ObjectMapper om = new ObjectMapper();
		Object o = resMsg.getObject();
		Message message = om.convertValue(o, Message.class);
		
		String sender = message.getSender();
		String reciever = message.getReciever();
//		userSessionManager.sendMessageToUser(sender, resMsg);
		userSessionManager.sendMessageToUser(reciever, resMsg);
	}

}
