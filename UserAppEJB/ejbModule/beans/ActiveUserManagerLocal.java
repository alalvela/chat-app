package beans;

import java.util.List;

import model.Host;
import model.User;

public interface ActiveUserManagerLocal {

	public List<User> getActiveUsers();
	
	public boolean checkIfActive(User user);
	
	public boolean addUserToActive(User user);
	
	public boolean removeUserFromActive(User user);
}
