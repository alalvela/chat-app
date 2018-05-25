package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.websocket.Session;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Host;
import model.User;
import restclient.RestClientSlaveServiceBean;
import session.UserSessionManager;
import util.ChatAppPropertiesBean;
import util.UrlBuilder;

/**
 * Session Bean implementation class ChatAppMainBean
 */
@Singleton
@LocalBean
@Startup
@DependsOn("ChatAppPropertiesBean")
public class ChatAppMainBean implements ChatAppMainLocal {
	
	private Host localHost;
	private Map<String, Host> hosts;
	private List<User> activeUsers;
	
	@EJB
	private ChatAppPropertiesBean propsBean;
	
	@EJB
	private RestClientSlaveServiceBean resteasyService;
	
	@PostConstruct
    private void init() {
		localHost = propsBean.getLocalHost();
		hosts = new HashMap<>();
		activeUsers = new ArrayList<>();
		
		Host master = propsBean.getMasterHost();
    	
		if(!master.equals(localHost)) {
        	hosts = resteasyService.register(localHost);
        	activeUsers = resteasyService.getActiveUsers();		
		}
		
    }
	
	@PreDestroy
	private void destroy() {
		System.out.println("USAO U PREDESTROY");
		if (!propsBean.getMasterHost().equals(localHost)) {
			Client client=ClientBuilder.newClient();
	    	String targetUrl = UrlBuilder.getUrl(propsBean.getMasterHost().getAddress(), propsBean.getChatAppName(), "master", "remove");
	        Response response = client.target(targetUrl).request(MediaType.TEXT_PLAIN).post(Entity.json(localHost));
		}
	
	}
	
	@Lock(LockType.WRITE)
	public void addNewNode(Host host) {
		hosts.put(host.getAlias(), host);
	}
	
	@Lock(LockType.WRITE)
	public void removeNode(Host host) {
		hosts.remove(host.getAlias());
	}

	@Lock(LockType.WRITE)
	public void addActiveUser(User user) {
		activeUsers.add(user);
	}
	
	@Lock(LockType.WRITE)
	public void removeActiveUser(User user) {
		Iterator<User> it = activeUsers.iterator();
		while(it.hasNext()) {
			if (user.getUsername().equals(it.next().getUsername())) {
				it.remove();
			}
		}
	}
	
	public Host getLocalHost() {
		return localHost;
	}

	// TEST --------------------------
	@Lock(LockType.READ)
	public Map<String, Host> getHosts() {
		return hosts;
	}
	
	@Lock(LockType.READ)
	public List<User> getActiveUsers() {
		return activeUsers;
	}
	
	

}
