package cluster;

import java.util.Map;

import model.Host;

public interface NodeManagerLocal {

	boolean addNode(Host host);
	boolean removeNode(Host host);
	Map<String, Host> getAllNodes();
}
