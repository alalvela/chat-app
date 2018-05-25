package restendpoint;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import beans.ActiveUserManagerBean;
import db.MongoClientProvider;
import db.dao.FriendshipDAO;
import db.dao.UserDAO;
import dto.Friendship;
import jmsclient.JMSClient;
import model.User;
import model.jms.ActiveUserMessageJMS;


@Stateless
@LocalBean
@Path("/user")
public class UserServiceBean implements UserServiceLocal{

   
	@EJB
	private MongoClientProvider mongoClientProvider;
	
	@EJB
	private ActiveUserManagerBean activeUserManager;
	
	@EJB
	private UserDAO userDAO;
	
	@EJB
	private FriendshipDAO friendshipDAO;
	
	@EJB
	private JMSClient jmsCli;
	
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User register(User user) {
		User found = userDAO.findUser(user.getUsername());
		if (!found.getUsername().equals("")) {
			return new User();
		}
		userDAO.addUser(user);
		return user;
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User login(User user) {
		User found = userDAO.findUser(user.getUsername());
		if (found.getUsername().equals("")) {
			return new User();
		}
		if (!found.getPassword().equals(user.getPassword())) {
			return new User();
		} 
		if (!activeUserManager.checkIfActive(user)) {
			if(activeUserManager.addUserToActive(user)) {
				try {
					jmsCli.sendModifyActiveUserMessage(new ActiveUserMessageJMS("login", user)); //bilo sendAdd
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		user.setPassword("");
		return user;
	}

	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean logout(User user) {
		boolean ret = false;
		if (activeUserManager.checkIfActive(user)) {
			ret = activeUserManager.removeUserFromActive(user);
			if(ret) {
				try {
					jmsCli.sendModifyActiveUserMessage(new ActiveUserMessageJMS("logout", user));
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	@GET
	@Path("/getActiveUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getActiveUsers() {
		return activeUserManager.getActiveUsers();
	}


	@POST
	@Path("/addFriendship")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addFriendship(Friendship friendship) {
		friendshipDAO.addFriendship(friendship);
	}

	@DELETE
	@Path("/removeFriendship")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeFriendship(Friendship friendship) {
		friendshipDAO.removeFriendship(friendship);
	}
}
