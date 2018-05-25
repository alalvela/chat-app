package restendpoint;

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

import masterservice.MasterNodeServiceBean;
import model.Host;

@Stateless
@LocalBean
@Path("/master")
public class RestEPMasterBean {

	@EJB
	MasterNodeServiceBean managerBean;

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Host> registerNode(Host host) {
		Map<String, Host> ret = managerBean.registerNode(host);
		return ret;
	}
	
	@POST
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public boolean removeNode(Host host) {
		return managerBean.unregisterNode(host);
	}
	
	@GET
	@Path("/getRegistered")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Host> getRegisteredHosts() {
		return managerBean.getRegisteredHosts();
	}
}
