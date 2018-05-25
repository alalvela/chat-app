package restclient;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Host;
import model.User;
import util.ChatAppPropertiesBean;
import util.UrlBuilder;

/**
 * Session Bean implementation class ClusterServiceBean
 */
@Stateless
@LocalBean
public class RestClientSlaveServiceBean {
	
	@EJB
	private ChatAppPropertiesBean props;

    public HashMap<String, Host> register(Host host) {
    	Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "master", "register");
        Response response = client.target(targetUrl).request(MediaType.APPLICATION_JSON).post(Entity.json(host));
        HashMap<String, Host> ret = response.readEntity(new GenericType<HashMap<String, Host>>() { });
        return ret;
    }
    
    public boolean unregister(Host host) {
    	Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getChatAppName(), "master", "remove");
        Response response = client.target(targetUrl).request(MediaType.TEXT_PLAIN).post(Entity.json(host));
        String ret = response.readEntity(String.class);
        return Boolean.valueOf(ret);
    }
    
    public List<User> getActiveUsers() {
    	Client client=ClientBuilder.newClient();
    	String targetUrl = UrlBuilder.getUrl(props.getMasterHost().getAddress(), props.getUserAppName(), "user", "getActiveUsers");
    	Response response = client.target(targetUrl).request().get();
        List<User> ret = response.readEntity(new GenericType<List<User>>() { });
        return ret;
    }
	
}
