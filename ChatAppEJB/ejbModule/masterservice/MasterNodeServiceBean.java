package masterservice;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cluster.NodeManagerBean;
import model.Host;
import restclient.RestClientMasterServiceBean;

/**
 * Session Bean implementation class ChatAppManagerBean
 * 
 * samo master node ima pristup
 */
@Stateless
@LocalBean
public class MasterNodeServiceBean implements MasterNodeServiceLocal{

	@EJB
    NodeManagerBean nodeManager;
	
	@EJB
	RestClientMasterServiceBean restClient;
	
	public Map<String, Host> registerNode(Host host) {
		if(nodeManager.addNode(host)) {
			restClient.sendRegisterNodeRequest(host);
			return nodeManager.getAllNodes();	
		}
		return new HashMap<>();
	}

	
	public boolean unregisterNode(Host host) {
		restClient.sendRemoveNodeRequest(host);
		return nodeManager.removeNode(host);
	}
	
	public Map<String, Host> getRegisteredHosts() {
		return nodeManager.getAllNodes();
	}

}
