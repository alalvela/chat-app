package masterservice;

import java.util.Map;

import model.Host;

public interface MasterNodeServiceLocal {

	Map<String, Host> registerNode(Host host);
	
	boolean unregisterNode(Host host);
}
