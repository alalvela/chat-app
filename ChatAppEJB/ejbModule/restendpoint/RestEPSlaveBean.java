package restendpoint;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import app.ChatAppMainBean;
import model.Host;
import model.User;
import session.UserSessionManager;


@Stateless
@LocalBean
@Path("/slave")
public class RestEPSlaveBean {

	@EJB
	ChatAppMainBean mainBean;
	
	@EJB
	UserSessionManager userSessionManager;
    
	@POST
	@Path("/addNewNode")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNode(Host host) {
		mainBean.addNewNode(host);
	}
	
	@POST
	@Path("/removeNode")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeNode(Host host) {
		mainBean.removeNode(host);
	}
	
	
	@POST
	@Path("/addActiveUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addActiveUser(User user) {
		mainBean.addActiveUser(user);
    	userSessionManager.notifyAllOfNewActiveUser(user);
	}
	
	@POST
	@Path("/removeActiveUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeActiveUser(User user) {
		mainBean.removeActiveUser(user);
		userSessionManager.notifyAllOfRemoveActiveUser(user);
	}
	
	// TEST -----------
	@GET
	@Path("/getLocalNodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Host> getLocalNodes() {
		return mainBean.getHosts();
	}
	
	@GET
	@Path("/getActiveUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getActiveUsers() {
		return mainBean.getActiveUsers();
	}
}
