package cluster;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import model.Host;
import util.ChatAppPropertiesBean;

@Singleton
@Startup
@LocalBean
@DependsOn("ChatAppPropertiesBean")
public class NodeManagerBean implements NodeManagerLocal{

	private Map<String, Host> nodes;

	@EJB
	ChatAppPropertiesBean props;
	
	@PostConstruct
    private void init() {
		nodes = new HashMap<>();
//		nodes.put(props.getMasterHost().getAlias(), props.getMasterHost());
    }
	
	@Lock(LockType.WRITE)
	public boolean addNode(Host host) {
		if (!containsNode(host)) {
			nodes.put(host.getAlias(), host);
			return true;
		}
		return false;
	}

	@Lock(LockType.WRITE)
	public boolean removeNode(Host host) {
		if(containsNode(host)) {
			nodes.remove(host.getAlias());
			return true;
		}
		return false;
	}

	@Lock(LockType.READ)
	public Map<String, Host> getAllNodes() {
		return nodes;
	}
	
	@Lock(LockType.READ)
	public boolean containsNode(Host host) {
		return nodes.containsKey(host.getAlias());
	}

}
