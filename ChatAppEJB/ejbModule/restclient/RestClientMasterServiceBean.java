package restclient;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import masterservice.MasterNodeServiceBean;
import model.Host;
import model.User;
import util.ChatAppPropertiesBean;
import util.UrlBuilder;

@Stateless
@LocalBean
public class RestClientMasterServiceBean {
	
	@EJB
	private MasterNodeServiceBean nodeServiceBean;
	
	@EJB
	private ChatAppPropertiesBean props;
	
	public void sendRegisterNodeRequest(Host host) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		nodeServiceBean.getRegisteredHosts()
			.forEach((key, val) -> {
				if (!(host.equals(val) || props.getMasterHost().equals(val))) {
					sendHostReqest(client, val.getAddress(), host, "addNewNode");
				}
			});
	}
	
	public void sendRemoveNodeRequest(Host host) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		nodeServiceBean.getRegisteredHosts()
			.forEach((key, val) -> {
				if (!(host.equals(val) || props.getMasterHost().equals(val))) {
					sendHostReqest(client, val.getAddress(), host, "removeNode");
				}
			});
	}
	
	public void sendAddActiveUserRequest(User user) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		nodeServiceBean.getRegisteredHosts()
			.forEach((key, val) -> {
				if (!props.getMasterHost().equals(val)) {
					sendUserReqest(client, val.getAddress(), user, "addActiveUser");
				}
			});
	}
	
	public void sendRemoveActiveUserRequest(User user) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		nodeServiceBean.getRegisteredHosts()
			.forEach((key, val) -> {
				if (!props.getMasterHost().equals(val)) {
					sendUserReqest(client, val.getAddress(), user, "removeActiveUser");
				}
			});
	}
	
	private void sendHostReqest(Client client, String slaveAddress, Host host, String method) {
		String targetUrl = UrlBuilder.getUrl(slaveAddress, props.getChatAppName(), "slave", method);
        client.target(targetUrl).request().post(Entity.json(host));
	}
	
	private void sendUserReqest(Client client, String slaveAddress, User user, String method) {
		String targetUrl = UrlBuilder.getUrl(slaveAddress, props.getChatAppName(), "slave", method);
        client.target(targetUrl).request().post(Entity.json(user));
	}

}
